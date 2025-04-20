import joblib
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
import numpy as np

# 🔢 Exemple de données manuelles
# Format: [salary, bonus, previousDelays]
X = np.array([
    [1000, 100, 0],
    [1200, 200, 1],
    [1500, 300, 0],
    [800, 50, 2],
    [1300, 250, 1],
    [1100, 100, 0],
    [1000, 150, 3],
])

# 🎯 Labels: 1 = payé à temps, 0 = retard
y = np.array([1, 0, 1, 0, 0, 1, 0])

# 🧠 Entraînement du modèle
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)
model = LogisticRegression()
model.fit(X_train, y_train)

# 💾 Sauvegarde
joblib.dump(model, "payment_model.pkl")

print("✅ Modèle entraîné et sauvegardé sous 'payment_model.pkl'")
