package be.kuleuven.researchpad.domain;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

public class UserDetailsImpl extends Person {

	private static final long serialVersionUID = -6501660339421001173L;

	private Person person;
	private Date lastLoginDate;

	private static boolean accountNonExpired = true;
	private static boolean credentialsNonExpired = true;
	private static boolean accountNonLocked = true;

	public UserDetailsImpl(Person person, Collection<? extends GrantedAuthority> authorities,
			Date lastLoginDate) throws IllegalArgumentException {
		/*super(person.getEmail(), person.getPassword(), person.isEnabled(),
				accountNonExpired, credentialsNonExpired, accountNonLocked,
				authorities);*/
		this.person = person;
		this.lastLoginDate = lastLoginDate;
	}

	public int getId() {
		return person.getId();
	}

	public String getPseudo() {
		return person.getEmail();
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}
}