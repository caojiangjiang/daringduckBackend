package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Part of a moment
 * 
 * @author Jochem Ligtenberg
 */
@Entity
@Table(name = "pictures")
public class Picture {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@NotNull private String file_location;

	public Picture() {}
	
	public Picture(String fileLocation) {
		this.file_location = fileLocation;
	}
	
	public long getId() { return id; }
	
	public String getFileLocation() { return file_location;}
	public void setFileLocation(String fileLocation) { this.file_location = fileLocation; }

}
