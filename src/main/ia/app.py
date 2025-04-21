from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import numpy as np
from pymongo import MongoClient
from train_isolation_forest import detect_anomalies

app = Flask(__name__)

# Configurer CORS
CORS(app, resources={r"/*": {"origins": "*", "methods": ["OPTIONS", "POST", "GET"]}})

# Charger le modèle de prédiction
model = joblib.load("payment_model.pkl")

# Connexion à la base de données MongoDB
client = MongoClient('mongodb://localhost:27017')
db = client.payroll_db
payments_collection = db.payments

# Route de test
@app.route('/')
def home():
    return 'Bienvenue sur l\'API de prédiction !'

# Route de prédiction
@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json()
    features = [[data['salary'], data['bonus'], data['previousDelays']]]
    prediction = model.predict(features)
    return jsonify({'prediction': int(prediction[0])})

# Ajouter une route pour les requêtes OPTIONS
@app.route('/detect-anomalies', methods=['OPTIONS'])
def options_detect_anomalies():
    return '', 200  # Répondre avec un statut 200 OK pour la requête OPTIONS

# Détection d'anomalies
@app.route('/detect-anomalies', methods=['POST'])
def detect():
    data = request.get_json()
    print("Requête POST reçue avec les données :", data)

    anomalies = detect_anomalies(data)
    print("Anomalies détectées :", anomalies)

    return jsonify(anomalies)


if __name__ == "__main__":
    app.run(port=5000)
