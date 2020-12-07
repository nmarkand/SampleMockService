package com.be.mock.service.repo;

import com.be.mock.service.db.entity.DB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbRepository extends JpaRepository<DB, Long> {
}
