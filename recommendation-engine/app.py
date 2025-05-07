from flask import Flask, jsonify
import pandas as pd
import requests
from flask_cors import CORS
from collections import Counter
from datetime import datetime
import logging

app = Flask(__name__)
CORS(app)

# Add logging configuration
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# API endpoints - update port to match your Spring Boot application
BASE_URL = 'http://localhost:8084/api'
EVENTS_API = f'{BASE_URL}/events'
PARTICIPATIONS_API = f'{BASE_URL}/participations/by-participant'
EVENT_ANALYTICS_API = f'{BASE_URL}/analytics/events'

# Add root route
@app.route('/')
def index():
    return jsonify({
        'name': 'Event Recommendation API',
        'version': '1.0',
        'endpoints': {
            'recommendations': '/recommendations/<participant_id>',
            'health': '/health'
        }
    })

# Add health check endpoint
@app.route('/health')
def health():
    try:
        # Test connection to Spring Boot API
        response = requests.get(f"{BASE_URL}/events")
        if response.ok:
            return jsonify({
                'status': 'healthy',
                'spring_boot_api': 'connected'
            })
        else:
            return jsonify({
                'status': 'degraded',
                'spring_boot_api': 'disconnected'
            }), 503
    except:
        return jsonify({
            'status': 'unhealthy',
            'spring_boot_api': 'unreachable'
        }), 503

def get_event_type_score(preferred_types, event_type, max_score=50):
    """Calculate score based on event type preference (0-50 points)"""
    if not preferred_types:
        return 0
    
    # Get percentage of this event type in user's history
    type_frequency = preferred_types.get(event_type, 0)
    total_participations = sum(preferred_types.values())
    
    if total_participations == 0:
        return 0
        
    return int((type_frequency / total_participations) * max_score)

def get_engagement_score(event_id, max_score=30):
    """Get engagement score based on event analytics (0-30 points)"""
    try:
        response = requests.get(f"{EVENT_ANALYTICS_API}/{event_id}")
        if response.status_code == 200:
            analytics = response.json()
            engagement_score = analytics.get('engagementScore', 0)
            # Convert engagement score from 0-1 to 0-30
            return int(engagement_score * max_score)
    except:
        pass
    return 0

def get_recency_score(event_date, max_score=20):
    """Calculate recency score (0-20 points)"""
    try:
        event_date = datetime.strptime(event_date, '%Y-%m-%d').date()
        days_until = (event_date - datetime.now().date()).days
        
        if 0 <= days_until <= 14:  # Next two weeks
            return max(0, max_score - (days_until * (max_score/14)))
    except:
        pass
    return 0

@app.route('/recommendations/<string:participant_id>')
def recommend(participant_id):
    logger.info(f"Received recommendation request for participant: {participant_id}")
    try:
        # 1. Get user's participation history
        logger.info(f"Fetching participations from: {PARTICIPATIONS_API}/{participant_id}")
        response = requests.get(f"{PARTICIPATIONS_API}/{participant_id}")
        logger.info(f"Participation response: {response.text}")
        
        participations = response.json() if response.ok else []
        
        # Analyze event types from participation history
        event_type_counts = Counter()
        for participation in participations:
            event_id = participation.get('eventId')
            if event_id:
                event_response = requests.get(f"{EVENTS_API}/{event_id}")
                if event_response.ok:
                    event = event_response.json()
                    event_type = event.get('eventType')
                    if event_type:
                        event_type_counts[event_type] += 1

        # Get most common event type
        most_common_type = event_type_counts.most_common(1)
        participation_pattern = {
            'most_frequent_event_type': most_common_type[0][0] if most_common_type else 'No events yet',
            'type_distribution': dict(event_type_counts),
            'total_participations': len(participations)
        }
        
        participated_ids = {p.get('eventId') for p in participations if p.get('eventId')}
        logger.info(f"User has participated in {len(participated_ids)} events")
        
        # 2. Get all available events
        logger.info("Fetching all events")
        all_events_response = requests.get(EVENTS_API)
        logger.info(f"All events response: {all_events_response.text}")
        
        if not all_events_response.ok:
            logger.error(f"Failed to get events: {all_events_response.status_code}")
            return jsonify({'error': 'Failed to get events'}), all_events_response.status_code
            
        all_events = all_events_response.json()
        logger.info(f"Found {len(all_events)} total events")
        
        # 3. Filter and categorize events
        today = datetime.now().date()
        future_events = []
        past_events = []
        invalid_dates = []
        
        for event in all_events:
            event_id = event.get('idEvent')
            start_date = event.get('startDate')
            
            try:
                if not start_date:
                    logger.warning(f"Event {event_id} has no start date")
                    invalid_dates.append(event_id)
                    continue
                    
                event_date = datetime.strptime(start_date, '%Y-%m-%d').date()
                
                if event_date >= today:
                    future_events.append(event)
                    logger.info(f"Future event found: {event_id} on {start_date}")
                else:
                    past_events.append(event)
                    logger.info(f"Past event found: {event_id} on {start_date}")
                    
            except Exception as e:
                logger.error(f"Error processing date for event {event_id}: {str(e)}")
                invalid_dates.append(event_id)
        
        logger.info(f"Events breakdown - Future: {len(future_events)}, Past: {len(past_events)}, Invalid: {len(invalid_dates)}")
        
        # 4. Filter out participated events
        candidate_events = [e for e in future_events if e.get('idEvent') not in participated_ids]
        logger.info(f"Found {len(candidate_events)} candidate events after filtering participated events")
        
        if not candidate_events:
            return jsonify({
                'message': 'No upcoming events available for recommendations',
                'total_events': len(all_events),
                'future_events': len(future_events),
                'past_events': len(past_events),
                'invalid_dates': len(invalid_dates),
                'participated_events': len(participated_ids),
                'participation_pattern': participation_pattern
            }), 200

        # 5. Score and rank events
        recommendations = []
        for event in candidate_events:
            try:
                score = 0
                event_id = event.get('idEvent')
                event_type = event.get('eventType')
                
                # Scoring
                type_score = get_event_type_score(event_type_counts, event_type)
                engagement_score = get_engagement_score(event_id)
                recency_score = get_recency_score(event.get('startDate'))
                
                score = type_score + engagement_score + recency_score
                
                logger.info(f"Scored event {event_id}: type={type_score}, engagement={engagement_score}, recency={recency_score}")
                
                recommendations.append({
                    'event_id': event_id,
                    'title': event.get('title', 'Untitled'),
                    'description': event.get('description', ''),
                    'event_type': event_type,
                    'start_date': event.get('startDate'),
                    'start_time': event.get('startTime'),
                    'imageUrl': event.get('imageUrl'),  # Add the image URL
                    'score': score,
                    'score_breakdown': {
                        'type_preference': type_score,
                        'engagement': engagement_score,
                        'recency': recency_score
                    }
                })
            except Exception as e:
                logger.error(f"Error scoring event {event.get('idEvent')}: {str(e)}")
                continue
        
        # Sort and return top 5
        sorted_recs = sorted(recommendations, key=lambda x: x['score'], reverse=True)[:5]
        logger.info(f"Returning {len(sorted_recs)} recommendations")
        return jsonify({
            'recommendations': sorted_recs,
            'participation_pattern': participation_pattern
        }), 200

    except Exception as e:
        logger.error(f"Recommendation error: {str(e)}", exc_info=True)
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    print(f"Server starting... Access the API documentation at http://127.0.0.1:5000/")
    app.run(port=5000, debug=True)