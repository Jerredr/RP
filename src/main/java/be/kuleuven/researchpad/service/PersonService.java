package be.kuleuven.researchpad.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.kuleuven.researchpad.domain.Person;

@Service
@Transactional
public interface PersonService {
	
	public void register(Person person);
	
	public Person getPerson(int id);
	
}
