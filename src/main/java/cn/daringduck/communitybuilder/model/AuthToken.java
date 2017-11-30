package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Authentication Token used to retrieve data that can
 * only be accessed when logged in
 * Uses Hibernate to load/store the data
 * 
 * @author Jochem
 */
@Entity
@Table(name = "auth_tokens")
public class AuthToken {

	@Id
	@NotNull private String token;
	@NotNull @OneToOne private User user;

	public AuthToken() {}
	
	public AuthToken(String token, User user) {
		this.token = token;
		this.user = user;
	}

	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }

	public User getUser() { return user; }

}