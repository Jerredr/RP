package be.kuleuven.researchpad.dao;

import be.kuleuven.researchpad.domain.Author;

public interface AuthorDao extends GenericJpaDao<Author, Integer> {
	
	public Author findAuthorById(int authorId);
	
	public Author findAuthorByName(String name, String firstName);

}
