package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A Role in the system
 * 
 * @author Jochem
 *
 */
@Entity
@Table(name = "roles")
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private int rank;
	
	public Role() {}
	
	public Role(String name, int rank) {
		this.name = name;
		this.rank = rank;
	}
	
	public int getId() { return id; }
	
	public String getName() { return name; }
	
	public int getRank() { return rank; }
	
	// Implementation
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Role) {
			Role otherRole = (Role) obj;
			return otherRole.id == id && otherRole.name.equals(name) && otherRole.rank == rank;
		} else if (obj instanceof String) {
			String otherRole = (String) obj;
			return otherRole.equals(name);
		}
		
		return super.equals(obj);
	}
}
