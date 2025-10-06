package org.example;

import org.example.model.Book;
import org.example.model.Member;
import org.example.service.LibraryService;

import java.util.List;

/**
 * Library
 * Simple facade over LibraryService for easier access.
 */
public class Library {
	private final LibraryService libraryService;

	public Library(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	// ----------------
	// Book methods
	// ----------------
	public void addBook(Book book) {
		libraryService.addBook(book);
	}

	public void removeBook(long id) {
		libraryService.removeBook(id);
	}

	public void updateBookTitle(long id, String title) {
		libraryService.updateBookTitle(id, title);
	}

	public void updateBookAuthor(long id, String author) {
		libraryService.updateBookAuthor(id, author);
	}

	public Book getBookById(long id) {
		return libraryService.getBookById(id);
	}

	public List<Book> listAllBooks() {
		return libraryService.listAllBooks();
	}

	// ----------------
	// Member methods
	// ----------------
	public void registerMember(Member member) {
		libraryService.registerMember(member);
	}

	public void deleteMember(long id) {
		libraryService.deleteMember(id);
	}

	public Member getMemberById(long id) {
		return libraryService.getMemberById(id);
	}

	public List<Member> listAllMembers() {
		return libraryService.listAllMembers();
	}

	// ----------------
	// Borrow methods
	// ----------------
	public void borrowBook(long memberId, long bookId) {
		libraryService.borrowBook(memberId, bookId);
	}

	public void returnBook(long memberId, long bookId) {
		libraryService.returnBook(memberId, bookId);
	}
}
