package ru.p03.snpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.p03.snpa.entity.forms.TagForm;

public interface TagFormRepository extends CrudRepository<TagForm, Long> {

    String TOP_TAGS = "WITH \n" +
            "   payment_type AS (\n" +
            "     SELECT cpt.name, cpt.code, cpt.parent_code, 1 as type, count(cpt.code) as count_tags\n" +
            "     FROM main.cls_payment_type cpt\n" +
            "     INNER JOIN main.reg_practice_attribute rpa ON rpa.code_attribute =  cpt.code AND rpa.attribute_type = 1 \n" +
            "     INNER JOIN main.reg_practice rp ON rp.code = rpa.code_practice AND rp.code IN (?1)\n" +
            "     GROUP BY cpt.name, cpt.code, cpt.parent_code\n" +
            "   ),\n" +
            "\n" +
            "    action AS (\n" +
            "     SELECT ca.name, ca.code, ca.parent_code, 2 as type, count(ca.code)  as count_tags \n" +
            "     FROM main.cls_action ca\n" +
            "     INNER JOIN main.reg_practice_attribute rpa ON rpa.code_attribute =  ca.code AND rpa.attribute_type = 2 \n" +
            "     INNER JOIN main.reg_practice rp ON rp.code = rpa.code_practice AND rp.code IN (?1)\n" +
            "     GROUP BY ca.name, ca.code, ca.parent_code\n" +
            "   ),      \n" +
            "   \n" +
            "    life_situation AS (\n" +
            "     SELECT cls.name, cls.code, cls.parent_code, 3 as type, count(cls.code)  as count_tags \n" +
            "     FROM main.cls_life_situation cls\n" +
            "     INNER JOIN main.reg_practice_attribute rpa ON rpa.code_attribute =  cls.code AND rpa.attribute_type = 3 \n" +
            "     INNER JOIN main.reg_practice rp ON rp.code = rpa.code_practice AND rp.code IN (?1)\n" +
            "     GROUP BY cls.name, cls.code, cls.parent_code\n" +
            "   ),\n" +
            "    \n" +
            "    union_tags as (\n" +
            "        select * from payment_type\n" +
            "        union\n" +
            "        select * from action\n" +
            "        union\n" +
            "        select * from life_situation\n" +
            "        \n" +
            "    )\n" +
            "    \n" +
            "\n" +
            "    select row_number() OVER() as id, name, code, parent_code, type \n" +
            "    from union_tags \n" +
            "    order by count_tags desc\n" +
            "    FETCH FIRST 10 ROWS ONLY";

    @Query(value = TOP_TAGS, nativeQuery = true)
    Iterable<TagForm> findTopTags(String[] idPractices);
}
