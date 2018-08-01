package pl.jstk.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.constants.ModelConstants;
import pl.jstk.service.impl.BookServiceImpl;
import pl.jstk.to.BookTo;

@Controller
public class BookController {

	@Autowired
	private BookServiceImpl bookService;

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String showListBooks(Model model) {

		List<BookTo> bookList = bookService.findAllBooks();
		model.addAttribute(ModelConstants.bookList, bookList);
		return "books";
	}

	@RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
	public String showDetailsBook(@RequestParam("id") Long id, Model model) {
		BookTo bookById = bookService.findBookById(id);
		model.addAttribute("book", bookById);
		return "book";
	}

	@RolesAllowed("ADMIN")
	@DeleteMapping(value = "books/bookDelete")
	public String deleteBook(@RequestParam("id") Long id, Model model) {
		bookService.deleteBook(id);
		model.addAttribute(ModelConstants.INFO, "You remove book.");
		return showListBooks(model);
	}

	@GetMapping("/books/add")
	public String addBookTemplate(Model model) {
		model.addAttribute("newBook", new BookTo());
		return "addBook";
	}

	@PostMapping("/greeting")
	public String addBook(@ModelAttribute("newBook") BookTo book, Model model) {
		bookService.saveBook(book);
		model.addAttribute(ModelConstants.INFO, "Congratullation! You add new book.");
		return showListBooks(model);
	}

	@GetMapping("/searchbooks")
	public String searchBookTemplate(Model model) {
		model.addAttribute("searchBook", new BookTo());
		return "searchbooks";
	}

	@PostMapping("/searchbooks")
	public String searchBook(@ModelAttribute("searchBook") BookTo book, Model model) {
		List<BookTo> books = bookService.findBookByAuthorOrTitle(book);

		model.addAttribute(ModelConstants.bookList, books);
		return "books";
	}

}
