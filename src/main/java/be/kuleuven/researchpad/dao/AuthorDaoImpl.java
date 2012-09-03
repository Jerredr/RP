package be.kuleuven.researchpad.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import be.kuleuven.researchpad.domain.Author;

@Repository
public class AuthorDaoImpl extends GenericJpaDaoImpl<Author, Integer> implements AuthorDao {
	
	public Author findAuthorById(int authorId) {
		TypedQuery<Author> q = em.createQuery("SELECT DISTINCT author FROM Author author LEFT JOIN author.documents d LEFT JOIN d.authors authors WHERE author.id = :authorId", Author.class);
		q.setParameter("authorId", authorId);
		return q.getSingleResult();
	}
	
	public Author findAuthorByName(String name, String firstName) {
		TypedQuery<Author> q =  em.createQuery("SELECT DISTINCT author FROM Author author WHERE LOWER(author.lastName) = LOWER(:authorName) AND LOWER(author.firstName) = LOWER(:authorFirstName)", Author.class);
		q.setParameter("authorName", name);
		q.setParameter("authorFirstName", firstName);
		List<Author> results = q.getResultList();
        if (results.isEmpty()) return null;
        else return results.get(0);
	}

}
