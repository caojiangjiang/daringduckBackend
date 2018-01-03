package cn.daringduck.communitybuilder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.daringduck.communitybuilder.model.Role;
import cn.daringduck.communitybuilder.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Page<User> findByRole(Role role, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE NOT EXISTS (SELECT 1 FROM Class c WHERE u MEMBER OF c.members AND c.id = :classid)")
	Page<User> getUsersNotInClass(@Param("classid") int classId, Pageable pageable);

	User findByUsernameIgnoreCase(String username);
	
	List<User> getByRoleId(int roleId); 
	
	List<User> findByClubId(int clubId);
	
}
