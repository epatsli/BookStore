package pl.jstk.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration

public class LoginControllerTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private UserDetailsService userDetailsService;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
	}

	@Test
	public void requiresAuthentication() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
		mvc.perform(get("/books/add")).andExpect(status().isMovedTemporarily());
	}

	@Test
	public void requestProtectedUrlWithUser() throws Exception {
		mvc.perform(get("/greeting").with(user("user"))).andExpect(status().isNotFound())
				.andExpect(authenticated().withUsername("user"));
	}

	@Test
	public void requestProtectedUrlWithAdmin() throws Exception {
		mvc.perform(get("/searchbooks")
				.with(user("admin").password(passwordEncoder().encode("admin")).roles("USER", "ADMIN")))
				.andExpect(status().isNotFound()).andExpect(authenticated().withUsername("admin"));
	}

	@Test(expected = UsernameNotFoundException.class)
	public void requestErrorWhenProtectedUrlWithUserDetails() throws Exception {
		userDetailsService.loadUserByUsername("inncorrect");
	}

	@Test
	public void requestProtectedUrlWithUserDetails() throws Exception {
		UserDetails userDetails = userDetailsService.loadUserByUsername("user");
		mvc.perform(get("/books/add").with(user(userDetails))).andExpect(status().isNotFound())
				.andExpect(authenticated().withAuthenticationPrincipal(userDetails));
	}

	@Test
	public void requestProtectedUrlWithAuthentication() throws Exception {
		Authentication authentication = new TestingAuthenticationToken("user", "notused", "ROLE_USER");
		mvc.perform(get("/searchbooks").with(authentication(authentication))).andExpect(status().isNotFound())
				.andExpect(authenticated().withAuthentication(authentication));
	}

	@Configuration
	@EnableWebMvcSecurity
	@EnableWebMvc
	static class Config extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated().and()
					.formLogin();
		}

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
		}

		@Override
		@Bean
		public UserDetailsService userDetailsServiceBean() throws Exception {
			return super.userDetailsServiceBean();
		}
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
