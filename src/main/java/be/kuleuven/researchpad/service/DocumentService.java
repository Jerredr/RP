package be.kuleuven.researchpad.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.kuleuven.researchpad.domain.Author;
import be.kuleuven.researchpad.domain.Document;
import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.PersonDocument;
import be.kuleuven.researchpad.domain.RatingItem;

@Service
@Transactional
public interface DocumentService {
	
	public Document getDocument(int documentId);
	
	public PersonDocument getPersonDocumentByDocId(int documentId, Person person);
	
	public Set<PersonDocument> getRecentDocumentsOfPerson(Person person);
	
	public Set<Author> findAuthorsOfDocumentsOfPerson(Person p);
	
	public Set<String> findPublicationYearsOfPerson(Person p);
	
	public void updatePersonDocument(PersonDocument pd);
	
	public PersonDocument getPersonDocumentById(int pdId);
	
	public void linkDocumentPerson(Person p, int docId);
	
	public int addRatingItem(RatingItem ri, PersonDocument pd);
	
	public void deleteRatingItem(int ri);
	
	public RatingItem getRatingItemById(int ratingItemId);
	
	public void saveRatingItem(int ratingItemId, String rating);
	
	public Set<PersonDocument> getDocumentsByYearOfPerson(String year, Person person);
	
	public Set<PersonDocument> getDocumentsByAuthorOfPerson(Person p, int authorId);
	
	public Set<PersonDocument> getDocumentsOfPerson(Person p);
	
	public PersonDocument readPersonDocumentByDocId(int documentId, Person person);
	
	public int getDocIdOfDoi(String doi);
}

