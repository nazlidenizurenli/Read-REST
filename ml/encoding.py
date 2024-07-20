# encoding.py
import numpy as np
from sklearn.preprocessing import OneHotEncoder, MinMaxScaler

def encode_genres_and_ratings(user_books, all_books, top_genres):
    """
    Encode genres and ratings for userBooks and allBooks.
    
    Parameters:
    user_books (list of dict): Filtered user books data with genres and ratings.
    all_books (list of dict): Filtered all books data with genres.
    top_genres (list of str): List of top genres.
    
    Returns:
    tuple: Encoded user books and all books, and user preferences.
    """
    # Initialize encoders and scalers
    genre_encoder = OneHotEncoder(categories=[top_genres], sparse=False)
    scaler = MinMaxScaler()

    def encode_book_data(books_json, include_ratings=False):
        """
        Encode genres and optionally ratings for a given set of books.
        
        Parameters:
        books_json (list of dict): List of books data.
        include_ratings (bool): Whether to include ratings in the encoding.
        
        Returns:
        list of dict: List of encoded books.
        """
        # Initialize lists for genres and ratings
        genres_data = []
        ratings_data = [] if include_ratings else None
        
        for book in books_json:
            genres = book.get('genres', [])
            rating = book.get('rating', 0) if include_ratings else None
            
            # Convert genres to vector
            genre_vector = np.zeros(len(top_genres))
            for genre in genres:
                if genre in top_genres:
                    genre_vector[top_genres.index(genre)] = 1
            genres_data.append(genre_vector)
            
            if include_ratings:
                ratings_data.append([rating])
        
        # Convert lists to numpy arrays
        genres_data = np.array(genres_data)
        
        if include_ratings:
            ratings_data = np.array(ratings_data)
            # Scale ratings
            ratings_data = scaler.fit_transform(ratings_data)
        
        # Combine genres and ratings if applicable
        encoded_books = []
        for i, book in enumerate(books_json):
            entry = {
                'id': book.get('id'),
                'title': book.get('title'),
                'author': book.get('author'),
                'genres': genres_data[i].tolist()
            }
            if include_ratings:
                entry['rating'] = ratings_data[i][0]
            encoded_books.append(entry)
        
        return encoded_books
    
    # Encode user books with ratings
    encoded_user_books = encode_book_data(user_books, include_ratings=True)
    
    # Encode all books without ratings
    encoded_all_books = encode_book_data(all_books, include_ratings=False)
    
    # Calculate user preferences
    user_preferences = {}
    for book in encoded_user_books:
        genres = book.get('genres')
        rating = book.get('rating')
        for idx, genre in enumerate(genres):
            if genre:
                if genre not in user_preferences:
                    user_preferences[genre] = 0
                user_preferences[genre] += rating
    
    # Normalize user preferences
    total_rating = sum(user_preferences.values())
    if total_rating > 0:
        for genre in user_preferences:
            user_preferences[genre] /= total_rating
    
    return encoded_user_books, encoded_all_books, user_preferences
