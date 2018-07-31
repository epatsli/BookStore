package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jstk.constants.ModelConstants;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller
@ResponseBody
public class DetailsBook {

	@Autowired
	private BookService bookService;

	@RequestMapping(value = "books/book")
	public BookTo showDetailsBook(@RequestParam("id") long id, Model model) {
		BookTo bookById = bookService.findBookById(id);
		model.addAttribute(ModelConstants.bookid, bookById);
		return bookById;
	}
}
