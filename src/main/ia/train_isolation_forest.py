import pandas as pd
import numpy as np
from pymongo import MongoClient
from sklearn.ensemble import IsolationForest
import joblib

# Connexion à MongoDB
client = MongoClient('mongodb://localhost:27017')
db = client.payroll_db
collection = db.payments

# Extraire les paiements depuis MongoDB
payments_data = list(collection.find())

# Convertir en DataFrame
df = pd.DataFrame(payments_data)

# Sélectionner uniquement la colonne 'amount' pour l'entraînement du modèle
df_model = df[['amount']]  # On ne garde que la colonne 'amount'

# Entraîner le modèle Isolation Forest
model = IsolationForest(contamination=0.2, random_state=42)  # 'contamination' définit le pourcentage d'anomalies attendues
model.fit(df_model)

# Sauvegarder le modèle
joblib.dump(model, "isolation_forest_model.pkl")

print("Modèle Isolation Forest entraîné et sauvegardé avec succès.")

# Fonction de détection des anomalies
def detect_anomalies(payments):
    # Charger le modèle préalablement sauvegardé
    model = joblib.load("isolation_forest_model.pkl")

    # Créer un DataFrame avec les paiements
    df = pd.DataFrame(payments)

    # Sélectionner la colonne 'amount' pour la détection d'anomalies
    amounts = df[['amount']]

    # Prédire les anomalies (1 = normal, -1 = anomalie)
    predictions = model.predict(amounts)

    anomalies = []

    # Rassembler les paiements détectés comme anomalies
    for i, payment in df.iterrows():
        if predictions[i] == -1:  # Si l'anomalie est détectée
            anomalies.append({
                "employeeId": payment.get('employeeId'),
                "amount": payment.get('amount'),
                "paymentId": payment.get('id'),
                "referenceNumber": payment.get('referenceNumber'),  # Ajout du numéro de référence
                "status": "ANOMALY",
                "reason": "Un montant inhabituel a été détecté par le système d’analyse"
            })


    return anomalies

