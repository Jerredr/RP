package be.kuleuven.researchpad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import be.kuleuven.researchpad.domain.Document;
import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.PersonDocument;
import be.kuleuven.researchpad.domain.RatingItem;
import be.kuleuven.researchpad.service.DocumentService;
import be.kuleuven.researchpad.service.PersonService;
import be.kuleuven.researchpad.service.SearchService;

@Controller
@RequestMapping(value="/document")
public class DocumentController {
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private SearchService searchService;

	@RequestMapping(value="/view/{documentId}", method=RequestMethod.GET)
	public ModelAndView viewdocument(@PathVariable int documentId) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("documentview");
		//TODO
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		Person person = personService.getPerson(1);
		PersonDocument document = documentService.readPersonDocumentByDocId(documentId, person);
		if(document.getDocument() != null && document.getDocument().getContent().equals("(To access the full article, please see PDF)")) {
			document.getDocument().setType(0);
		}
		mav.addObject("pd", document);
		return mav;
	}
	
	@RequestMapping(value="/{documentId}", method=RequestMethod.GET)
	public ModelAndView document(@PathVariable int documentId) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("document");
		Document document = documentService.getDocument(documentId);
		if(document != null) {
			if(!document.isComplete()) {
				document = searchService.completeDocument(document);
				if(document != null) {
					mav.addObject("document", document);
					mav.addObject("found", true);
				} else {
					mav.addObject("found", false);
				}
			} else {
				mav.addObject("document", document);
				mav.addObject("found", true);
			}
		} else {
			mav.addObject("found", false);
		}
		return mav;
	}
	
	@RequestMapping(value="/postnotes", method=RequestMethod.POST)
	public @ResponseBody String notes(@RequestParam(value="notes", required=true) String notes, @RequestParam(value="pdId", required=true) Integer pdId) {
		PersonDocument pd = documentService.getPersonDocumentById(pdId);
		pd.setNotes(notes.trim());
		documentService.updatePersonDocument(pd);
		return notes;
	}
	
	@RequestMapping(value="/link", method=RequestMethod.POST)
	public @ResponseBody void link(@RequestParam(value="documentId", required=true) String documentId) {
		//TODO
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		Person person = personService.getPerson(1);
		
		documentService.linkDocumentPerson(person, Integer.parseInt(documentId));
	}
	
	@RequestMapping(value="/addrating", method=RequestMethod.POST)
	public @ResponseBody String addrating(@RequestParam(value="pdId", required=true) Integer pdId, @RequestParam(value="ratingItem", required=true) String ratingItem) {
		PersonDocument pd = documentService.getPersonDocumentById(pdId);
		RatingItem item = new RatingItem();
		item.setContent(ratingItem.trim());
		Integer newRatingId = documentService.addRatingItem(item, pd);
		return newRatingId.toString();
	}
	
	@RequestMapping(value="/removerating", method=RequestMethod.POST)
	public @ResponseBody void removerating(@RequestParam(value="itemId", required=true) String itemId) {
		documentService.deleteRatingItem(Integer.parseInt(itemId));
	}
	
	@RequestMapping(value="/submitrating", method=RequestMethod.POST)
	public @ResponseBody String submitrating(@RequestParam(value="pdId", required=true) Integer pdId,  @RequestParam(value="rating", required=true) Integer rating) {
		PersonDocument pd = documentService.getPersonDocumentById(pdId);
		pd.setRating(rating);
		documentService.updatePersonDocument(pd);
		return "OK";
	}
	
	@RequestMapping(value="/saveratingitem", method=RequestMethod.POST)
	public @ResponseBody String saveratingitem(@RequestParam(value="ratingItemId", required=true) Integer ratingItemId,  @RequestParam(value="rating", required=true) String rating) {
		documentService.saveRatingItem(ratingItemId, rating);
		return "OK";
	}
	
	@RequestMapping(value="/doi/{doi1}/{doi2}", method=RequestMethod.GET)
	public ModelAndView retrievedoi(@PathVariable String doi1, @PathVariable String doi2) {
		String doi = doi1 + "/" + doi2;
		doi = doi.replaceAll("%22", "/");
		// check whether the document isn't already in the system
		int docId = documentService.getDocIdOfDoi(doi);
		if(docId < 1) {
			// retrieve the document
			docId = searchService.importByDoi(doi);
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView("/document/view/" + docId, true, true, true));
		return mav;
	}
}