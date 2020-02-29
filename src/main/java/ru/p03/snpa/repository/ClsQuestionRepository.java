package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.ClsQuestion;

import java.util.Optional;

public interface ClsQuestionRepository extends CrudRepository<ClsQuestion, Long> {
    @Override
    Iterable<ClsQuestion> findAll();

    Iterable<ClsQuestion> findAllByCodeIn(String[] code);

    Optional<ClsQuestion> findByCode(String code);

    Iterable<ClsQuestion> findAllByParentCode(String parentCode);

    Iterable<ClsQuestion> findAllByPracticeCode(String practiceCode);

    Optional<ClsQuestion> findFirstByParentCode(String parentCode);

    ClsQuestion findFirstByCode(String code);
}
