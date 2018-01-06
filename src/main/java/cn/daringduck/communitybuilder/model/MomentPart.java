package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Part of a moment
 * 
 * @author Jochem Ligtenberg
 */
@Entity
@Table(name = "moment_parts")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class MomentPart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long moment_Id;
	private int part;
	
	private String text;
	@NotNull @OneToOne private Picture picture;
	
	public MomentPart() { }
	
	public MomentPart(int part, String text, Picture picture,long moment_Id) {
		this.part = part;
		this.text = text;
		this.picture = picture;
		this.moment_Id = moment_Id;
	}
	
	public int getPart() { return part; }
	public void setPart(int part) { this.part = part; }

	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public Picture getPicture() { return picture; }
	public void setPicture(Picture picture) { this.picture = picture; }

	public Long getMomentId() {
		return this.moment_Id;
	}

	public void setMomentId(long moment_Id) {
		this.moment_Id = moment_Id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String toString() {
		return this.id+","+this.moment_Id+","+this.part+","+this.text;
	}
	
}
