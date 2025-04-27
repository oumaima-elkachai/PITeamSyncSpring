from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware
import base64, uuid, os
import cv2
import numpy as np
import urllib.request
from deepface import DeepFace

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class FaceCompareRequest(BaseModel):
    capturedImage: str
    storedImage: str

def decode_base64(img_str):
    img_data = base64.b64decode(img_str)
    np_arr = np.frombuffer(img_data, np.uint8)
    return cv2.imdecode(np_arr, cv2.IMREAD_COLOR)

@app.post("/verify")
def verify(req: FaceCompareRequest):
    try:
        img2 = decode_base64(req.capturedImage)

        if req.storedImage.startswith("http"):
            filename = f"/tmp/{uuid.uuid4()}.jpg"
            urllib.request.urlretrieve(req.storedImage, filename)
            result = DeepFace.verify(img1_path=filename, img2_path=img2, enforce_detection=False)
            os.remove(filename)
        else:
            img1 = decode_base64(req.storedImage)
            result = DeepFace.verify(img1_path=img1, img2_path=img2, enforce_detection=False)

        return {"verified": result["verified"]}
    except Exception as e:
        return {"verified": False, "error": str(e)}