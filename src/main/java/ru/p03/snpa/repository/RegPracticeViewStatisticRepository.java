package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.RegPracticeViewStatistic;

public interface RegPracticeViewStatisticRepository extends CrudRepository<RegPracticeViewStatistic, Long> {

    @Override
    <S extends RegPracticeViewStatistic> S save(S s);

    @Override
    Iterable<RegPracticeViewStatistic> findAll();
}
