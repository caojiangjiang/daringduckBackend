package cn.daringduck.communitybuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.daringduck.communitybuilder.model.Role;
import cn.daringduck.communitybuilder.repository.RoleRepository;

@Service
public class RoleService extends GenericService<Role, Integer>{
	
	private final RoleRepository roleRepository;

	@Autowired
    public RoleService(RoleRepository roleRepository) {
    	super(roleRepository);
    	this.roleRepository = roleRepository;
	}
	
	public Role addRole(String name, int rank) {
		Role role = new Role(name, rank);
		roleRepository.save(role);
		return role;
	}

}
