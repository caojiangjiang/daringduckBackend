package cn.daringduck.communitybuilder.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.MomentPart;
import cn.daringduck.communitybuilder.model.Picture;


public interface MomentPartRepository  extends JpaRepository<MomentPart, Long> {
	
	
	@Query(value = "SELECT momentPart FROM MomentPart momentPart WHERE momentPart.momentId = ?1")
	List<MomentPart> getMomentPart(long moment_id);
	
	@Modifying
	@Query("update MomentPart momentPart set  momentPart.text=?2 , picture=?3 where momentPart.id = ?1")
	public void upDateMomentPart(long momentPartId,String text,Picture picture);

}
