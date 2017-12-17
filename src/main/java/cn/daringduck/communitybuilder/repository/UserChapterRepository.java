package cn.daringduck.communitybuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.daringduck.communitybuilder.model.UserChapter;

public interface UserChapterRepository extends JpaRepository<UserChapter,Long> {
	
	public UserChapter findByUserIdAndChapterId(long userId,long chapterId);

}
