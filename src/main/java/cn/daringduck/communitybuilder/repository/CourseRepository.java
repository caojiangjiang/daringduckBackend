package cn.daringduck.communitybuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.daringduck.communitybuilder.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer>{

}
