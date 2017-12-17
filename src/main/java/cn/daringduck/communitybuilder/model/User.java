package cn.daringduck.communitybuilder.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User in the System Uses Hibernate to load/store the data
 * 
 * @author Jochem
 */
@Entity
@Table(name = "users")
//@JsonIgnoreProperties({"friends", "classes", "password"})
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class User {
	
	/* TODO: User should have
	 *  - username = email
	 *  - name in latin characters
	 *  - name in chinese "
	 */
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@NotNull private String username;
	@NotNull private String password;
	private boolean gender;
	private String nickname;
	private String phone;
	private String wechat;
	private String email;
	private boolean disabled;
	@NotNull @OneToOne private Role role;
	@OneToOne private Picture picture;
	@OneToOne private Club club;

	@ManyToMany
	@JoinTable(name = "friends", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "friend_id"))
	private List<User> friends;
	
	@ManyToMany(mappedBy="members") 
	private List<Class> classes;
	
	// Constructors

	public User() {}

	public User(String username, String password, boolean gender, String nickname, String phone, String wechat,
			String email, Role role, Picture picture, Club club, List<User> friends) {
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.nickname = nickname;
		this.phone = phone;
		this.wechat = wechat;
		this.email = email;
		this.role = role;
		this.picture = picture;
		this.club = club;
		this.friends = friends;
	}

	// Getters & Setters

	public long getId() { return id; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public boolean isGender() { return gender; }
	public void setGender(boolean gender) { this.gender = gender; }

	public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; }

	public String getNickname() { return nickname; }
	public void setNickname(String nickname) { this.nickname = nickname; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getWechat() { return wechat; }
	public void setWechat(String wechat) { this.wechat = wechat; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	public boolean isDisabled() { return disabled; }
	public void setDisabled(boolean disabled) { this.disabled = disabled; }

	public Picture getPicture() { return picture; }
	public void setPicture(Picture picture) { this.picture = picture; }

	public Club getClub() { return club; }
	public void setClub(Club club) { this.club = club; }
	
	public List<User> getFriends() { return friends; }
	public void addFriend(User friend) { friends.add(friend); }
	
	public List<Class> getClasses() { return classes; }
	public void setClasses(List<Class> classes) { this.classes = classes;}
	public void addClass(Class newClass) { classes.add(newClass); }

	@Override
	public boolean equals(Object other) {
		if (other instanceof User) {
			User otherUser = (User) other;
			return otherUser.id == id;
		}		
		return false;
	}
	
}