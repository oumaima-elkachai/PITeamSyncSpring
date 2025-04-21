import pandas as pd
from pymongo import MongoClient
from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import numpy as np

from sklearn.ensemble import IsolationForest

app = Flask(__name__)
CORS(app)  # Autoriser les requêtes depuis Angular

model = joblib.load("payment_model.pkl")

client = MongoClient('mongodb://localhost:27017')
db = client.payroll_db
payments_collection = db.payments

@app.route('/')
def home():
    return 'Bienvenue sur l\'API de prédiction !'

@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json()
    features = [[data['salary'], data['bonus'], data['previousDelays']]]
    prediction = model.predict(features)
    return jsonify({'prediction': int(prediction[0])})

@app.route('/detect-anomalies', methods=['POST'])
def detect_anomalies():
    payments = request.json
    employee_groups = {}

    # Group by employee
    for payment in payments:
        emp_id = payment['employeeId']
        if emp_id not in employee_groups:
            employee_groups[emp_id] = []
        employee_groups[emp_id].append(payment)

    anomalies = []

    for emp_id, emp_payments in employee_groups.items():
        amounts = [p['amount'] for p in emp_payments]
        if len(amounts) < 2:
            continue  # skip if not enough data

        mean = np.mean(amounts)
        std = np.std(amounts)

        # Calcul des seuils
        threshold_percentage = 2 * mean  # 200% de la moyenne
        threshold_std = mean + 1.5 * std  # 1.5 fois l'écart-type

        for payment in emp_payments:
            amount = payment['amount']
            reference_number = payment.get('referenceNumber', "Référence manquante")  # Fetch from payment data
            payment_id = payment.get('id', None)  # Récupérer 'id' avec une valeur par défaut (None)

            if amount > threshold_percentage:  # Si supérieur à 200% de la moyenne
                anomalies.append({
                    "employeeId": emp_id,
                    "referenceNumber": reference_number,  # Afficher la référence de l'employé depuis le paiement
                    "month": payment.get('month'),
                    "amount": amount,
                    "mean": round(mean, 2),
                    "std": round(std, 2),
                    "anomaly": "HIGH",
                    "reason": "Montant élevé inhabituel (200% de la moyenne)",
                    "paymentId": payment_id if payment_id else "ID manquant"
                })
            elif amount > threshold_std:  # Si supérieur à la moyenne + 1.5 fois l'écart-type
                anomalies.append({
                    "employeeId": emp_id,
                    "referenceNumber": reference_number,  # Afficher la référence de l'employé depuis le paiement
                    "month": payment.get('month'),
                    "amount": amount,
                    "mean": round(mean, 2),
                    "std": round(std, 2),
                    "anomaly": "HIGH",
                    "reason": "Montant élevé inhabituel (basé sur l'écart-type)",
                    "paymentId": payment_id if payment_id else "ID manquant"
                })
            elif amount < mean - 2 * std:  # Si inférieur à la moyenne - 2 fois l'écart-type
                anomalies.append({
                    "employeeId": emp_id,
                    "referenceNumber": reference_number,  # Afficher la référence de l'employé depuis le paiement
                    "month": payment.get('month'),
                    "amount": amount,
                    "mean": round(mean, 2),
                    "std": round(std, 2),
                    "anomaly": "LOW",
                    "reason": "Montant faible inhabituel",
                    "paymentId": payment_id if payment_id else "ID manquant"
                })

    return jsonify(anomalies)



if __name__ == "__main__":
    app.run(port=5000)
