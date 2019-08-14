package com.redhat.ace.controller;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.redhat.ace.model.Index;

@Controller
public class MainController {
	
	private Index index = new Index(1000000, 23, -1.0);
	Random rand = new Random();

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("index", index);
		return "index";
	}
	
	@PostMapping("/")
	public String indexSubmit(@Valid @ModelAttribute Index index, BindingResult validation) {
		
		if (validation.hasErrors()) {
			index.setResult(-1.0);
			return "index";
		}
		
		int iterations = index.getIterations();
		int people = index.getPeople();
		
		double duplicates = 0;
		
		for (int i = 0; i < iterations; i++) {
			Set<Integer> birthdays = new HashSet<Integer>();
			for (int j = 0; j < people; j++) {
				// add method of set returns false if value already exists
				if (!birthdays.add(rand.nextInt(365))) {
					duplicates++;
					break;
				}
			}
		}
		
		double result = (duplicates / iterations) * 100;
		
		index.setResult(result);

		return "index";
	}
}
