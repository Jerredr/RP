package be.kuleuven.researchpad.dao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.PersonDocument;

@Repository
public class PersonDocumentDaoImpl extends
		GenericJpaDaoImpl<PersonDocument, Integer> implements PersonDocumentDao {

	public PersonDocument findByDocumentId(int documentId, Person person) {
		TypedQuery<PersonDocument> q = em
				.createQuery(
						"SELECT pd FROM PersonDocument pd WHERE pd.person = :person AND pd.document.id = :documentId",
						PersonDocument.class);
		q.setParameter("person", person);
		q.setParameter("documentId", documentId);
		try {
			return q.getSingleResult();
		} catch(Exception e) {
			return null;
		}
	}

	public PersonDocument findByDocumentAndPerson(int documentId, Person person) {
		TypedQuery<PersonDocument> q = em
				.createQuery(
						"SELECT pd FROM PersonDocument pd WHERE pd.person = :person AND pd.document.id = :documentId",
						PersonDocument.class);
		q.setParameter("person", person);
		q.setParameter("documentId", documentId);
		
		PersonDocument pd = null;
		// if no result: an exception is thrown (strange API behavior ...)
		try {
			pd = q.getSingleResult();
		} catch(Exception e) {
			// nothing
		}
		return pd;
	}
	
	public Set<PersonDocument> findDocumentsOfPerson(Person person) {
		TypedQuery<PersonDocument> q = em
				.createQuery(
						"SELECT pd FROM PersonDocument pd WHERE pd.person = :person",
						PersonDocument.class);
		q.setParameter("person", person);
		
		return new HashSet<PersonDocument>(q.getResultList());
	}

}
