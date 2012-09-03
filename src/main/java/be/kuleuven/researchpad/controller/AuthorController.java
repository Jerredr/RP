package be.kuleuven.researchpad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import be.kuleuven.researchpad.domain.Author;
import be.kuleuven.researchpad.domain.Document;
import be.kuleuven.researchpad.service.AuthorService;
import be.kuleuven.researchpad.service.SearchService;

@Controller
@RequestMapping(value="/author")
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private SearchService searchService;

	@RequestMapping(value="/{authorId}", method=RequestMethod.GET)
	public ModelAndView author(@PathVariable int authorId) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("author");
		Author author = authorService.getAuthorById(authorId);
		mav.addObject("author", author);
		List<Document> foundDocs = searchService.searchAuthorDocs(author.getFirstName(), author.getLastName());
		mav.addObject("foundDocs", foundDocs);
		return mav;
	}
}