package ru.p03.snpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.RegPractice;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;
import java.util.Map;

public interface RegPractice2Repository extends CrudRepository<RegPractice, Long> {

    Optional<RegPractice> findByCode(String code);

    @Query(value = FULL_TEXT_SEARCH_ORDER_BY_DATE, nativeQuery = true)
    Iterable<RegPractice> findAllByFullTextSearchOrderByDateOfDocument(String search);

    @Query(value = FULL_TEXT_SEARCH_WITH_DOC_TYPE_ORDER_BY_DATE, nativeQuery = true)
    Iterable<RegPractice> findAllByFullTextSearchAndDocTypeOrderByDateOfDocument(String search, String docType);

    @Query(value = FULL_TEXT_SEARCH_WITH_DOC_TYPE_IN_ORDER_BY_DATE, nativeQuery = true)
    Iterable<RegPractice> findAllByFullTextSearchAndDocTypeInOrderByDateOfDocument(String search, String[] docTypes);

    @Query(value = FULL_TEXT_SEARCH_WITH_CODES_ORDER_BY_DATE, nativeQuery = true)
    Iterable<RegPractice> findAllByFullTextSearchAndCodeInOrderByDateOfDocument(String search, String[] codes);

    @Query(value = VALID_FULL_TEXT_SEARCH_ORDER_BY_DATE, nativeQuery = true)
    Iterable<RegPractice> findAllValidByFullTextSearchOrderByDateOfDocument(String search, Date now);

    @Query(value = EXPIRED_FULL_TEXT_SEARCH_ORDER_BY_DATE, nativeQuery = true)
    Iterable<RegPractice> findAllExpiredByFullTextSearchOrderByDateOfDocument(String search, Date now);

    @Query(value = INVALID_FULL_TEXT_SEARCH_ORDER_BY_DATE, nativeQuery = true)
    Iterable<RegPractice> findAllInvalidByFullTextSearchOrderByDateOfDocument(String search, Date now);

    @Query(value = FULL_TEXT_SEARCH_ORDER_BY_RELEVANCE, nativeQuery = true)
    Iterable<RegPractice> findAllByFullTextSearchOrderByRelevance(String search);

    @Query(value = FULL_TEXT_SEARCH_WITH_DOC_TYPE_ORDER_BY_RELEVANCE, nativeQuery = true)
    Iterable<RegPractice> findAllByFullTextSearchAndDocTypeOrderByRelevance(String search, String docType);

    @Query(value = FULL_TEXT_SEARCH_WITH_DOC_TYPE_IN_ORDER_BY_RELEVANCE, nativeQuery = true)
    Iterable<RegPractice> findAllByFullTextSearchAndDocTypeInOrderByRelevance(String search, String[] docTypes);

    @Query(value = FULL_TEXT_SEARCH_WITH_CODES_ORDER_BY_RELEVANCE, nativeQuery = true)
    Iterable<RegPractice> findAllByFullTextSearchAndCodeInOrderByRelevance(String search, String[] codes);

    @Query(value = VALID_FULL_TEXT_SEARCH_ORDER_BY_RELEVANCE, nativeQuery = true)
    Iterable<RegPractice> findAllValidByFullTextSearchOrderByRelevance(String search, Date now);

    @Query(value = EXPIRED_FULL_TEXT_SEARCH_ORDER_BY_RELEVANCE, nativeQuery = true)
    Iterable<RegPractice> findAllExpiredByFullTextSearchOrderByRelevance(String search, Date now);

    @Query(value = INVALID_FULL_TEXT_SEARCH_ORDER_BY_RELEVANCE, nativeQuery = true)
    Iterable<RegPractice> findAllInvalidByFullTextSearchOrderByRelevance(String search, Date now);




    @Query(value = GET_NUMBERS_BY_DOC_TYPE_IN, nativeQuery = true)
    Iterable<String> findAllNumbersByDocTypeIn(String[] docTypes);

    @Query(value = GET_STATS_BY_DOC_TYPES, nativeQuery = true)
    Iterable<Map<String, Integer>> findStatsByDocTypes();

    Iterable<RegPractice> findTop4ByOrderByDateOfDocumentDesc();


    String GET_STATS_BY_DOC_TYPES = "SELECT 'counter1' as name, COUNT(*) as counter FROM main.reg_practice WHERE ((doc_type = 'z') OR (doc_type = 'n')) AND code_parent IS NULL\n" +
            " union\n" +
            " SELECT 'counter2' as name, COUNT(*) as counter FROM main.reg_practice WHERE ((doc_type = 'z') OR (doc_type = 'n')) AND NOT code_parent IS NULL\n" +
            " union\n" +
            " SELECT 'counter3' as name, COUNT(*) as counter FROM main.reg_practice WHERE doc_type = 'p'\n" +
            " union\n" +
            " SELECT 'counter4' as name, COUNT(*) as counter FROM main.reg_practice WHERE doc_type = 'f'\n" +
            " order by 1";

    String GET_NUMBERS_BY_DOC_TYPE_IN = "SELECT p.number\n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.doc_type IN (?1)";

