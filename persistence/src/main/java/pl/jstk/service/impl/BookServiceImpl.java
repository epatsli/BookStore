package pl.jstk.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.jstk.entity.BookEntity;
import pl.jstk.mapper.BookMapper;
import pl.jstk.repository.BookRepository;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public List<BookTo> findAllBooks() {
		return BookMapper.map2To(bookRepository.findAll());
	}

	@Override
	public List<BookTo> findBooksByTitle(String title) {
		return BookMapper.map2To(bookRepository.findBookByTitle(title));
	}

	@Override
	public List<BookTo> findBooksByAuthor(String author) {
		return BookMapper.map2To(bookRepository.findBookByAuthor(author));
	}

	@Override
	@Transactional
	public BookTo saveBook(BookTo book) {
		BookEntity entity = BookMapper.map(book);
		entity = bookRepository.save(entity);
		return BookMapper.map(entity);
	}

	@Override
	@Transactional
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);

	}

	public BookTo findBookById(long id) {
		return BookMapper.map(bookRepository.getOne(id));
	}

	public List<BookTo> findBookByAuthorOrTitle(BookTo book) {

		List<BookTo> listBooks = null;

		if ((book.getAuthors() == "") && (book.getTitle() != "")) {
			listBooks = findBooksByTitle(book.getTitle());
		} else if ((book.getTitle() == "") && (book.getAuthors() != "")) {
			listBooks = findBooksByAuthor(book.getAuthors());
		} else if ((book.getTitle() == "") && (book.getAuthors() == "")) {
			listBooks = findAllBooks();
		} else if ((book.getTitle() != "") && (book.getAuthors() != "")) {
			listBooks = findBooksByTitle(book.getTitle());
			listBooks = listBooks.stream().filter(b -> b.getAuthors() == (book.getAuthors()))
					.collect(Collectors.toList());
		}

		return listBooks;
	}
}
