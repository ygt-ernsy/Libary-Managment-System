package org.example.service;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import org.example.model.Book;
import org.example.model.Member;
import org.example.repository.BookRepository;
import org.example.repository.BorrowRepository;
import org.example.repository.MemberRepository;
import org.postgresql.core.SqlCommand;

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

	public void addBook(Book book) {
		try {
			bookRepository.saveBook(book);
		} catch (SQLException e) {
			throw new RuntimeException("Error in adding book " + book.getId(), e);
		}
	}

	public void removeBook(long id) {
		try {
			bookRepository.deleteBook(id);
		} catch (SQLException e) {
			throw new RuntimeException("Error in removing book " + id, e);
		}
	}

	public void updateBookTitle(long id, String title) {
		try {
			bookRepository.updateBookTitle(id, title);
		} catch (SQLException e) {
			throw new RuntimeException("Error in updating book title for book " + id, e);
		}
	}

	public void updateBookAuthor(long id, String author) {
		try {
			bookRepository.updateBookAuthor(id, author);
		} catch (SQLException e) {
			throw new RuntimeException("Error in updating book author for book " + id, e);
		}
	}

	public Book getBookById(long id) {
		try {
			Book book = bookRepository.getBookById(id);

			if (book == null) {
				throw new SQLException("Book with id " + id + " not found.");
			}

			return book;
		} catch (SQLException e) {
			throw new RuntimeException("Error in getting book " + id, e);
		}
	}

	public List<Book> listAllBooks() {
		try {
			List<Book> list = bookRepository.getAllBooks();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException("Error in listing all books", e);
		}
	}

	public boolean isBookAvailable(long id) {
		try {
			Book book = bookRepository.getBookById(id);
			return book.isAvailable();
		} catch (SQLException e) {
			return false;
		}
	}

	////////////////////
	// Member Methdos //
	////////////////////

	public void registerMember(Member member) {
		try {
			memberRepository.saveMember(member);
		} catch (SQLException e) {
			throw new RuntimeException("Error in registering member " + member.getId(), e);
		}
	}

	public void deleteMember(long id) {
		try {
			memberRepository.deleteMember(id);
		} catch (SQLException e) {
			throw new RuntimeException("Error in deleting member " + id, e);
		}
	}

	public Member getMemberById(long id) {
		try {
			Member member = memberRepository.getMemberById(id);

			if (member == null) {
				throw new SQLException("Member by id " + id + " is not found.");
			}

			return member;
		} catch (SQLException e) {
			throw new RuntimeException("Error in getting member " + id, e);
		}
	}

	public List<Member> listAllMembers() {
		try {
			List<Member> list = memberRepository.getAllMembers();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException("Error in listing all members", e);
		}
	}

	public boolean doesMemberExist(long id) {

		try {
			Member member = memberRepository.getMemberById(id);
			return member != null;
		} catch (SQLException e) {
			return false;
		}
	}

	////////////////////
	// Borrow Methdos //
	////////////////////

	public void borrowBook(long memberId, long bookId) {
		try {
			if (!doesMemberExist(memberId)) {
				throw new IllegalArgumentException("Member with id " + memberId + " not found");
			}

			if (!isBookAvailable(bookId)) {
				throw new IllegalStateException("Book with id " + bookId + " is not available");
			}

			connection.setAutoCommit(false);

			borrowRepository.addBorrow(memberId, bookId);
			bookRepository.markBookUnavailable(bookId);

			connection.commit();
		} catch (SQLException e) {

			try {
				connection.rollback();
			}

			catch (SQLException rollbackException) {
				throw new RuntimeException("Error during rollback while borrowing book " + bookId,
						rollbackException);
			}

			throw new RuntimeException(
					"Error occured in the service layer while trying to borrow book with id "
							+ bookId,
					e);

		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new RuntimeException("Error resetting auto-commit", e);
			}
		}
	}

	public void returnBook(long memberId, long bookId) {
		try {
			isBookAvailable(bookId);

			doesMemberExist(memberId);

			connection.setAutoCommit(false);

			borrowRepository.returnBook(memberId, bookId);
			bookRepository.markBookAvailable(bookId);

			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				throw new RuntimeException("Error during rollback while returning book " + bookId,
						rollbackException);
			}
			throw new RuntimeException(
					"Error occured in the service layer while trying to return book with id "
							+ bookId,
					e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new RuntimeException("Error resetting auto-commit", e);
			}
		}
	}
}
