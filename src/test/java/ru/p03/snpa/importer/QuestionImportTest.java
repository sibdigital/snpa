package ru.p03.snpa.importer;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.p03.snpa.entity.ClsQuestion;
import ru.p03.snpa.repository.ClsQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionImportTest {


    @Autowired
    ClsQuestionRepository clsQuestionRepository;

    @Autowired
    QuestionImport questionImport;

    private static final Logger log = LoggerFactory.getLogger(PracticeImport.class);

    @Test
    @Ignore
    public void startImportQuestion() {

        log.info("startImportQuestion Start");
        //update ts_content column
        questionImport.startImportQuestion();
        log.info("startImportQuestion End.");

    }
}
