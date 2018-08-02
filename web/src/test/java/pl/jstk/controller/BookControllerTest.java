package pl.jstk.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.jstk.config.WebConfig;
import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.enumerations.BookStatus;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class BookControllerTest {

	@Mock
	BookService bookService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	BookController bookController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).build();
		MockitoAnnotations.initMocks(bookService);
		Mockito.reset(bookService);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void shouldAddBook() throws Exception {
		// given
		long bookId = 1L;
		String title = "title";
		String authors = "authors";
		BookStatus status = BookStatus.FREE;
		BookTo book = new BookTo();
		book.setId(bookId);
		book.setTitle(title);
		book.setAuthors(authors);
		book.setStatus(status);
		Mockito.when(bookService.saveBook(book)).thenReturn(book);

		// when
		ResultActions resultActions = mockMvc.perform(post("/greeting"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.WELCOME)).andDo(print())
				.andExpect(model().attribute(ModelConstants.INFO, "Book added."));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void shouldShowBook() throws Exception {
		// given
		long bookId = 1L;
		String title = "title";
		String authors = "authors";
		BookStatus status = BookStatus.FREE;
		BookTo book = new BookTo();
		book.setId(bookId);
		book.setTitle(title);
		book.setAuthors(authors);
		book.setStatus(status);
		Mockito.when(bookService.findBookById(bookId)).thenReturn(book);

		// when
		ResultActions resultActions = mockMvc.perform(get("/books/book?id=1"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name("book")).andDo(print())
				.andExpect(model().attribute("book", hasProperty("id", is(bookId))))
				.andExpect(model().attribute("book", hasProperty("title", is(title))))
				.andExpect(model().attribute("book", hasProperty("authors", is(authors))))
				.andExpect(model().attribute("book", hasProperty("status", is(status))));

		verify(bookService, times(1)).findBookById(Mockito.anyLong());
		verifyNoMoreInteractions(bookService);
	}

	/*
	 * @Test
	 * 
	 * @WithMockUser(username = "admin", roles = { "ADMIN" }) public void
	 * shouldShowBooks() throws Exception {
	 * 
	 * // given List<BookTo> books = new ArrayList<>(); books.add(new BookTo());
	 * books.add(new BookTo());
	 * 
	 * // when Mockito.when(bookService.findAllBooks()).thenReturn(books);
	 * ResultActions resultActions = mockMvc.perform(get("/books"));
	 * 
	 * // then
	 * resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.
	 * BOOKS)); }
	 */
	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
