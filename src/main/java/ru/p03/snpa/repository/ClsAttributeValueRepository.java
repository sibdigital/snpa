package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.ClsAttributeValue;
import ru.p03.snpa.entity.ClsLifeSituation;

import java.util.Optional;

public interface ClsAttributeValueRepository extends CrudRepository<ClsAttributeValue, Long> {
    @Override
    <S extends ClsAttributeValue> S save(S s);

    @Override
    Iterable<ClsAttributeValue> findAll();

    Optional<ClsAttributeValue> findFirstByName(String name);

    Optional<ClsAttributeValue> findByCode(String code);

    Iterable<ClsAttributeValue> findAllByCodeIn(String[] code);

}
