package cn.daringduck.communitybuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.Chapter;
import cn.daringduck.communitybuilder.model.ChapterPart;

public interface ChapterPartRepository extends JpaRepository<ChapterPart, Long>{

	@Query(value = "select chapterPart from ChapterPart chapterPart where chapterPart.chapter.id = ?1")
	public List<ChapterPart> getChapterPartListByChapterId(long chapterId);
	
	
	@Query(value = "select chapterPart from ChapterPart chapterPart where chapterPart.id = ?1")
	public ChapterPart getChapterPartById(long chapterPartId);
	
	public int deleteByChapter(Chapter chapter); 
}
