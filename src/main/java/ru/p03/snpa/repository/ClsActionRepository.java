package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.ClsAction;

import java.util.Optional;

public interface ClsActionRepository extends CrudRepository<ClsAction, Long> {
    @Override
    Iterable<ClsAction> findAll();

    Iterable<ClsAction> findAllByCodeIn(String[] code);

    Iterable<ClsAction> findAllByParentCode(String parentCode);

    Optional<ClsAction> findFirstByParentCode(String parentCode);

    ClsAction findFirstByCode(String code);

}
