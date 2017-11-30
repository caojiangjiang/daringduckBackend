package cn.daringduck.communitybuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.daringduck.communitybuilder.model.AuthToken;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String>{
	
}
