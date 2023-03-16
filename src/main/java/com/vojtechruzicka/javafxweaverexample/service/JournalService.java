package com.vojtechruzicka.javafxweaverexample.service;

import com.vojtechruzicka.javafxweaverexample.entyti.JournalEntity;
import com.vojtechruzicka.javafxweaverexample.entyti.UserEntity;
import com.vojtechruzicka.javafxweaverexample.repo.JournalRepo;
import org.springframework.stereotype.Service;

@Service
public class JournalService {
    private final JournalRepo repo;
    public JournalService(JournalRepo repo) {
        this.repo = repo;
    }
    public Iterable<JournalEntity> getAll() {
        return repo.findAll();
    }
    public JournalEntity save(JournalEntity journal) {
        return repo.save(journal);
    }
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
