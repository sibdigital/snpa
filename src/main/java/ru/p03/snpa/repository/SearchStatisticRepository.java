package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.SearchStatistic;

public interface SearchStatisticRepository extends CrudRepository<SearchStatistic, Long> {
    @Override
    <S extends SearchStatistic> S save(S s);
}
