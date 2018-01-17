package cn.daringduck.communitybuilder.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import cn.daringduck.communitybuilder.model.Friends;
import cn.daringduck.communitybuilder.model.User;

public interface FriendsRepository extends JpaRepository<Friends, Long> {

//	@Query(value = "select friends Friends friends where friends.user.id = ?1")
//	List<Friends> findByUserId(long userId);
	
	List<Friends> findByUser(User user,Pageable pageable);
	
	List<Friends> findByUser(User user);
	
	List<Friends> findByFriend(User user,Pageable pageable);
	
	List<Friends> findByFriend(User user);
	
	int deleteByUserAndFriend(User user,User Friend);
	
	Friends findByUserAndFriend(User user,User friend);
}
