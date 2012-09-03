package be.kuleuven.researchpad.service;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.kuleuven.researchpad.dao.DocumentDao;
import be.kuleuven.researchpad.dao.PersonDocumentDao;
import be.kuleuven.researchpad.dao.RatingItemDao;
import be.kuleuven.researchpad.domain.Author;
import be.kuleuven.researchpad.domain.Document;
import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.PersonDocument;
import be.kuleuven.researchpad.domain.RatingItem;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {
	
	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private PersonDocumentDao personDocumentDao;
	
	@Autowired
	private RatingItemDao ratingItemDao;
	
	public Document getDocument(int documentId) {
		return documentDao.findById(documentId);
	}

	public Set<PersonDocument> getRecentDocumentsOfPerson(Person person) {
		return documentDao.findRecentDocumentsOfPerson(person);
	}
	
	public Set<Author> findAuthorsOfDocumentsOfPerson(Person p) {
		return documentDao.findAuthorsOfDocumentsOfPerson(p);
	}
	
	public Set<String> findPublicationYearsOfPerson(Person p) {
		return documentDao.findPublicationYearsOfPerson(p);
	}

	public void updatePersonDocument(PersonDocument pd) {
		personDocumentDao.update(pd);
	}

	public PersonDocument getPersonDocumentById(int pdId) {
		return personDocumentDao.findById(pdId);
		
	}

	public PersonDocument getPersonDocumentByDocId(int documentId, Person person) {
		PersonDocument pd = personDocumentDao.findByDocumentId(documentId, person);
		// if not link, link it
		if(pd == null) {
			pd = this.linkDocPerson(person, documentDao.findById(documentId));
		}
		return pd;
	}

	public void linkDocumentPerson(Person p, int docId) {
		// check whether the link doesn't already exist
		PersonDocument pd = personDocumentDao.findByDocumentAndPerson(docId, p);
		
		if(pd == null) {
			this.linkDocPerson(p, documentDao.findById(docId));
		}
	}
	
	private PersonDocument linkDocPerson(Person p, Document d) {
		PersonDocument pd = new PersonDocument();
		pd.setDocument(d);
		pd.setPerson(p);
		Date currentDate = new Date();
		pd.setImportDate(currentDate);
		pd.setRecentDate(currentDate);
		personDocumentDao.create(pd);
		
		return pd;
	}

	public int addRatingItem(RatingItem ri, PersonDocument pd) {
		ri.setPersonDocument(pd);
		ratingItemDao.create(ri);
		pd.addRatingItem(ri);
		return ri.getId();
	}

	public void deleteRatingItem(int ri) {
		RatingItem item = ratingItemDao.findById(ri);
		PersonDocument pd = item.getPersonDocument();
		pd.removeRatingItem(item);
		personDocumentDao.update(pd);
		ratingItemDao.deleteById(ri);
	}

	public RatingItem getRatingItemById(int ratingItemId) {
		return ratingItemDao.findById(ratingItemId);
	}

	public void saveRatingItem(int ratingItemId, String rating) {
		RatingItem item = ratingItemDao.findById(ratingItemId);
		item.setContent(rating);
		ratingItemDao.update(item);
	}

	public Set<PersonDocument> getDocumentsByYearOfPerson(String year,
			Person person) {
		return documentDao.findDocumentsByYearOfPerson(person, year);
	}
	
	public Set<PersonDocument> getDocumentsByAuthorOfPerson(Person p, int authorId) {
		return documentDao.findDocumentsByAuthorOfPerson(p, authorId);
	}
	
	public Set<PersonDocument> getDocumentsOfPerson(Person p) {
		return personDocumentDao.findDocumentsOfPerson(p);
	}
	
	public PersonDocument readPersonDocumentByDocId(int documentId, Person person) {
		PersonDocument doc = this.getPersonDocumentByDocId(documentId, person);
		
		// update last accessed date of the personal document
		doc.setRecentDate(new Date());
		personDocumentDao.update(doc);
		
		return doc;
	}
	
	public int getDocIdOfDoi(String doi) {
		return documentDao.getDocIdOfDoi(doi);
	}

}
