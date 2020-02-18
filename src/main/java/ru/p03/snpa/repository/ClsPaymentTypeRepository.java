package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.ClsPaymentType;

import java.util.Optional;

public interface ClsPaymentTypeRepository extends CrudRepository<ClsPaymentType, Long> {

    @Override
    Iterable<ClsPaymentType> findAll();

    Iterable<ClsPaymentType> findAllByCodeIn(String[] code);

    Iterable<ClsPaymentType> findAllByParentCode(String parentCode);

    Optional<ClsPaymentType> findFirstByParentCode(String parentCode);

}
