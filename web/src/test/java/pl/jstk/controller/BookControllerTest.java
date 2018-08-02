package pl.jstk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class BookControllerTest {

	private MockMvc mockMvc;

	@Mock
	BookService bookService;

	@InjectMocks
	BookController bookController;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).build();
		MockitoAnnotations.initMocks(bookService);
		Mockito.reset(bookService);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		// ReflectionTestUtils.setField(bookController, "bookService",
		// bookService);
	}

	/*
	 * @Test public void shouldReturnBookFromListBooks() throws Exception {
	 * mockMvc.perform(MockMvcRequestBuilders.get("/books/book/3").accept(
	 * MediaType.APPLICATION_JSON)) .andExpect(jsonPath("$.id").exists())
	 * .andExpect(jsonPath("$.title").exists())
	 * .andExpect(jsonPath("$.authors").exists())
	 * .andExpect(jsonPath("$.id").value(3))
	 * .andExpect(jsonPath("$.title").value("Third book"))
	 * .andExpect(jsonPath("$.authors").value("Janusz Jankowski"
	 * )).andDo(print()); }
	 */
	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void shouldShowBooks() throws Exception {

		// given
		List<BookTo> books = new ArrayList<>();
		books.add(new BookTo());
		books.add(new BookTo());

		// when
		Mockito.when(bookService.findAllBooks()).thenReturn(books);
		ResultActions resultActions = mockMvc.perform(get("/books"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.BOOKS));

	}
}
