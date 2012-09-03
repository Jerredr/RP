package be.kuleuven.researchpad.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.kuleuven.researchpad.domain.Author;

@Service
@Transactional
public interface AuthorService {
	
	public Author getAuthorById(int authorId);

}
