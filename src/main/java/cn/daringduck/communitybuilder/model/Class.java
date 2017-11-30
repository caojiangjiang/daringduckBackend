package cn.daringduck.communitybuilder.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "classes")
public class Class {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String code;
	@NotNull private String name;
	@NotNull @OneToOne private Club club;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "user_class", 
    	joinColumns = @JoinColumn(name = "class_id", referencedColumnName = "id"), 
    	inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private List<User> members;

	public Class() {}

	public Class(String code, String name, Club club) {
		this.code = code;
		this.name = name;
		this.club = club;
	}
	
	public long getId() { return id; }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public Club getClub() { return club; }
	public void setClub(Club club) { this.club = club; }

	public List<User> getMembers() { return members; }
	public void addMember(User member) { this.members.add(member); }
	public void removeMember(User member) { this.members.remove(member); }
	public boolean isMember(User member) { return this.members.contains(member); }
	
}
