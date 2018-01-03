package cn.daringduck.communitybuilder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.Moment;
import cn.daringduck.communitybuilder.model.User;

public interface MomentRepository extends JpaRepository<Moment, Long> {
	
	Page<Moment> findByUser(User user, Pageable pageable);
	
	@Query(value = "select moment from Moment moment where moment.posted =(select max(posted) from Moment moment where moment.user.id='14') ")
	Moment getDD01NewestMoment();
	
//	@Modifying
//	@Query(value = "select * from moments where moments.user_id in (1,5) ORDER BY moments.posted DESC LIMIT 5,25",nativeQuery=true)
//	List<Moment> getMyFriendsMoments();
	
	Page<Moment> findByUserIdInOrderByPostedDesc(List <Long> friendsValue,Pageable pageable);

	//	@Query(value = "select moment from Moment moment where moment.user.id in ?2 ORDER BY moment.posted DESC LIMIT ?1,25",nativeQuery=true)
}
