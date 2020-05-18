package ru.p03.snpa.repository;

import ru.p03.snpa.entity.RegPractice;

public interface RegPracticeCustomRepository {

    Iterable<RegPractice> findPracticeByParameters(String text_query, String[] payment_types, String[] actions, String[] life_situations, String[] questions, String whereConditionString, String orderConditionString);

}
