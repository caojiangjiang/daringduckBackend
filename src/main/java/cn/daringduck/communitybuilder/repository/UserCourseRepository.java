package cn.daringduck.communitybuilder.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.UserCourse;

public interface UserCourseRepository extends JpaRepository<UserCourse,Long>{

	public UserCourse findByUserIdAndCourseId(long userId,int courseId);
	
	@Query(value = "select userCourse from UserCourse userCourse where userCourse.user.id=?1")
	List<UserCourse> findByUserId(long userId);
	
	@Modifying
	@Query(value = "delete from UserCourse userCourse where userCourse.user.id=?1 and userCourse.course.id = ?2")
	int deleteUserCourseByUserIdAndCourseId(long userId,int courseId);
}
