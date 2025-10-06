package org.example;

import java.sql.Connection;
import java.sql.SQLException;

import org.example.model.Book;
import org.example.repository.BookRepository;
import org.example.repository.BorrowRepository;
import org.example.repository.MemberRepository;
import org.example.service.LibraryService;

public class App {

	public static void main(String[] args) {
		DatabaseUtils databaseUtils = new DatabaseUtils();

		// try-with-resources ensures connection is closed automatically
		try (Connection connection = databaseUtils.getConnection()) {

			// Instantiate repositories
			BookRepository bookRepo = new BookRepository(databaseUtils);
			MemberRepository memberRepo = new MemberRepository(databaseUtils);
			BorrowRepository borrowRepo = new BorrowRepository(connection);

			// Instantiate service
			LibraryService libraryService = new LibraryService(connection, bookRepo, borrowRepo,
					memberRepo);

			// Instantiate library
			Library library = new Library(libraryService);

			// Example usage
			Book book1 = new Book(0, "1984", "George Orwell", true);
			library.addBook(book1);

			// You could add more calls like borrowBook, registerMember, etc.

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
