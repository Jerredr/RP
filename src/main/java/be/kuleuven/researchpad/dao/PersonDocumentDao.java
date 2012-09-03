package be.kuleuven.researchpad.dao;

import java.util.Set;

import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.PersonDocument;

public interface PersonDocumentDao extends GenericJpaDao<PersonDocument, Integer>{
	
	public PersonDocument findByDocumentId(int documentId, Person person);
	
	public PersonDocument findByDocumentAndPerson(int documentId, Person p);
	
	public Set<PersonDocument> findDocumentsOfPerson(Person p);

}
