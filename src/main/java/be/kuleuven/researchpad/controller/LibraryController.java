package be.kuleuven.researchpad.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import be.kuleuven.researchpad.domain.Author;
import be.kuleuven.researchpad.domain.Document;
import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.PersonDocument;
import be.kuleuven.researchpad.service.DocumentService;
import be.kuleuven.researchpad.service.PersonService;
import be.kuleuven.researchpad.service.SearchService;

@Controller
@RequestMapping(value="/library")
public class LibraryController {
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public ModelAndView home() {
		//searchService.search("Designing and conducting tabletop exercises to assess public health preparedness for manmade and naturally occurring biological threats");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("library");
		
		//TODO userId
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		Person person = personService.getPerson(1);
		Set<PersonDocument> personDocuments = documentService.getRecentDocumentsOfPerson(person);
		mav.addObject("documents", personDocuments);
		
		// get authors
		Set<Author> authors = documentService.findAuthorsOfDocumentsOfPerson(person);
		/*for(Author a : authors) {
			System.out.println(a.getLastName());
		}*/
		mav.addObject("authors", authors);
		
		// get publication years
		Set<String> years = documentService.findPublicationYearsOfPerson(person);
		mav.addObject("years", years);
				
		return mav;
	}
	
	@RequestMapping(value="/year/{year}", method=RequestMethod.GET)
	public ModelAndView year(@PathVariable String year) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("documentlist");
		
		//TODO userId
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		Person person = personService.getPerson(1);
		Set<PersonDocument> documents = documentService.getDocumentsByYearOfPerson(year, person);
		mav.addObject("documents", documents);
				
		return mav;
	}
	
	@RequestMapping(value="/author/{id}", method=RequestMethod.GET)
	public ModelAndView author(@PathVariable String id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("documentlist");
		
		//TODO userId
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		Person person = personService.getPerson(1);
		Set<PersonDocument> documents = documentService.getDocumentsByAuthorOfPerson(person, Integer.parseInt(id));
		mav.addObject("documents", documents);
				
		return mav;
	}
	
	@RequestMapping(value="/all", method=RequestMethod.GET)
	public ModelAndView all() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("documentlist");
		
		//TODO userId
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		Person person = personService.getPerson(1);
		Set<PersonDocument> documents = documentService.getDocumentsOfPerson(person);
		mav.addObject("documents", documents);
				
		return mav;
	}
	
	@RequestMapping(value="/recent", method=RequestMethod.GET)
	public ModelAndView recent() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("documentlist");
		
		//TODO userId
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		Person person = personService.getPerson(1);
		Set<PersonDocument> personDocuments = documentService.getRecentDocumentsOfPerson(person);
		mav.addObject("documents", personDocuments);
				
		return mav;
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public ModelAndView search(@RequestParam(value="title", required=false) String title) {
		//System.out.println(title);
		ModelAndView mav = new ModelAndView();
		if(title != null && !title.trim().equals("")) {
			List<Document> results = searchService.searchResults(title);
			mav.addObject("results", results);
			mav.addObject("found", true);
			mav.setViewName("search");
		} else {
			mav.addObject("found", false);
			mav.setViewName("search");
		}
				
		return mav;
	}
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public ModelAndView search2(@RequestParam(value="title", required=false) String title) {
		//System.out.println(title);
		ModelAndView mav = new ModelAndView();
		if(title != null && !title.trim().equals("")) {
			mav.addObject("results", searchService.searchResults(title));
			mav.addObject("found", true);
		} else {
			mav.addObject("found", false);
		}
		mav.setViewName("search");
				
		return mav;
	}
	
	@RequestMapping(value="/gettype", method=RequestMethod.POST)
	public @ResponseBody String gettype(@RequestParam(value="itemId", required=true) String itemId) {
        //System.out.println(itemId);
		return searchService.getDocumentType(itemId) + "";
	}

}
