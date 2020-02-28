package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.ClsAction;
import ru.p03.snpa.entity.ClsLifeSituation;

import java.util.Optional;

public interface ClsLifeSituationRepository extends CrudRepository<ClsLifeSituation, Long> {
    @Override
    Iterable<ClsLifeSituation> findAll();

    Iterable<ClsLifeSituation> findAllByCodeIn(String[] code);

    Optional<ClsLifeSituation> findByCode(String code);

    Iterable<ClsLifeSituation> findAllByParentCode(String parentCode);

    Optional<ClsLifeSituation> findFirstByParentCode(String parentCode);

    ClsLifeSituation findFirstByCode(String code);
}
