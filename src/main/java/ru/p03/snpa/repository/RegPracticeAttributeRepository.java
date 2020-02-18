package ru.p03.snpa.repository;

import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.RegPracticeAttribute;

public interface RegPracticeAttributeRepository extends CrudRepository<RegPracticeAttribute, Long> {

    @Override
    Iterable<RegPracticeAttribute> findAll();

    Iterable<RegPracticeAttribute> findAllByAttributeTypeAndCodeAttributeIn(int attributeType, String[] codeAttribute);

    Iterable<RegPracticeAttribute> findAllByAttributeTypeAndCodeAttributeInAndDocType(int attributeType, String[] codeAttribute, String docType);

    Iterable<RegPracticeAttribute> findAllByCodePractice(String codePractice);

    Iterable<RegPracticeAttribute> findAllByCodePracticeAndAttributeType(String codePractice, int attributeType);


}
