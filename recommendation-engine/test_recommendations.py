import requests
import json
from datetime import datetime
import sys
from pprint import pprint

def test_recommendations(participant_id):
    """Test the recommendations endpoint for a given participant ID"""
    
    url = f'http://localhost:5000/recommendations/{participant_id}'
    
    try:
        print(f"\nTesting recommendations for participant: {participant_id}")
        print("-" * 50)
        
        # Make request to recommendations endpoint
        response = requests.get(url)
        
        # Check if request was successful
        if response.status_code == 200:
            recommendations = response.json()
            
            if isinstance(recommendations, list):
                print(f"Found {len(recommendations)} recommendations\n")
                
                for i, rec in enumerate(recommendations, 1):
                    print(f"Recommendation #{i}:")
                    print(f"Title: {rec['title']}")
                    print(f"Type: {rec['event_type']}")
                    print(f"Start Date: {rec['start_date']}")
                    print(f"Total Score: {rec['score']}")
                    print("\nScore Breakdown:")
                    for key, value in rec['score_breakdown'].items():
                        print(f"- {key}: {value}")
                    print("-" * 30)
            else:
                print("No recommendations found or invalid response format")
                
        else:
            print(f"Error: Received status code {response.status_code}")
            print(f"Response: {response.text}")
            
    except requests.exceptions.ConnectionError:
        print("Error: Could not connect to recommendation service")
        print("Make sure the Flask application is running on port 5000")
    except Exception as e:
        print(f"Error occurred: {str(e)}")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python test_recommendations.py <participant_id>")
        sys.exit(1)
        
    participant_id = sys.argv[1]
    test_recommendations(participant_id)