    String VALID_FULL_TEXT_SEARCH_ORDER_BY_DATE = "SELECT * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE (p.date_start<?2 OR p.date_end>?2)" +
            "               AND(p.date_start=null OR p.date_end>?2)" +
            "               AND(p.date_start<?2 OR p.date_end=null)" +
            "               AND(p.date_start=null OR p.date_end=null) AND \n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY date_of_document DESC\n";

    String EXPIRED_FULL_TEXT_SEARCH_ORDER_BY_DATE = "SELECT * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.date_end<?2 AND\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY date_of_document DESC\n";

    String INVALID_FULL_TEXT_SEARCH_ORDER_BY_DATE = "SELECT * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.date_start>?2 AND \n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY date_of_document DESC\n";

    String FULL_TEXT_SEARCH_ORDER_BY_DATE = "SELECT * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE \n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY date_of_document DESC\n";

    String FULL_TEXT_SEARCH_WITH_DOC_TYPE_ORDER_BY_DATE = "SELECT * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.doc_type=?2 AND\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY date_of_document DESC\n";

    String FULL_TEXT_SEARCH_WITH_DOC_TYPE_IN_ORDER_BY_DATE = "SELECT * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.doc_type IN (?2) AND\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY date_of_document DESC\n";

    String FULL_TEXT_SEARCH_WITH_CODES_ORDER_BY_DATE = "SELECT * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.code IN (?2) AND\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY date_of_document DESC\n";




    String VALID_FULL_TEXT_SEARCH_ORDER_BY_RELEVANCE = "SELECT   " +
            "ts_rank(\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B')\n" +
            "           ), plainto_tsquery('russian', ?1)\n" +
            "        ), * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE (p.date_start<?2 OR p.date_end>?2)" +
            "               AND(p.date_start=null OR p.date_end>?2)" +
            "               AND(p.date_start<?2 OR p.date_end=null)" +
            "               AND(p.date_start=null OR p.date_end=null) AND \n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY ts_rank DESC, date_of_document DESC\n";

    String EXPIRED_FULL_TEXT_SEARCH_ORDER_BY_RELEVANCE = "SELECT " +
            "ts_rank(\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B')\n" +
            "           ), plainto_tsquery('russian', ?1)\n" +
            "        ), * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.date_end<?2 AND\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY ts_rank DESC, date_of_document DESC\n";

    String INVALID_FULL_TEXT_SEARCH_ORDER_BY_RELEVANCE = "SELECT " +
            "ts_rank(\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B')\n" +
            "           ), plainto_tsquery('russian', ?1)\n" +
            "        ), * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.date_start>?2 AND \n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY ts_rank DESC, date_of_document DESC\n";

    String FULL_TEXT_SEARCH_ORDER_BY_RELEVANCE = "SELECT \n" +
            "ts_rank(\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B')\n" +
            "           ), plainto_tsquery('russian', ?1)\n" +
            "        ), * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE \n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY ts_rank DESC, date_of_document DESC\n";

    String FULL_TEXT_SEARCH_WITH_DOC_TYPE_ORDER_BY_RELEVANCE = "SELECT \n" +
            "ts_rank(\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B')\n" +
            "           ), plainto_tsquery('russian', ?1)\n" +
            "        ), * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.doc_type=?2 AND\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY ts_rank DESC, date_of_document DESC\n";

    String FULL_TEXT_SEARCH_WITH_DOC_TYPE_IN_ORDER_BY_RELEVANCE = "SELECT \n" +
            "ts_rank(\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B')\n" +
            "           ), plainto_tsquery('russian', ?1)\n" +
            "        ), * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.doc_type IN (?2) AND\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY ts_rank DESC, date_of_document DESC\n";

    String FULL_TEXT_SEARCH_WITH_CODES_ORDER_BY_RELEVANCE = "SELECT \n" +
            "ts_rank(\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B')\n" +
            "           ), plainto_tsquery('russian', ?1)\n" +
            "        ), * \n" +
            "        FROM main.reg_practice p\n" +
            "        WHERE p.code IN (?2) AND\n" +
            "        (\n" +
            "           setweight(coalesce(p.ts_number,''), 'A') ||\n" +
            "           setweight(coalesce(p.ts_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_parent_name,''), 'B') ||\n" +
            "           setweight(coalesce(p.ts_content,''), 'C')\n" +
            "        ) @@ plainto_tsquery('russian', ?1)\n" +
            "ORDER BY  ts_rank DESC, date_of_document DESC\n";

// можно на кейсы переделать потом
//    SELECT *
//    FROM main.reg_practice p
//    WHERE
//            CASE
//    WHEN 'NULL' IS NOT NULL
//    THEN p.number='123'
//    ELSE true
//    END
//    AND
//            (
//                    setweight(to_tsvector('russian', p.number), 'A') ||
//    setweight(to_tsvector('russian', p.name), 'B') ||
//    setweight(to_tsvector('russian', p.parent_name), 'B') ||
//    setweight(to_tsvector('russian', coalesce(p.content, '')), 'C')
//            ) @@ plainto_tsquery('russian', 'пенсия')
//    ORDER BY date_of_document DESC
}
