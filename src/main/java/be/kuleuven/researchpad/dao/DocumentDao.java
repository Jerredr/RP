package be.kuleuven.researchpad.dao;

import java.util.Set;

import be.kuleuven.researchpad.domain.Author;
import be.kuleuven.researchpad.domain.Document;
import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.PersonDocument;

public interface DocumentDao extends GenericJpaDao<Document, Integer>{
	
	public Set<PersonDocument> findRecentDocumentsOfPerson(Person p);
	
	public Set<Author> findAuthorsOfDocumentsOfPerson(Person p);
	
	public Set<String> findPublicationYearsOfPerson(Person p);
	
	public Set<PersonDocument> findDocumentsByAuthorOfPerson(Person person, int authorId);
	
	public Set<PersonDocument> findDocumentsByYearOfPerson(Person p, String year);
	
	public Document findDocumentByTitle(String title);

	public int getDocIdOfDoi(String doi);
}
