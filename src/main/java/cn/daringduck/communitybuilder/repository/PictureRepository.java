package cn.daringduck.communitybuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.Picture;

public interface PictureRepository extends JpaRepository<Picture,Long> {
	
	@Query(value = ("select picture from Picture picture where picture.id=?1"))
	public Picture getPictureById(long id);
}