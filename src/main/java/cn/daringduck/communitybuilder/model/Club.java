package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * One of the daring duck clubs
 * 
 * @author Jochem Ligtenberg
 */
@Entity
@Table(name = "clubs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Club {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String code;
	private String name;
	private String location;

	public Club() {}
	
	public Club(String code, String name, String location) {
		this.code = code;
		this.name = name;
		this.location = location;
	}

	public int getId() { return id; }
	
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }
	
}
