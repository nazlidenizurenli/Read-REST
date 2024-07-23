import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

def compute_user_preference_vector(encoded_user_books):
    """
    Compute the user preference vector based on their book ratings.
    
    Parameters:
    encoded_user_books (list of list): Encoded user books data with genres and ratings.
    
    Returns:
    numpy.ndarray: User preference vector.
    """
    # Initialize the preference vector with zeros
    num_genres = len(encoded_user_books[0]) - 1 
    user_preference_vector = np.zeros(num_genres)
    total_ratings = 0
    
    # Aggregate the genre encodings weighted by the ratings
    for book in encoded_user_books:
        genres = book[:-1]
        rating = book[-1]
        
        user_preference_vector += np.array(genres) * rating
        total_ratings += rating
    
    # Normalize by dividing by the total ratings
    if total_ratings > 0:
        user_preference_vector /= total_ratings
    
    return user_preference_vector

def compute_similarity_scores(user_preference_vector, encoded_all_books):
    """
    Compute the cosine similarity scores between the user preference vector and all books.
    
    Parameters:
    user_preference_vector (numpy.ndarray): User preference vector.
    encoded_all_books (list of list): Encoded all books data with genres.
    
    Returns:
    list of float: Similarity scores for each book.
    """
    # Convert the encoded_all_books to a numpy array
    all_books_matrix = np.array([book['features'] for book in encoded_all_books])
    
    # Compute cosine similarity
    similarity_scores = cosine_similarity([user_preference_vector], all_books_matrix)
    
    # Flatten the result (it's a 2D array with 1 row)
    return similarity_scores.flatten()

def compute_similarity_scores(user_preference_vector, encoded_all_books):
    """
    Compute the cosine similarity scores between the user preference vector and all books.
    
    Parameters:
    user_preference_vector (numpy.ndarray): User preference vector.
    encoded_all_books (list of list): Encoded all books data with genres.
    
    Returns:
    numpy.ndarray: Similarity scores for each book.
    """
    # Convert the encoded_all_books to a numpy array
    all_books_matrix = np.array(encoded_all_books)  # 5837x25 matrix
    
    # Compute cosine similarity
    similarity_scores = cosine_similarity([user_preference_vector], all_books_matrix)  # 1x5837 matrix
    
    # Flatten the result to a 1D array
    return similarity_scores.flatten()  # 5837-dimensional array

def recommend_books(user_preference_vector, encoded_all_books, book_details, top_n=5):
    """
    Recommend books based on the user preference vector and similarity scores.
    
    Parameters:
    user_preference_vector (numpy.ndarray): User preference vector.
    encoded_all_books (list of list): Encoded all books data with genres.
    book_details (list of dict): List of book details with id, title, author, genres.
    top_n (int): Number of top recommendations to return.
    
    Returns:
    list of dict: Recommended books sorted by similarity score.
    """
    # Compute similarity scores
    similarity_scores = compute_similarity_scores(user_preference_vector, encoded_all_books)
    
    # Create a list of (index, score) tuples
    indexed_scores = list(enumerate(similarity_scores))
    
    # Sort by similarity score in descending order
    indexed_scores.sort(key=lambda x: x[1], reverse=True)
    
    # Get the indices of the top_n books
    top_indices = [index for index, score in indexed_scores[:top_n]]
    
    # Retrieve the top_n books and their details
    recommended_books = []
    for index in top_indices:
        book_detail = book_details[index]  # Fetch book details using the index
        score = similarity_scores[index]
        match_percentage = round(score * 100, 2)  # Convert similarity score to percentage
        
        # Construct the recommended book entry
        recommended_book = {
            'id': book_detail['id'],
            'title': book_detail['title'],
            'author': book_detail['author'],
            'genres': book_detail['genres'],
            'matchPercentage': match_percentage
        }
        recommended_books.append(recommended_book)
    
    return recommended_books