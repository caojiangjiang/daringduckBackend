package cn.daringduck.communitybuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.daringduck.communitybuilder.model.Role;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.repository.RoleRepository;
import cn.daringduck.communitybuilder.repository.UserRepository;

@Service
public class TeacherService{

    private final UserRepository userRepository;
    private final Role teacherRole;

	@Autowired
    public TeacherService(UserRepository userRepository, RoleRepository roleRepository) {
    	this.userRepository = userRepository;
    	
    	teacherRole = roleRepository.findByNameIgnoreCase("teacher");
	}

	/**
	 * Get a list of 50 users every time page increases by one you will get the
	 * next 50 users
	 * 
	 * @param page
	 * @return 
	 * @return
	 */
	public Page<User> getTeachers(int page) {
		return userRepository.findByRole(teacherRole, new PageRequest(page, GenericService.PAGE_SIZE));
	}

}
