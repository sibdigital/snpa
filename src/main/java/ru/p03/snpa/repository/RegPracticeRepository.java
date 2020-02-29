package ru.p03.snpa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.p03.snpa.entity.RegPractice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

public interface RegPracticeRepository extends CrudRepository<RegPractice, Long> {

    @Override
    Iterable<RegPractice> findAll();

    @Override
    <S extends RegPractice> S save(S s);

    Iterable<RegPractice> findAllByDateOfDocumentAfterAndDateOfDocumentBeforeOrderByDateOfDocumentDesc(Date dateStart, Date dateEnd);
    Iterable<RegPractice> findAllByDateOfDocumentAfterOrderByDateOfDocumentDesc(Date dateStart);
    Iterable<RegPractice> findAllByDateOfDocumentBeforeOrderByDateOfDocumentDesc(Date dateEnd);

    Iterable<RegPractice> findAllByDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(Date dateStart, Date dateEnd, String docType);
    Iterable<RegPractice> findAllByDateOfDocumentAfterAndDocTypeOrderByDateOfDocumentDesc(Date dateStart, String docType);
    Iterable<RegPractice> findAllByDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(Date dateEnd, String docType);

    Iterable<RegPractice> findAllByDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeInOrderByDateOfDocumentDesc(Date dateStart, Date dateEnd, String[] docType);
    Iterable<RegPractice> findAllByDateOfDocumentAfterAndDocTypeInOrderByDateOfDocumentDesc(Date dateStart, String[] docType);
    Iterable<RegPractice> findAllByDateOfDocumentBeforeAndDocTypeInOrderByDateOfDocumentDesc(Date dateEnd, String[] docType);



    // ------------------- searchType == ALL ------------------------------
    @Query(name = "RegPractice.findAllValid")
    Iterable<RegPractice> findAllValid(@Param("now") Date date);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBefore")
    Iterable<RegPractice> findAllValidAndDateOfDocumentAfterAndDateOfDocumentBefore(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentAfter")
    Iterable<RegPractice> findAllValidAndDateOfDocumentAfter(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentBefore")
    Iterable<RegPractice> findAllValidAndDateOfDocumentBefore(@Param("now") Date date, @Param("dateEnd") Date dateEnd);

    @Query(name = "RegPractice.findAllExpired")
    Iterable<RegPractice> findAllExpired(@Param("now") Date date);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBefore")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBefore(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentAfter")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentAfter(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentBefore")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentBefore(@Param("now") Date date, @Param("dateEnd") Date dateEnd);

    @Query(name = "RegPractice.findAllInvalid")
    Iterable<RegPractice> findAllInvalid(@Param("now") Date date);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBefore")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBefore(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentAfter")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentAfter(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentBefore")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentBefore(@Param("now") Date date, @Param("dateEnd") Date dateEnd);
// ------------------- searchType == ALL ------------------------------


    // ------------------- searchType == P ------------------------------
    @Query(name = "RegPractice.findAllValidAndDocTypeP")
    Iterable<RegPractice> findAllValidAndDocTypeP(@Param("now") Date date);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP")
    Iterable<RegPractice> findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDocTypeP")
    Iterable<RegPractice> findAllValidAndDateOfDocumentAfterAndDocTypeP(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentBeforeAndDocTypeP")
    Iterable<RegPractice> findAllValidAndDateOfDocumentBeforeAndDocTypeP(@Param("now") Date date, @Param("dateEnd") Date dateEnd);

    @Query(name = "RegPractice.findAllExpiredAndDocTypeP")
    Iterable<RegPractice> findAllExpiredAndDocTypeP(@Param("now") Date date);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDocTypeP")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentAfterAndDocTypeP(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentBeforeAndDocTypeP")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentBeforeAndDocTypeP(@Param("now") Date date, @Param("dateEnd") Date dateEnd);

    @Query(name = "RegPractice.findAllInvalidAndDocTypeP")
    Iterable<RegPractice> findAllInvalidAndDocTypeP(@Param("now") Date date);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDocTypeP")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentAfterAndDocTypeP(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentBeforeAndDocTypeP")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentBeforeAndDocTypeP(@Param("now") Date date, @Param("dateEnd") Date dateEnd);
// ------------------- searchType == P ------------------------------


    // ------------------- searchType == R ------------------------------
    @Query(name = "RegPractice.findAllValidAndDocTypeR")
    Iterable<RegPractice> findAllValidAndDocTypeR(@Param("now") Date date);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR")
    Iterable<RegPractice> findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDocTypeR")
    Iterable<RegPractice> findAllValidAndDateOfDocumentAfterAndDocTypeR(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentBeforeAndDocTypeR")
    Iterable<RegPractice> findAllValidAndDateOfDocumentBeforeAndDocTypeR(@Param("now") Date date, @Param("dateEnd") Date dateEnd);

    @Query(name = "RegPractice.findAllExpiredAndDocTypeR")
    Iterable<RegPractice> findAllExpiredAndDocTypeR(@Param("now") Date date);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDocTypeR")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentAfterAndDocTypeR(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentBeforeAndDocTypeR")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentBeforeAndDocTypeR(@Param("now") Date date, @Param("dateEnd") Date dateEnd);

    @Query(name = "RegPractice.findAllInvalidAndDocTypeR")
    Iterable<RegPractice> findAllInvalidAndDocTypeR(@Param("now") Date date);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDocTypeR")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentAfterAndDocTypeR(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentBeforeAndDocTypeR")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentBeforeAndDocTypeR(@Param("now") Date date, @Param("dateEnd") Date dateEnd);
// ------------------- searchType == R ------------------------------

