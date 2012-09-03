package be.kuleuven.researchpad.service;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.kuleuven.researchpad.dao.PersonDao;
import be.kuleuven.researchpad.domain.Person;
import be.kuleuven.researchpad.domain.UserDetailsImpl;

@Service
@Transactional
//@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PersonDao personDao;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		Person person = personDao.findByEmail(username);
		if (person == null) {
			throw new UsernameNotFoundException("No such user: " + username);
		}

		Collection<? extends GrantedAuthority> authorities = person.getAuthorities();
		Date lastLoginDate = new Date();

		return new UserDetailsImpl(person, authorities, lastLoginDate);
	}
}