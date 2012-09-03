package be.kuleuven.researchpad.dao;

import be.kuleuven.researchpad.domain.Person;

public interface PersonDao extends GenericJpaDao<Person, Integer>{
	
	public Person findByEmail(String email);

}

