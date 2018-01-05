package cn.daringduck.communitybuilder.repository;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer>{
	
	@Query(value = "select course from Course course where upper(course.english_name) like %?1% ")
	public List<Course> findByEnglishNameLike(String name);
	
	@Query(value = "select course from Course course where upper(course.chinese_name) like %?1% ")
	public List<Course> findByChineseNameLike(String name);
	
	@Query(value = "select course from Course course where upper(course.dutch_name) like %?1% ")
	public List<Course> findByDutchNameLike(String name);
}
