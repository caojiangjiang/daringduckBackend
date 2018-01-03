package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "friends")
public class Friends {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull @OneToOne private User user;
	
	@NotNull @OneToOne private User friend;
	
	//whether the friends is allowed to see my moments
	private boolean allowToSeeMyMoments;
	
	public Friends() {}

	public Friends(User user, User friend,boolean allowToSeeMyMoments) {
		
		this.user = user;
		
		this.friend = friend;
		
		this.allowToSeeMyMoments = allowToSeeMyMoments;
		
	}
	
	public long getId() {return this.id;}
	
	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}

	public User getFriend() {return friend;}
	public void setFriend(User friend) {this.friend = friend;}

	public boolean isAllowToSeeMyMoments() {return allowToSeeMyMoments;}
	public void setAllowToSeeMyMoments(boolean allowToSeeMyMoments) {
		this.allowToSeeMyMoments = allowToSeeMyMoments;
	} 
}
