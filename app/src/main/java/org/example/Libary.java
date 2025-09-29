package org.example;

import java.util.ArrayList;

/**
 * Libary
 */
public class Libary {
	ArrayList<Book> listOfBooks;
	ArrayList<Member> listOfMembers;

	public void addBook(Book book) {
		listOfBooks.add(book);
	}

	public void registerMember(Member member) {
		listOfMembers.add(member);
	}

	public Book[] getBookArray() {

		int numberOfBooks = listOfBooks.size();

		Book[] newArray = new Book[numberOfBooks];

		for (int i = 0; i < numberOfBooks; i++) {
			newArray[i] = listOfBooks.get(i);
		}

		return newArray;
	}

	public Member[] getMemberArray() {

		int numberOfMembers = listOfMembers.size();

		Member[] newArray = new Member[numberOfMembers];

		for (int i = 0; i < numberOfMembers; i++) {
			newArray[i] = listOfMembers.get(i);
		}

		return newArray;
	}
}
