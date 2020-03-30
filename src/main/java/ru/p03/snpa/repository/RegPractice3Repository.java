package ru.p03.snpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.p03.snpa.entity.RegPractice;

public interface RegPractice3Repository extends JpaRepository<RegPractice, Long>, RegPracticeCustomRepository{

}
