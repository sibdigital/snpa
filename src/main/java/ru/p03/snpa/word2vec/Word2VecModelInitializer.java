package ru.p03.snpa.word2vec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.p03.snpa.entity.RegPractice;
import ru.p03.snpa.repository.RegPractice2Repository;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Word2VecModelInitializer {

    private static Logger log = LoggerFactory.getLogger(ru.p03.snpa.word2vec.Word2VecModelInitializer.class);

    private static Word2VecAdvisor initedWord2VecAdvisor;


    public static Word2VecAdvisor initialize(String pathToSerializedWord2VecModel, RegPractice2Repository repository){

        Word2VecAdvisor word2VecAdvisor = null;
        try {

            word2VecAdvisor = new Word2VecAdvisor(pathToSerializedWord2VecModel);

        } catch (IOException e) {

            log.error(e.toString());

            Iterable<RegPractice> documents = repository.findAll();

            List<String> docs = new ArrayList();
            documents.forEach(regPractice -> docs.add(regPractice.getContent()));
            Word2VecTrainer word2VecTrainer = new Word2VecTrainer();
            try {
                word2VecAdvisor = new Word2VecAdvisor(word2VecTrainer.train(docs, pathToSerializedWord2VecModel));
            } catch (Exception ex) {
                log.error(ex.toString());
            }
        }
        log.info("Model is ready!");
        initedWord2VecAdvisor = word2VecAdvisor;
        return word2VecAdvisor;
    };

    public static Word2VecAdvisor getInitedWord2VecAdvisor() {
        return initedWord2VecAdvisor;
    }
}
