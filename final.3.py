import cv2
import numpy as np
import tensorflow as tf

# Load your pre-trained model from the H5 file.
model = tf.keras.models.load_model('best.h5')

# Initialize the webcam (you can also use a video file).
cap = cv2.VideoCapture(0)

# Define a function to preprocess and classify a frame.
def classify_frame(frame_sequence, threshold=0.5):
    # Resize and preprocess each frame.
    resized_frames = [cv2.cvtColor(frame, cv2.COLOR_BGR2RGB) for frame in frame_sequence]
    resized_frames = [cv2.resize(frame, (64, 64)) for frame in resized_frames]
    normalized_frames = [frame / 255.0 for frame in resized_frames]
    frame_sequence = np.array(normalized_frames)
    frame_sequence = np.expand_dims(frame_sequence, axis=0)  # Add batch dimension

    # Predict on the frame sequence.
    predictions = model.predict(frame_sequence)

    # Assuming a binary classification task: 0 for Non-Violent, 1 for Violent
    predicted_class = "Violent" if predictions[0][0] < threshold else "Non-Violent"
    return predicted_class

# Adjust the threshold value here (e.g., 0.6 for a higher threshold)
threshold = 0.05

frame_sequence = []  # To store the frame sequence

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # Add the frame to the sequence
    frame_sequence.append(frame)

    # Maintain a fixed-length frame sequence (e.g., 16 frames)
    if len(frame_sequence) > 16:
        frame_sequence.pop(0)  # Remove the oldest frame

    # Ensure that the frame sequence contains 16 frames before making predictions
    if len(frame_sequence) == 16:
        action_label = classify_frame(frame_sequence, threshold=threshold)

        cv2.putText(frame, f"Action: {action_label}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        cv2.imshow('Action Detection', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
