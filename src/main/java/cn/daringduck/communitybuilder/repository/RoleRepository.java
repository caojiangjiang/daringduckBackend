package cn.daringduck.communitybuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.daringduck.communitybuilder.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Role findByNameIgnoreCase(String name);
	
}
