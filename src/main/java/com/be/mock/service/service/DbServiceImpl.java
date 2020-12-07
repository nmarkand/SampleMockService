package com.be.mock.service.service;

import com.be.mock.service.db.entity.DB;
import com.be.mock.service.repo.DbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DbServiceImpl {
    private DbRepository dbRepository;

    @Autowired
    public DbServiceImpl(DbRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    public DB storeDb(DB db) {
        return dbRepository.save(db);
    }
    
    public List<DB> findAllDb() {
        return dbRepository.findAll();
    }

    public Optional<DB> findDbById(Long id) {
        return dbRepository.findById(id);
    }
}
