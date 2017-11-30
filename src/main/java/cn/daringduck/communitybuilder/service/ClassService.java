package cn.daringduck.communitybuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.daringduck.communitybuilder.Error;
import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Class;
import cn.daringduck.communitybuilder.model.Club;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.repository.ClassRepository;
import cn.daringduck.communitybuilder.repository.ClubRepository;
import cn.daringduck.communitybuilder.repository.RoleRepository;
import cn.daringduck.communitybuilder.repository.UserRepository;

@Service
public class ClassService extends GenericService<Class, Integer>{

	private ClassRepository classRepository;
	private ClubRepository clubRepository;
	private UserRepository userRepository;

	@Autowired
	public ClassService(ClassRepository classRepository, ClubRepository clubRepository, UserRepository userRepository,
			RoleRepository roleRepository) {
		super(classRepository);
		this.classRepository = classRepository;
		this.clubRepository = clubRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Create a new class
	 * @param code
	 * @param name
	 * @param clubId
	 * @return
	 */
	public Class createClass(String code, String name, int clubId) {
		Club club = clubRepository.findOne(clubId);

		Class theClass = new Class(code, name, club);

		classRepository.save(theClass);

		return theClass;
	}
	
	/**
	 * Get a page of users that are not yet member of a class
	 * @param classId
	 * @param page
	 * @return
	 */
	public Page<User> getAvailableMembers(int classId, int page) {
		return userRepository.getUsersNotInClass(classId, new PageRequest(page, 25));
	}

	/**
	 * Update the class information
	 * @param id
	 * @param code
	 * @param name
	 * @param clubId
	 * @return
	 */
	public Class updateClass(int id, String code, String name, int clubId) {
		Class theClass = classRepository.findOne(id);

		if (code != null) {
			theClass.setCode(code);
		}
		
		if (name != null) {
			theClass.setName(name);
		}

		if (clubId != 0) {
			Club club = clubRepository.findOne(clubId);
			theClass.setClub(club);
		}

		classRepository.save(theClass);

		return theClass;
	}

	public void deleteClass(int id) {
		classRepository.delete(id);
	}
	
	/**
	 * Add a member to a class
	 * @param theClass
	 * @param member
	 * @throws RequestException
	 */
	public void addMember(Class theClass, User member) throws RequestException {
		//if the member is already member of the class
		if(theClass.isMember(member)) {
			throw new RequestException(Error.USER_ALREADY_MEMBER_OF_CLASS);
		}
		
		theClass.addMember(member);
		
		classRepository.save(theClass);
	}

	/**
	 * Remove a member of a class
	 * @param theClass
	 * @param member
	 * @throws RequestException
	 */
	public void removeMember(Class theClass, User member) throws RequestException {
		//if the member is a member of the class
		if(theClass.isMember(member)) {
			theClass.removeMember(member);		
		}
		//if the member is not a member of the class
		else {
			throw new RequestException(Error.USER_NOT_MEMBER_OF_CLASS);
		}
		
		classRepository.save(theClass);
	}

}
