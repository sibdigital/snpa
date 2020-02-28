/*******************************************************************************
 * Copyright (c) 2015-2019 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package ru.p03.snpa.word2vec;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

/**
 * @author Victor Filippov
 * Word2Vec trainer
 */
public class Word2VecTrainer {


    private static Logger log = LoggerFactory.getLogger(ru.p03.snpa.word2vec.Word2VecTrainer.class);

    public Word2Vec train(Collection<String> sentences, String pathToSerializedWord2VecModel) throws Exception {

        //dataLocalPath = DownloaderUtility.NLPDATA.Download();
        // Gets Path to Text file

        log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new CollectionSentenceIterator(sentences);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        /*
            CustomPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;\\-]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
         */
        t.setTokenPreProcessor(new CustomPreprocessor());

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();
        this.saveWord2VecModel(vec, pathToSerializedWord2VecModel);
        return vec;
        /*log.info("Writing word vectors to text file....");

        // Prints out the closest 10 words to "day". An example on what to do with these Word Vectors.
        log.info("Closest Words:");
        try{
            Collection<String> lst = vec.wordsNearestSum("закон", 10);
            log.info("10 Words closest to 'day': {}", lst);
        } catch (Exception e) {
            log.info("There are no any suggestions");
        }*/
    }

    private void saveWord2VecModel(Word2Vec vec, String pathToSerializedWord2VecModel) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathToSerializedWord2VecModel)))
        {
            oos.writeObject(vec);
            oos.flush();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }



    public static void main(String[] args) throws Exception {

        //dataLocalPath = DownloaderUtility.NLPDATA.Download();
        // Gets Path to Text file
        String filePath = new File("words.txt").getAbsolutePath();

        log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
         */
        t.setTokenPreProcessor(new CustomPreprocessor());

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();

        log.info("Writing word vectors to text file....");

        // Prints out the closest 10 words to "day". An example on what to do with these Word Vectors.
        log.info("Closest Words:");
        try{
            Collection<String> lst = vec.wordsNearestSum("закон", 10);
            log.info("10 Words closest to 'day': {}", lst);
        } catch (Exception e) {
            log.info("There are no any suggestions");
        }




    }
}
