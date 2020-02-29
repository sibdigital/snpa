package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.ClsPracticePractice;

public interface ClsPracticePracticeRepository extends CrudRepository<ClsPracticePractice, Long> {
    @Override
    Iterable<ClsPracticePractice> findAll();

    Iterable<ClsPracticePractice> findAllByPractice1Code(String practice1Code);

    Iterable<ClsPracticePractice> findAllByPractice2Code(String practice2Code);

    Iterable<ClsPracticePractice> findAllByPractice1CodeContaining(String containing);

    Iterable<ClsPracticePractice> findAllByPractice2CodeContaining(String containing);
}
