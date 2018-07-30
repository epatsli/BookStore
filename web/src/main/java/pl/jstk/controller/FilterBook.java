package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.jstk.constants.ModelConstants;
import pl.jstk.service.impl.BookServiceImpl;

@Controller
public class FilterBook {

	@Autowired
	private BookServiceImpl bookService;

	@RequestMapping(value = "/book/s", method = RequestMethod.GET)
	public String showListBook(Model model) {
		model.addAttribute(ModelConstants.INFO, bookService.findAllBooks());
		return "books";
	}
}
