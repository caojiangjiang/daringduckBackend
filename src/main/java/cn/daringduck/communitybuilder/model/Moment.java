package cn.daringduck.communitybuilder.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "moments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Moment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@NotNull private Date posted;
	@NotNull private String title;
	@NotNull @OneToOne private User user;
	@NotNull private Privacy privacy;
	private boolean hidden;
	private String eventDate;
	
	public Moment() { }
	
	public Moment(String title, User user, Privacy privacy,String eventDate) {
		this.title = title;
		this.user = user;
		this.privacy = privacy;
		this.posted = new Date();
		this.eventDate =  eventDate;
	}

	public long getId() { return id; }

	public String getPosted() {
		SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return myFmt.format(posted);
	}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public User getUser() { return user; }
	
	public Privacy getPrivacy() { return privacy; }
	public void setPrivacy(Privacy privacy) { this.privacy = privacy; }

	public boolean isHidden() { return hidden; }
	public void setHidden(boolean hidden) { this.hidden = hidden; }
	

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	
}
