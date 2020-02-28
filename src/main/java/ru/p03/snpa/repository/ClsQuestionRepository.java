package ru.p03.snpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.p03.snpa.entity.ClsQuestion;
import ru.p03.snpa.entity.RegPractice;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

public interface ClsQuestionRepository extends CrudRepository<ClsQuestion, Long> {


//    @Override
//    Iterable<ClsQuestion> findAll();
//
//    @Override
//    <S extends ClsQuestion> S save(S s);

    @Modifying
    @Transactional
    @Query(value = "update main.cls_question SET ts_content = to_tsvector('russian', coalesce(content, ''))", nativeQuery = true)
    int updateTsColumns();
}
