package pl.jstk.service;

import java.util.List;

import pl.jstk.to.BookTo;

public interface BookService {

	List<BookTo> findAllBooks();

	List<BookTo> findBooksByTitle(String title);

	List<BookTo> findBooksByAuthor(String author);

	BookTo saveBook(BookTo book);

	void deleteBook(Long id);

	BookTo findBookById(long id);

	List<BookTo> findBookByAuthorOrTitle(BookTo book);
}
