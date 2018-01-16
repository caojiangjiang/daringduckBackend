package cn.daringduck.communitybuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.ChapterChapterPart;


public interface ChapterChapterPartRepository extends JpaRepository<ChapterChapterPart, Long> {

	@Modifying
	@Query(value = "delete from ChapterChapterPart chapterChapterPart where chapterChapterPart.chapter.id = ?1 ")
	int deleteChapterPartFromChapterChapterPartByChapterId(long chapterId);
	
	@Query(value = "select chapterChapterPart from ChapterChapterPart chapterChapterPart where chapterChapterPart.chapter.id = ?1")
	List<ChapterChapterPart> getByChapterId(long chapterId);
	
	
	
}
