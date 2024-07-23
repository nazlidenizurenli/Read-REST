from flask import Flask, request, jsonify
import preprocess
import cosine_similarity
import encoding
import click

app = Flask(__name__)

def calculate_match_percentage(user_preference_vector, book):
    """
    Calculate the match percentage for a book based on user preference vector.
    
    Parameters:
    user_preference_vector (numpy.ndarray): User preference vector.
    book (dict): Book data with genres.
    
    Returns:
    float: Match percentage.
    """
    # Convert the book's genres to a vector
    book_vector = np.array([book.get('genres', [0]*len(user_preference_vector))])
    
    # Calculate similarity between the user preference vector and the book vector
    similarity = cosine_similarity.cosine_similarity([user_preference_vector], book_vector)
    
    # Normalize similarity to get a percentage
    return (similarity[0][0] * 100)  # Convert to percentage

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
    click.secho(f"Finding top genres...", fg="blue", bold=True)
    top_genres = preprocess.get_top_genres(all_books)
    click.secho(f"Filtering user data and books database...", fg="blue", bold=True)
    filtered_all_books = preprocess.filter_genres(all_books, top_genres, False)
    filtered_user_books = preprocess.filter_genres(user_books, top_genres, True)
    
    # Step 4: Encode data
    click.secho(f"Encoding book vectors...", fg="blue", bold=True)
    encoded_user_books, encoded_all_books = encoding.encode_genres_and_ratings(filtered_user_books, filtered_all_books, top_genres)
    # print("Encoded user books:", len(encoded_user_books[0]))
    # print("Encoded all books:", len(encoded_all_books[0]))
    
    # Step 5: Compute the user preference vector
    click.secho(f"Building user preference vector...", fg="yellow", bold=True)
    user_preference_vector = cosine_similarity.compute_user_preference_vector(encoded_user_books)
    # print(user_preference_vector)
    # print(len(user_preference_vector))
    
    # Step 6: Recommend books
    click.secho(f"Preparing recommendations...", fg="red", bold=True)
    print("User data dimensions: ", len(user_preference_vector))
    print("All books data dimensions: ", len(encoded_all_books), len(encoded_all_books[0]))
    book_details = [{'id': book['id'], 'title': book['title'], 'author': book['author'], 'genres': book['genres']} for book in filtered_all_books]
    recommended_books = cosine_similarity.recommend_books(user_preference_vector, encoded_all_books, book_details, top_n=5)
    
    # Return the recommendations as JSON
    click.secho(f"Done with recommendation generation!", fg="yellow", bold=True)
    return jsonify(recommended_books)

if __name__ == '__main__':
    app.run(debug=True)
