// Function to search books and display results
function searchBooks() {
    var searchTitle = document.getElementById("searchInput").value.trim();
    if (searchTitle !== "") {
        $.ajax({
            type: "GET",
            url: "/books/search",
            data: {
                query: searchTitle
            },
            success: function (response) {
                displaySearchResults(response);
            },
            error: function () {
                alert("Error fetching search results.");
            }
        });
    } else {
        alert("Please enter a book title.");
    }
}

function displaySearchResults(books) {
    var resultsContainer = document.getElementById("searchResults");
    resultsContainer.innerHTML = "";

    if (books.length === 0) {
        resultsContainer.innerHTML = "<p>No books found.</p>";
    } else {
        var tableHtml = "<table><thead><tr><th>Title</th><th>Author</th><th>Genre</th><th>Action</th></tr></thead><tbody>";
        books.forEach(function (book) {
            tableHtml += "<tr>";
            tableHtml += "<td>" + book.title + "</td>";
            tableHtml += "<td>" + book.author + "</td>";
            tableHtml += "<td>" + book.genres.join(", ") + "</td>";
            // Add the add button for each book entry
            tableHtml += "<td><button class='add-button' data-book-id='" + book.id + "'>+</button></td>";
            tableHtml += "</tr>";
        });
        tableHtml += "</tbody></table>";
        resultsContainer.innerHTML = tableHtml;
    }
}

// Event delegation to handle add book functionality
$(document).on('click', '.add-button', function() {
    selectedBookId = $(this).data('book-id');
    openRatingModal();
});

function openRatingModal() {
    $('#ratingModal').show();
}

function closeRatingModal() {
    $('#ratingModal').hide();
}

$(document).on('click', '.star', function() {
    selectedRating = $(this).data('value');
    $('.star').each(function() {
        if ($(this).data('value') <= selectedRating) {
            $(this).addClass('selected');
        } else {
            $(this).removeClass('selected');
        }
    });
});

$('#submitRating').click(function() {
    if (selectedRating === 0) {
        alert("Please select a rating.");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/books/add",
        data: {
            bookId: selectedBookId,
            rating: selectedRating
        },
        success: function (response) {
            alert("Rating submitted successfully!");
            closeRatingModal();
            updateMyBooksSection();
            getRecommendations();
        },
        error: function () {
            alert("Error submitting rating.");
        }
    });
});

$('#cancelRating').click(function() {
    closeRatingModal();
});

function getRecommendations() {
    $.ajax({
        type: "GET",
        url: "/books/recommendations",
        success: function (response) {
            displayRecommendations(response);
        },
        error: function () {
            alert("Error fetching recommendations.");
        }
    });
}

function displayRecommendations(recommendations) {
    let container = $('#recommendedBooksTable tbody');
    container.empty(); // Clear existing recommendations
    
    if (recommendations.length > 0) {
        recommendations.forEach(function(book) {
            container.append(`
                <tr>
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.genres.join(", ")}</td>
                    <td>${book.matchPercentage || "N/A"}</td> <!-- Assuming you have matchPercentage in BookDto -->
                </tr>
            `);
        });
    } else {
        container.append("<tr><td colspan='4'>No recommendations available.</td></tr>");
    }
}

// Function to update My Books section
function updateMyBooksSection() {
    $.ajax({
        type: "GET",
        url: "/books/getMyBooks",
        success: function (books) {
            var tableBody = $('#myBooksTableBody');
            tableBody.empty();
    
            books.forEach(function(book) {
                var row = "<tr>";
                row += "<td>" + book.title + "</td>";
                row += "<td>" + book.author + "</td>";
                row += "<td>" + book.genres.join(", ") + "</td>";
                row += "<td>" + book.rating + "</td>";
                row += "</tr>";
                tableBody.append(row);
            });
        },
        error: function () {
            alert("Error fetching My Books.");
        }
    });
}

// Initial load of My Books and Recommended Books section
$(document).ready(function() {
    updateMyBooksSection();
    getRecommendations();
});
