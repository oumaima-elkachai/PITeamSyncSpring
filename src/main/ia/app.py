from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib

app = Flask(__name__)
CORS(app)  # Autoriser les requêtes depuis Angular

model = joblib.load("payment_model.pkl")

@app.route('/')
def home():
    return 'Bienvenue sur l\'API de prédiction !'

@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json()

    # Ajout d'une logique de retard, ici simplement un exemple
    # Si le paiement est effectué après la date prévue, on considère qu'il est en retard
    payment_date = data['paymentDate']  # Format : "YYYY-MM-DD"
    due_date = data['dueDate']  # Format : "YYYY-MM-DD"

    if payment_date > due_date:
        data['previousDelays'] += 1  # On incrémente le nombre de retards si c'est le cas

    features = [[data['salary'], data['bonus'], data['previousDelays']]]
    prediction = model.predict(features)
    return jsonify({'prediction': int(prediction[0])})


if __name__ == "__main__":
    app.run(port=5000)
