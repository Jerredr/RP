package be.kuleuven.researchpad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.kuleuven.researchpad.dao.PersonDao;
import be.kuleuven.researchpad.domain.Person;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	private PersonDao personDao;
	
	public void register(Person person) {
		personDao.create(person);
		
	}

	public Person getPerson(int id) {
		return personDao.findById(id);
	} 
	
}