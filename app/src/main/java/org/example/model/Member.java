package org.example.model;

import java.util.ArrayList;

/**
 * Member
 */
public class Member {

	private long id;
	private String name;
	private ArrayList<Book> borrowedBooks;

	public Member() {
		this(0, null, null);
	}

	public Member(long id) {
		this(id, null, null);
	}

	public Member(long id, String name) {
		this(id, name, null);
	}

	public Member(long id, String name, ArrayList<Book> bookList) {
		this.id = id;
		this.name = name;
		this.borrowedBooks = bookList;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public void viewBooks() {
		for (Book book : borrowedBooks) {

			long id = book.getId();
			String title = book.getTitle();
			String author = book.getAuthor();

			System.out.println(id + " " + title + " " + author);
		}
	}

	public void borrowBook(Book book) {
		borrowedBooks.add(book);
	}

	public void returnBook(Book book) {
		if (borrowedBooks.contains(book)) {
			int indexOf = borrowedBooks.indexOf(book);
			borrowedBooks.remove(indexOf);
		}
	}
}
