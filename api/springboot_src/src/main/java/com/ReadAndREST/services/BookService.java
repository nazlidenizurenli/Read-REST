package com.ReadAndREST.services;

import com.ReadAndREST.models.Book;
import com.ReadAndREST.repositories.BookRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCase(query);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @PostConstruct
    public void init() {
        loadBooksFromCSV();
    }
    @Transactional
    public void loadBooksFromCSV() {
        try {
            ClassPathResource resource = new ClassPathResource("books.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(reader);

            List<Book> books = new ArrayList<>();
            for (CSVRecord csvRecord : csvParser) {
                Book book = new Book();
                book.setTitle(csvRecord.get("Book"));
                book.setAuthor(csvRecord.get("Author"));
                String genresString = csvRecord.get("Genres");
                genresString = genresString.replaceAll("\\[", "").replaceAll("\\]", ""); // Remove brackets
                String[] genresArray = genresString.split(", ");
                // Convert array to Set
                Set<String> genresSet = new HashSet<>(Arrays.asList(genresArray));
                // Set genres in Book entity
                book.setGenres(genresSet);
                books.add(book);
            }
            bookRepository.saveAll(books);

            csvParser.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
