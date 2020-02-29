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


    @Query(value = SEARCH_BY_CONTENT_AND_KEYWORD, nativeQuery = true)
    Iterable<ClsQuestion> findAllByContentAndKeywords(String search);

    @Modifying
    @Transactional
    @Query(value = "update main.cls_question SET ts_content = to_tsvector('russian', coalesce(content, ''))", nativeQuery = true)
    int updateTsColumns();

    String SEARCH_BY_CONTENT_AND_KEYWORD ="SELECT * \n" +
            "        FROM main.cls_question p\n" +
            "        WHERE \n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_content,''), 'A')\n" +
//            "           setweight(coalesce(p.ts_keyword,''), 'B')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" ;

}
