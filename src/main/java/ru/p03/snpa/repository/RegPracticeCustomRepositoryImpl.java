package ru.p03.snpa.repository;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.jpa.TypedParameterValue;
import ru.p03.snpa.entity.RegPractice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class RegPracticeCustomRepositoryImpl implements RegPracticeCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RegPractice> findPracticeByParameters(String text_query, String[] payment_types, String[] actions, String[] life_situations, String whereConditionString, String orderConditionString) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from main.get_reg_practice(:text_query, :payment_types, :actions, :life_situations) as temp_pr ");
        sb.append(" inner join main.reg_practice as pr on pr.id = temp_pr.id ");
        sb.append(whereConditionString);
        sb.append(orderConditionString);
        Query query = entityManager.createNativeQuery(sb.toString(), RegPractice.class);

        query.setParameter("text_query", text_query);
        query.setParameter("payment_types", new TypedParameterValue(StringArrayType.INSTANCE, payment_types));
        query.setParameter("actions", new TypedParameterValue(StringArrayType.INSTANCE, actions));
        query.setParameter("life_situations", new TypedParameterValue(StringArrayType.INSTANCE, life_situations));

        List<RegPractice> res = query.getResultList();
        return res;
    }
}