package org.example.service;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import org.example.model.Book;
import org.example.model.Member;
import org.example.repository.BookRepository;
import org.example.repository.BorrowRepository;
import org.example.repository.MemberRepository;

/**
 * LibraryService
 */
public class LibraryService {
	Connection connection;
	BookRepository bookRepository;
	BorrowRepository borrowRepository;
	MemberRepository memberRepository;

	public LibraryService(Connection connection, BookRepository bookRepository, BorrowRepository borrowRepository,
			MemberRepository memberRepository) {
		this.connection = connection;
		this.bookRepository = bookRepository;
		this.borrowRepository = borrowRepository;
		this.memberRepository = memberRepository;

	}

	//////////////////
	// Book Methdos //
	//////////////////

	public void addBook(Book book) throws SQLException {
		bookRepository.saveBook(book);
	}

	public void removeBook(long id) throws SQLException {

		bookRepository.deleteBook(id);
	}

	public void updateBookTitle(long id, String title) throws SQLException {

		bookRepository.updateBookTitle(id, title);
	}

	public void updateBookAuthor(long id, String author) throws SQLException {

		bookRepository.updateBookAuthor(id, author);
	}

	public Book getBookById(long id) throws SQLException {

		Book book = bookRepository.getBookById(id);

		if (book == null) {
			throw new SQLException("Book with id " + id + " not found.");
		}

		return book;
	}

	public List<Book> listAllBooks() throws SQLException {
		List<Book> list = bookRepository.getAllBooks();

		return list;
	}

	////////////////////
	// Member Methdos //
	////////////////////

	public void registerMember(Member member) throws SQLException {
		memberRepository.saveMember(member);
	}

	public void deleteMember(long id) throws SQLException {
		memberRepository.deleteMember(id);
	}

	public Member getMemberById(long id) throws SQLException {
		Member member = memberRepository.getMemberById(id);

		if (member == null) {
			throw new SQLException("Member by id " + id + " is not found.");
		}

		return member;
	}

	public List<Member> listAllMembers() throws SQLException {
		List<Member> list = memberRepository.getAllMembers();

		return list;
	}

	////////////////////
	// Borrow Methdos //
	////////////////////

	public void borrowBook(long memberId, long bookId) throws SQLException {
		Member member = memberRepository.getMemberById(memberId);
		if (member == null) {
			throw new IllegalArgumentException("Member with id " + memberId + " not found");
		}

		Book book = bookRepository.getBookById(bookId);

		if (!book.isAvailable()) {
			throw new IllegalStateException("Book with id " + bookId + " is not available");
		}

		try {
			connection.setAutoCommit(false);

			borrowRepository.addBorrow(memberId, bookId);
			bookRepository.markBookUnavailable(bookId);

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new RuntimeException(
					"Error occured in the service layer while trying to borrow book with id "
							+ bookId,
					e);
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public void returnBook(long memberId, long bookId) throws SQLException {
		Member member = memberRepository.getMemberById(memberId);
		if (member == null) {
			throw new IllegalArgumentException("Member with id " + memberId + " not found");
		}

		try {
			connection.setAutoCommit(false);

			borrowRepository.returnBook(memberId, bookId);
			bookRepository.markBookAvailable(bookId);

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new RuntimeException(
					"Error occured in the service layer while trying to return book with id "
							+ bookId,
					e);
		} finally {
			connection.setAutoCommit(true);
		}
	}
}
