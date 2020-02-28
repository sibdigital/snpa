package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.RegSearchStatisticView;

import java.util.Date;

public interface RegSearchStatisticViewRepository extends CrudRepository<RegSearchStatisticView, Long> {
    @Override
    <S extends RegSearchStatisticView> S save(S s);

    Iterable<RegSearchStatisticView> findAllBySearchDateTimeAfterAndSearchDateTimeBeforeOrderBySearchDateTimeDesc(Date dateStart, Date dateEnd);

}
