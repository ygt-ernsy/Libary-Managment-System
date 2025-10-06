package org.example.model;

/**
 * Book
 */
public class Book {
	private long id;
	private int numberOfPages;
	private String title;
	private String author;
	private boolean available;

	public Book() {
	}

	public Book(
			long id,
			String title,
			String author,
			boolean avaible) {

		this.id = id;
		this.title = title;
		this.author = author;
		this.available = avaible;
	}

	public boolean isAvailable() {
		return available;
	}

	public String getTitle() {
		return title;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAvaible(boolean avaible) {
		this.available = avaible;
	}
}
