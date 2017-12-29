package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "friends")
public class Friends {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@OneToOne private User user;
	
	@OneToOne private User friends;
	
	//whether the friends is allowed to see my moments
	private boolean allowToSeeMyMoments;
	
	public Friends() {}

	public Friends(User user, User friends,boolean allowToSeeMyMoments) {
		
		this.user = user;
		
		this.friends = friends;
		
		this.allowToSeeMyMoments = allowToSeeMyMoments;
		
	}
	
	public long getId() {
		return this.id;
	}
	
	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public User getFriends() {
		return friends;
	}


	public void setFriends(User friends) {
		this.friends = friends;
	}


	public boolean isAllowToSeeMyMoments() {
		return allowToSeeMyMoments;
	}


	public void setAllowToSeeMyMoments(boolean allowToSeeMyMoments) {
		this.allowToSeeMyMoments = allowToSeeMyMoments;
	} 
}
