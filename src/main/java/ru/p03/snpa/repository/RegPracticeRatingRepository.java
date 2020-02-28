package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.RegPracticeRating;

public interface RegPracticeRatingRepository extends CrudRepository<RegPracticeRating, Long> {

    @Override
    <S extends RegPracticeRating> S save(S s);

    @Override
    Iterable<RegPracticeRating> findAll();
}
