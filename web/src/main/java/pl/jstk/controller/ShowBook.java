package pl.jstk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.jstk.constants.ModelConstants;
import pl.jstk.service.impl.BookServiceImpl;
import pl.jstk.to.BookTo;

@Controller
public class ShowBook {

	@Autowired
	private BookServiceImpl bookService;

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String showListBooks(Model model) {

		List<BookTo> bookList = bookService.findAllBooks();
		model.addAttribute(ModelConstants.bookList, bookList);
		return "books";
	}

	@RequestMapping(value = "/book/gfddfv", method = RequestMethod.GET)
	public String showDetailsForBook(Model model, @PathVariable("id") long id) {

		BookTo bookById = bookService.findBookById(id);
		model.addAttribute(ModelConstants.bookid, bookById);
		return "book";
	}

}
