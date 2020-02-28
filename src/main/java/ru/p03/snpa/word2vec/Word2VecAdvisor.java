package ru.p03.snpa.word2vec;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;

public class Word2VecAdvisor {

    private static Logger log = LoggerFactory.getLogger(ru.p03.snpa.word2vec.Word2VecAdvisor.class);

    private Word2Vec advisor;

    public Word2VecAdvisor(Word2Vec advisor) {
        this.advisor = advisor;
    }

    public Word2VecAdvisor(String pathToSerializedWord2VecModel) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathToSerializedWord2VecModel));
        try {
            Word2Vec word2Vec = (Word2Vec) ois.readObject();
            this.advisor = word2Vec;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAdvisor(Word2Vec advisor) {
        this.advisor = advisor;
    }

    public Collection<String> getSuggestionsForWord(String word, int numberOfSuggestions){
        Collection<String> lst = new ArrayList<>(0);
        try{
            lst = this.advisor.wordsNearestSum(word, numberOfSuggestions);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return lst;
    }


}
