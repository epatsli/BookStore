package pl.jstk.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).build();

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
