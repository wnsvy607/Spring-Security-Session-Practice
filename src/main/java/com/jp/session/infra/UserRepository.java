package com.jp.session.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jp.session.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u"
		+ " join fetch u.role"
		+ " where u.email = :email")
	Optional<User> findByEmail(@Param("email") String email);


}
