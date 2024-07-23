import numpy as np
from sklearn.preprocessing import MultiLabelBinarizer

def encode_genres(genres, top_genres):
    """
    One-hot encode the genres based on the top genres.
    
    Parameters:
    genres (list of str): List of genres to encode.
    top_genres (list of str): List of top genres for encoding.
    
    Returns:
    numpy array: One-hot encoded representation of the genres.
    """
    mlb = MultiLabelBinarizer(classes=top_genres)
    encoded_genres = mlb.fit_transform([genres])[0]
    return encoded_genres

def encode_user_books(user_books, top_genres):
    """
    Encode user books with both genres and ratings.
    
    Parameters:
    user_books (list of dict): List of user books with genres and ratings.
    top_genres (list of str): List of top genres for encoding.
    
    Returns:
    list of numpy arrays: List of encoded user books.
    """
    encoded_user_books = []
    for book in user_books:
        genres = book['genres']
        rating = book['rating']
        encoded_genres = encode_genres(genres, top_genres)
        encoded_book = np.append(encoded_genres, rating)
        encoded_user_books.append(encoded_book)
    return encoded_user_books

def encode_all_books(all_books, top_genres):
    """
    Encode all books with genres only.
    
    Parameters:
    all_books (list of dict): List of all books with genres.
    top_genres (list of str): List of top genres for encoding.
    
    Returns:
    list of numpy arrays: List of encoded all books.
    """
    encoded_all_books = []
    for book in all_books:
        genres = book['genres']
        encoded_genres = encode_genres(genres, top_genres)
        encoded_all_books.append(encoded_genres)
    return encoded_all_books

def encode_genres_and_ratings(user_books, all_books, top_genres):
    """
    Encode genres and ratings for user books and all books.
    
    Parameters:
    user_books (list of dict): List of user books with genres and ratings.
    all_books (list of dict): List of all books with genres.
    top_genres (list of str): List of top genres for encoding.
    
    Returns:
    tuple: Encoded user books and all books.
    """
    encoded_user_books = encode_user_books(user_books, top_genres)
    encoded_all_books = encode_all_books(all_books, top_genres)
    
    return encoded_user_books, encoded_all_books
