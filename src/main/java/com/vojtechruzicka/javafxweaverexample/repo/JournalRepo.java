package com.vojtechruzicka.javafxweaverexample.repo;

import com.vojtechruzicka.javafxweaverexample.entyti.JournalEntity;
import org.springframework.data.repository.CrudRepository;

public interface JournalRepo extends CrudRepository<JournalEntity, Long> {
}
