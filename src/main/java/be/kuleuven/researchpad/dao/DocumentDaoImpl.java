package be.kuleuven.researchpad.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import be.kuleuven.researchpad.domain.Author;
import be.kuleuven.researchpad.domain.Document;
import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.PersonDocument;

@Repository
public class DocumentDaoImpl extends GenericJpaDaoImpl<Document, Integer> implements DocumentDao {

	@SuppressWarnings("unchecked")
	public Set<PersonDocument> findRecentDocumentsOfPerson(Person person) {
		Query q = em.createQuery("FROM PersonDocument d WHERE d.person = :person ORDER BY d.recentDate DESC LIMIT 0, 15");
		q.setParameter("person", person);
		return new HashSet<PersonDocument>(q.getResultList());
	}

	public Set<Author> findAuthorsOfDocumentsOfPerson(Person person) {
		TypedQuery<Author> q = em.createQuery("SELECT a FROM Author a LEFT JOIN a.documents d LEFT JOIN d.personDocuments pd WHERE pd.person = :person ORDER BY 'a.lastName' ASC", Author.class);
		q.setParameter("person", person);
		return new HashSet<Author>(q.getResultList());
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> findPublicationYearsOfPerson(Person person) {
		Query q = em.createQuery("SELECT d.publicationYear FROM Document d LEFT JOIN d.personDocuments pd WHERE pd.person = :person group by d.publicationYear");
		q.setParameter("person", person);
		return new HashSet<String>(q.getResultList());
	}
	
	public Set<PersonDocument> findDocumentsByYearOfPerson(Person person, String year) {
		TypedQuery<PersonDocument> q = em.createQuery("SELECT pd FROM PersonDocument pd LEFT JOIN pd.document d WHERE d.publicationYear = :year AND pd.person = :person", PersonDocument.class);
		q.setParameter("person", person);
		q.setParameter("year", year);
		return new HashSet<PersonDocument>(q.getResultList());
	}

	public Set<PersonDocument> findDocumentsByAuthorOfPerson(Person person, int authorId) {
		TypedQuery<PersonDocument> q = em.createQuery("SELECT pd FROM Author a, PersonDocument pd LEFT JOIN a.documents d LEFT JOIN pd.document pdd WHERE a.id = :authorId AND pd.person = :person AND pdd.id = d.id", PersonDocument.class);
		q.setParameter("person", person);
		q.setParameter("authorId", authorId);
		return new HashSet<PersonDocument>(q.getResultList());
	}
	
	public Document findDocumentByTitle(String title) {
		TypedQuery<Document> q = em.createQuery("SELECT d FROM Document d WHERE LOWER(d.name) = LOWER(:title)", Document.class);
		q.setParameter("title", title.trim());
		List<Document> results = q.getResultList();
        if (results.isEmpty()) return null;
        else return results.get(0);
	}
	
	public int getDocIdOfDoi(String doi) {
		TypedQuery<Document> q = em.createQuery("SELECT d FROM Document d WHERE LOWER(d.doi) LIKE LOWER(:doi)", Document.class);
		q.setParameter("doi", doi.trim());
		// getSingleResult throws exception if no result
		try {
			Document result = q.getSingleResult();
			return result.getId();
		} catch(Exception ex) {
			return 0;
		}
	}
}
