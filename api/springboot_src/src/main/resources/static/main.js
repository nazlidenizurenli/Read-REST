document.getElementById('search-button').addEventListener('click', function() {
    const query = document.getElementById('search-bar').value;

    fetch(`/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(data => {
            const searchResults = document.getElementById('search-results');
            searchResults.innerHTML = '';

            if (data.length === 0) {
                searchResults.innerHTML = '<p>No books found.</p>';
            } else {
                const table = document.createElement('table');
                table.innerHTML = `
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Genre</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                `;

                const tbody = table.querySelector('tbody');
                data.forEach(book => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.genre}</td>
                    `;
                    tbody.appendChild(row);
                });

                searchResults.appendChild(table);
            }
        })
        .catch(error => {
            console.error('Error fetching search results:', error);
        });
});
