package be.kuleuven.researchpad.dao;


import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import be.kuleuven.researchpad.domain.Person;

@Repository
public class PersonDaoImpl extends GenericJpaDaoImpl<Person, Integer> implements
		PersonDao {

	public Person findByEmail(String email) {
		Query q = em.createQuery("FROM User WHERE lower(username) = :username");
		q.setParameter("username", email.trim().toLowerCase());
		return (Person) q.getSingleResult();
	}

}
