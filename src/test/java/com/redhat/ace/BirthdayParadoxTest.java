package com.redhat.ace;

import static org.hamcrest.Matchers.containsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.redhat.ace.controller.MainController;
import com.redhat.ace.model.Index;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BirthdayParadoxTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MainController controller;

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
	
	@Test
	public void reachHomepage() throws Exception {
		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("Birthday Paradox")));
	}
}
