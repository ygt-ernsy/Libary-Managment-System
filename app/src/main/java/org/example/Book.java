package org.example;

/**
 * Book
 */
public class Book {
	private long id;
	private int numberOfPages;
	private String title;
	private String author;
	private boolean avaible;

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
		this.avaible = avaible;
	}

	public boolean isAvaible() {
		return avaible;
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
		this.avaible = avaible;
	}
}
