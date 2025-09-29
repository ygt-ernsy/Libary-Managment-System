package org.example;

import java.util.ArrayList;

/**
 * Member
 */
public class Member {

	private long id;
	private String name;
	private ArrayList<Book> borrowedBooks;

	public Member() {
		borrowedBooks = new ArrayList<Book>();
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
