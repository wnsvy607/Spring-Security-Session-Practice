package com.jp.session.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jp.session.constant.UserRole;
import com.jp.session.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {


	@Query("select r from Role r where r.userRole = :userRole ")
	Optional<Role> findByName(@Param("userRole") UserRole userRole);
}
