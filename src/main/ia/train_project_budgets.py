from urllib import request

import pandas as pd
import matplotlib.pyplot as plt
from pymongo import MongoClient
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import mean_squared_error
import joblib


# Connexion à MongoDB
client = MongoClient('mongodb://localhost:27017')
db = client.payroll_db
collection = db.project_budgets

# Charger les documents de budget
documents = list(collection.find())

# Filtrage pour ne garder que les champs nécessaires
data = []
for doc in documents:
    if "allocatedFunds" in doc and "usedFunds" in doc:
        allocated = doc["allocatedFunds"]
        used = doc["usedFunds"]

        # On crée une cible : évaluer la "performance" budgétaire
        # Exemple simple : + proche de 100%, mieux c’est
        if allocated > 0:
            score = max(0, min(100, (used / allocated) * 100))
            data.append({
                "allocated_funds": allocated,
                "used_funds": used,
                "budget_score": score
            })

# Convertir en DataFrame
df = pd.DataFrame(data)

if df.empty:
    print("⚠️ Aucune donnée valide trouvée.")
    exit()

# Visualiser la relation entre allocated_funds, used_funds et budget_score
plt.scatter(df['allocated_funds'], df['budget_score'], label='Allocated vs Budget Score')
plt.scatter(df['used_funds'], df['budget_score'], label='Used vs Budget Score', color='red')
plt.xlabel('Funds')
plt.ylabel('Budget Score')
plt.legend()
plt.show()

# Préparation du modèle
X = df[["allocated_funds", "used_funds"]]
y = df["budget_score"]

# Normaliser les données
scaler = StandardScaler()                  # Définir le scaler
input_data = request.get_json()
X_input = pd.DataFrame([input_data], columns=["allocated_funds", "used_funds"])
X_scaled = scaler.fit_transform(X)         # L'entraîner sur X

# Sauvegarder le scaler APRÈS l’avoir entraîné
joblib.dump(scaler, "budget_scaler.pkl")

# Séparer les données en ensembles d'entraînement et de test
X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.2, random_state=42)

# Entraînement du modèle
model = RandomForestRegressor(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

# Sauvegarder le modèle
joblib.dump(model, "budget_model.pkl")

print("✅ Modèle entraîné et sauvegardé avec succès.")

# Vérification de la performance du modèle
y_pred = model.predict(X_test)

# Afficher quelques résultats
print("Prédictions :", y_pred[:10])
print("Réels :", y_test[:10])

# Calculer l'erreur quadratique moyenne
mse = mean_squared_error(y_test, y_pred)
print("Mean Squared Error :", mse)
