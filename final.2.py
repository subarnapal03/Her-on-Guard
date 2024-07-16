import cv2
import numpy as np
import tensorflow as tf

# Load your pre-trained model from the H5 file.
model = tf.keras.models.load_model('model.h5')

# Initialize the webcam (you can also use a video file).
cap = cv2.VideoCapture(0)

# Define a function to preprocess and classify a frame.
def classify_frame(frame, threshold=0.5):
    # Resize the frame to match the expected input shape of the model (128, 128).
    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    frame = cv2.resize(frame, (128, 128))
    frame = frame / 255.0  # Normalize pixel values to the range [0, 1]
    frame = np.expand_dims(frame, axis=0)  # Add batch dimension
    predictions = model.predict(frame)
    # Assuming a binary classification task: 0 for Non-Violent, 1 for Violent
    predicted_class = "Violent" if predictions[0][0] > threshold else "Non-Violent"
    return predicted_class

# Adjust the threshold value here according to light
threshold = 0.5

frame_buffer = []  # To store a sequence of frames for smoother predictions

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # Preprocess and classify the frame
    action_label = classify_frame(frame, threshold=threshold)

    frame_buffer.append(action_label)

    # Maintain a fixed-length frame sequence (e.g., 10 frames)
    if len(frame_buffer) > 10:
        frame_buffer.pop(0)  

    # Count the occurrences of "Violent" in the frame sequence
    violent_count = frame_buffer.count("Violent")

    # Make a decision based on the majority vote
    if violent_count >= 10:
        final_action_label = "Violent"
    else:
        final_action_label = "Non-Violent"

    cv2.putText(frame, f"Action: {final_action_label}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 2)
    cv2.imshow('Action Detection', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
