// // Function to handle book search and display
// function searchBooks() {
//     var searchInput = document.getElementById("searchInput").value.trim();

//     fetch(`/api/search?query=${searchInput}`)
//         .then(response => response.json())
//         .then(data => {
//             var searchResults = document.getElementById("searchResults");
//             searchResults.innerHTML = ""; // Clear previous results

//             data.forEach(book => {
//                 var row = document.createElement("div");
//                 row.classList.add("book-row");

//                 var title = document.createElement("h3");
//                 title.textContent = book.title;
//                 row.appendChild(title);

//                 var author = document.createElement("p");
//                 author.textContent = `Author: ${book.author}`;
//                 row.appendChild(author);

//                 var genre = document.createElement("p");
//                 genre.textContent = `Genre: ${book.genres.join(", ")}`;
//                 row.appendChild(genre);

//                 var addButton = document.createElement("button");
//                 addButton.textContent = "+";
//                 addButton.classList.add("add-button");
//                 addButton.onclick = function() {
//                     addToMyBooks(book.id);
//                 };
//                 row.appendChild(addButton);

//                 searchResults.appendChild(row);
//             });
//         })
//         .catch(error => {
//             console.error('Error searching books:', error);
//         });
// }

// // Function to handle adding a book to My Books with a rating
// function addToMyBooks(bookId) {
//     // Display the rating modal
//     var modal = document.getElementById("ratingModal");
//     modal.style.display = "block";

//     // Dynamically create rating buttons
//     var modalContent = document.getElementById("ratingButtons");
//     modalContent.innerHTML = "";
//     for (var i = 1; i <= 10; i++) {
//         var button = document.createElement("button");
//         button.innerText = i;
//         button.onclick = function() {
//             var rating = this.innerText;
//             addBookToMyBooks(bookId, rating);
//             modal.style.display = "none"; // Hide modal after rating
//         };
//         modalContent.appendChild(button);
//     }

//     // Close modal when the close button is clicked
//     var closeBtn = document.getElementsByClassName("close")[0];
//     closeBtn.onclick = function() {
//         modal.style.display = "none";
//     };

//     // Close modal if user clicks outside of it
//     window.onclick = function(event) {
//         if (event.target == modal) {
//             modal.style.display = "none";
//         }
//     };
// }

// // Function to add book to My Books with the given rating
// function addBookToMyBooks(bookId, rating) {
//     fetch('/api/add-book-to-my-books', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify({ bookId: bookId, rating: rating }),
//     })
//     .then(response => {
//         if (!response.ok) {
//             throw new Error('Failed to add book to My Books');
//         }
//         return response.json();
//     })
//     .then(data => {
//         console.log('Book added to My Books:', data);
//         // Refresh My Books table or update UI as needed
//         fetchMyBooks(); // Optional: Update My Books table after adding a book
//     })
//     .catch(error => {
//         console.error('Error adding book to My Books:', error);
//     });
// }

// // Function to fetch and display user's My Books
// function fetchMyBooks() {
//     fetch('/api/my-books')
//         .then(response => response.json())
//         .then(data => {
//             var myBooksTable = document.getElementById("myBooksTable").getElementsByTagName('tbody')[0];
//             myBooksTable.innerHTML = ""; // Clear previous content

//             data.forEach(book => {
//                 var row = myBooksTable.insertRow();
//                 var titleCell = row.insertCell(0);
//                 var authorCell = row.insertCell(1);
//                 var genreCell = row.insertCell(2);
//                 var actionCell = row.insertCell(3);

//                 titleCell.textContent = book.title;
//                 authorCell.textContent = book.author;
//                 genreCell.textContent = book.genres.join(", ");

//                 var removeButton = document.createElement("button");
//                 removeButton.textContent = "Remove";
//                 removeButton.onclick = function() {
//                     removeBookFromMyBooks(book.id);
//                 };
//                 actionCell.appendChild(removeButton);
//             });
//         })
//         .catch(error => {
//             console.error('Error fetching My Books:', error);
//         });
// }

// // Function to remove book from My Books
// function removeBookFromMyBooks(bookId) {
//     fetch(`/api/remove-book-from-my-books/${bookId}`, {
//         method: 'DELETE',
//     })
//     .then(response => {
//         if (!response.ok) {
//             throw new Error('Failed to remove book from My Books');
//         }
//         return response.json();
//     })
//     .then(data => {
//         console.log('Book removed from My Books:', data);
//         fetchMyBooks(); // Refresh My Books table after removing a book
//     })
//     .catch(error => {
//         console.error('Error removing book from My Books:', error);
//     });
// }

// // Initial fetch of My Books when the page loads
// window.onload = fetchMyBooks;
