package cn.daringduck.communitybuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.CourseChapter;


public interface CourseChapterRepository extends JpaRepository<CourseChapter, Long>{
	
	@Query(value = "select courseChapter from CourseChapter courseChapter where courseChapter.course.id = ?1 order by position asc")
	List<CourseChapter> getChapterFromCourseChapterByCourseId(int courseId);
	
	@Modifying
	@Query(value = "delete from CourseChapter courseChapter where courseChapter.course.id = ?1 ")
	int deleteChapterFromCourseChapterByCourseId(int courseId);
	
	@Modifying
	@Query(value = "delete from CourseChapter courseChapter where courseChapter.chapter.id = ?1")
	int deleteChapterFromCourseChapterByChapterId(long chapterId); 

}
