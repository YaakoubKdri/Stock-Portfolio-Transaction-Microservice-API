package com.kadri.portfolio_service.repository;

import com.kadri.portfolio_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
