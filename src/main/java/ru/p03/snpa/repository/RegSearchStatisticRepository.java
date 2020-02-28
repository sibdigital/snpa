package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.RegPractice;
import ru.p03.snpa.entity.RegSearchStatistic;

import java.util.Date;
import java.util.Optional;

public interface RegSearchStatisticRepository extends CrudRepository<RegSearchStatistic, Long> {
    @Override
    <S extends RegSearchStatistic> S save(S s);

    Optional<RegSearchStatistic> findById(Long aLong);

    Iterable<RegSearchStatistic> findAllBySearchDateTimeAfterAndSearchDateTimeBeforeOrderBySearchDateTimeDesc(Date dateStart, Date dateEnd);

}
