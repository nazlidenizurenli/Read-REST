# Read&REST Book Recommendation System

## Overview

Read&REST is a book recommendation system that leverages both collaborative filtering and content-based filtering to provide personalized book recommendations. The system consists of a Java-based RESTful API for handling user interactions and a Python-based machine learning component for generating recommendations.

## Features

- **User Book Ratings:** Users can rate books they have read.
- **Personalized Recommendations:** The system generates book recommendations based on user preferences and reading history.
- **Hybrid Approach:** Combines collaborative filtering and content-based filtering for improved recommendations.
- **Web Interface:** A user-friendly web interface for users to interact with the system.

## Technologies Used

### Backend
- **Java:** For the RESTful API to handle user requests.
- **Spring Boot:** For creating the RESTful API.
- **MySQL:** For storing user data, book data, and ratings.
- **Flask:** Python framework for handling ML requests.

### Machine Learning
- **Python:** For data processing and machine learning tasks.
- **Pandas:** For data manipulation.
- **Scikit-learn:** For machine learning algorithms.
- **Numpy:** For numerical computations.

### Frontend
- **HTML/CSS/JavaScript:** For the web interface.
- **Bootstrap:** For responsive design.

## Project Structure

- **app.py:** Main Flask application file handling incoming requests.
- **preprocess.py:** Module for data preprocessing tasks.
- **encoding.py:** Module for encoding genres and ratings.
- **model.py:** Module for machine learning model training and predictions.
- **templates/**: HTML templates for the web interface.
- **static/**: Static files like CSS and JavaScript.
