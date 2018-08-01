package pl.jstk.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.jstk.service.impl.BookServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BookControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private BookController bookController;

	@Spy
	private BookServiceImpl bookService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).build();
		MockitoAnnotations.initMocks(bookService);
		Mockito.reset(bookService);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
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

}
