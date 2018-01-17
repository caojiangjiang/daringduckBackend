package cn.daringduck.communitybuilder.model;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Part of a moment
 * 
 * @author Jochem Ligtenberg
 */
@Entity
@Table(name = "moment_parts")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class MomentPart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long momentId;
	private int part;
	
	private String text;
	@NotNull @OneToOne private Picture picture;
	
	public MomentPart() { }
	
	public MomentPart(int part, String text, Picture picture,long momentId) {
		this.part = part;
		
		this.text = text;
		
		this.picture = picture;
		
		this.setMomentId(momentId);
	}
	
	public int getPart() { return part; }
	public void setPart(int part) { this.part = part; }

	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public Picture getPicture() { return picture; }
	public void setPicture(Picture picture) { this.picture = picture; }
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String toString() {
		return this.id+","+this.part+","+this.text;
	}

	public long getMomentId() {
		return momentId;
	}

	public void setMomentId(long momentId) {
		this.momentId = momentId;
	}

	
}