    // ------------------- searchType == Z ------------------------------
    @Query(name = "RegPractice.findAllValidAndDocTypeZ")
    Iterable<RegPractice> findAllValidAndDocTypeZ(@Param("now") Date date);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ")
    Iterable<RegPractice> findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentAfterAndDocTypeZ")
    Iterable<RegPractice> findAllValidAndDateOfDocumentAfterAndDocTypeZ(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllValidAndDateOfDocumentBeforeAndDocTypeZ")
    Iterable<RegPractice> findAllValidAndDateOfDocumentBeforeAndDocTypeZ(@Param("now") Date date, @Param("dateEnd") Date dateEnd);

    @Query(name = "RegPractice.findAllExpiredAndDocTypeZ")
    Iterable<RegPractice> findAllExpiredAndDocTypeZ(@Param("now") Date date);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentAfterAndDocTypeZ")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentAfterAndDocTypeZ(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllExpiredAndDateOfDocumentBeforeAndDocTypeZ")
    Iterable<RegPractice> findAllExpiredAndDateOfDocumentBeforeAndDocTypeZ(@Param("now") Date date, @Param("dateEnd") Date dateEnd);

    @Query(name = "RegPractice.findAllInvalidAndDocTypeZ")
    Iterable<RegPractice> findAllInvalidAndDocTypeZ(@Param("now") Date date);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ(@Param("now") Date date, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentAfterAndDocTypeZ")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentAfterAndDocTypeZ(@Param("now") Date date, @Param("dateStart") Date dateStart);
    @Query(name = "RegPractice.findAllInvalidAndDateOfDocumentBeforeAndDocTypeZ")
    Iterable<RegPractice> findAllInvalidAndDateOfDocumentBeforeAndDocTypeZ(@Param("now") Date date, @Param("dateEnd") Date dateEnd);
// ------------------- searchType == Z ------------------------------


    Iterable<RegPractice> findAllByDocTypeOrderByDateOfDocumentDesc(String docType);

    Iterable<RegPractice> findAllByDocTypeInOrderByDateOfDocumentDesc(String[] docTypes);

    Page<RegPractice> findAllByCodeParentAndDocType(Pageable pageable, String codeParent, String DocType);

    Optional<RegPractice> findFirstByCode(String code);

    Optional<RegPractice> findFirstByCodeAndDocType(String code, String docType);

    Optional<RegPractice> findById(Long aLong);

    Iterable<RegPractice> findAllByCodeInOrderByDateOfDocumentDesc(String[] code);

    // ------------------- ------------------------------
    @Modifying
    @Transactional
    @Query(value = "update main.reg_practice SET ts_content = to_tsvector('russian', coalesce(strip_tags(content), ''))"
            + ", ts_number = to_tsvector('russian', coalesce(number, ''))"
            + ", ts_name = to_tsvector('russian', coalesce(name, ''))"
            + ", ts_parent_name = to_tsvector('russian', coalesce(parent_name, '')) ", nativeQuery = true)
    int updateTsColumns();
}
