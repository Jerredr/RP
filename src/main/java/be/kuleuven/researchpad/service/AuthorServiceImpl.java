package be.kuleuven.researchpad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.kuleuven.researchpad.dao.AuthorDao;
import be.kuleuven.researchpad.domain.Author;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
	
	@Autowired
	private AuthorDao authorDao;
	
	public Author getAuthorById(int authorId) {
		return authorDao.findAuthorById(authorId);
	}

}
