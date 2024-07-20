from flask import Flask, request, jsonify
import preprocess
import model
import encoding

app = Flask(__name__)

@app.route('/recommend', methods=['POST'])
def recommend():
    # Step 1: Parse the JSON payload
    data = request.json
    
    # Step 2: Extract userBooks and allBooks
    user_books = data.get('userBooks')
    all_books = data.get('allBooks')
    
    # Check if allBooks and userBooks are provided
    if not all_books or not user_books:
        return jsonify({"error": "Invalid input data"}), 400

    # Step 3: Preprocess data
    top_genres = preprocess.get_top_genres(all_books)
    filtered_all_books = preprocess.filter_genres(all_books, top_genres)
    filtered_user_books = preprocess.filter_genres(user_books, top_genres)
    
    # # Step 4: Encode data for ML engine.
    # Encode genres and ratings
    encoded_user_books, encoded_all_books = encoding.encode_genres_and_ratings(filtered_user_books, filtered_all_books, top_genres)
    
    # # Preprocess userBooks
    # user_books_df = preprocess.preprocess_user_books(user_books)
    
    # # Preprocess allBooks
    # all_books_df = preprocess.preprocess_all_books(all_books)

    # # Step 4: Train model
    # # Train the model using user_books_df
    # recommendation_model = model.train_model(user_books_df)
    
    # # Step 5: Generate recommendations
    # # Generate recommendations based on the trained model and all_books_df
    # recommendations = model.generate_recommendations(recommendation_model, user_books_df, all_books_df)

    # # Step 6: Return recommendations as JSON
    # return jsonify(recommendations)
    return jsonify(user_books)

if __name__ == '__main__':
    app.run(debug=True)
