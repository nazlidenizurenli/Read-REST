import json
from collections import Counter

def refine_genres(genres):
    """
    Refine the list of genres to remove redundancies and ensure uniqueness.
    Exclude genres with more than one word (except 'self help'), and standardize 'self help' to 'selfhelp'.
    
    Parameters:
    genres (list of str): List of genre strings.
    
    Returns:
    list of str: Refined list of unique genres.
    """
    # Convert genres to lowercase for consistent comparison
    genres = [genre.lower() for genre in genres]
    
    # Normalize 'self help' to 'selfhelp'
    genres = ['selfhelp' if genre == 'self help' else genre for genre in genres]
    
    # Remove genres with more than one word, except 'selfhelp'
    genres = [genre for genre in genres if ' ' not in genre or genre == 'selfhelp']

    # Create a dictionary to store the shortest genre for each prefix
    genre_dict = {}

    # Iterate through genres to identify and keep the shortest genre
    for genre in genres:
        found = False
        for existing_genre in list(genre_dict.keys()):
            if genre.startswith(existing_genre[:4]):
                found = True
                if len(genre) < len(existing_genre):
                    genre_dict[genre] = True
                    del genre_dict[existing_genre]
                break
        if not found:
            genre_dict[genre] = True
    
    # Return the sorted list of unique genres
    return sorted(genre_dict.keys(), key=lambda g: genres.count(g), reverse=True)

def get_top_genres(all_books_json):
    """
    Identify the top 25 genres from allBooks data, refining them to remove redundancies and multi-word genres.
    
    Parameters:
    all_books_json (list of dict): List of all books data in JSON format.
    
    Returns:
    list of str: Top 25 refined genres.
    """
    # Extract all genres from allBooks
    all_genres = []
    for book in all_books_json:
        genres = book.get('genres', [])
        all_genres.extend(genres)
    
    # Refine genres to remove redundancies and ensure uniqueness
    refined_genres = refine_genres(all_genres)
    
    # Get the top 25 genres
    top_genres = refined_genres[:25]
    
    return top_genres

def filter_genres(books_json, top_genres, mode):
    """
    Filter genres in the book entries to keep only the top genres.
    Exclude entries with empty genres or fewer than 3 genres after filtering.
    
    Parameters:
    books_json (list of dict): List of books data in JSON format.
    top_genres (list of str): List of top genres.
    mode: True if user books, False if all books
    
    Returns:
    list of dict: List of books with filtered genres.
    """
    filtered_books = []
    for book in books_json:
        # Get and filter genres
        genres = book.get('genres', [])
        
        # If genres field is empty, skip this book
        if not genres:
            continue
        
        filtered_genres = [genre.lower() for genre in genres if genre.lower() in top_genres]
        
        # If there are fewer than 3 genres after filtering, skip this book
        if len(filtered_genres) < 3:
            continue
        
        # Keep only the first 3 genres from top genres
        filtered_genres = filtered_genres[:3]
        
        # Create a new book entry with filtered genres
        if mode:
            filtered_book = {
                'id': book.get('id'),
                'title': book.get('title'),
                'author': book.get('author'),
                'genres': filtered_genres,
                'rating': book.get('rating')
            }
        else:
            filtered_book = {
                'id': book.get('id'),
                'title': book.get('title'),
                'author': book.get('author'),
                'genres': filtered_genres
            }
        
        filtered_books.append(filtered_book)
    
    return filtered_books


# Example function to save JSON data for debugging purposes
def save_json(data, filename):
    """
    Save data to a JSON file.
    
    Parameters:
    data (any): Data to be saved.
    filename (str): Name of the file where data will be saved.
    """
    with open(filename, 'w') as f:
        json.dump(data, f, indent=4)
