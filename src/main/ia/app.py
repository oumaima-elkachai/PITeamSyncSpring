from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import numpy as np
from pymongo import MongoClient
from sklearn.preprocessing import StandardScaler

from train_isolation_forest import detect_anomalies
import pandas as pd


app = Flask(__name__)

# Configurer CORS
CORS(app, resources={r"/*": {"origins": "*", "methods": ["OPTIONS", "POST", "GET"]}})

# Charger le modèle de prédiction
model = joblib.load("payment_model.pkl")
budget_model = joblib.load("budget_model.pkl")

# Connexion à la base de données MongoDB
client = MongoClient('mongodb://localhost:27017')
db = client.payroll_db
payments_collection = db.payments

# Route de test
@app.route('/')
def home():
    return 'Bienvenue sur l\'API de prédiction !'

# Route de prédiction
#@app.route("/predict", methods=["POST"])
#def predict():
    #data = request.get_json()
    #features = [[data['salary'], data['bonus'], data['previousDelays']]]
    #prediction = model.predict(features)
    #return jsonify({'prediction': int(prediction[0])})


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


scaler = joblib.load("budget_scaler.pkl")
@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    allocated = data.get("allocated_funds")
    used = data.get("used_funds")

    if allocated is None or used is None:
        return jsonify({"error": "Missing input"}), 400

    features = np.array([[allocated, used]])
    features_scaled = scaler.transform(features)
    prediction = budget_model.predict(features_scaled)

    return jsonify({
        "budget_score": round(prediction[0], 2)
    })


if __name__ == "__main__":
    app.run(port=5000)
