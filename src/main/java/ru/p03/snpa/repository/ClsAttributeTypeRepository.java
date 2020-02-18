package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.ClsAttributeType;

public interface ClsAttributeTypeRepository extends CrudRepository<ClsAttributeType, Long> {
    @Override
    <S extends ClsAttributeType> S save(S s);
}
