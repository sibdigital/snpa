package ru.p03.snpa.word2vec;

import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;

import java.util.regex.Pattern;

public class CustomPreprocessor implements TokenPreProcess {

    static public class StringCleaning {
        private static final Pattern punctPattern = Pattern.compile("[\\d\\.:,\"'\\(\\)\\[\\]|/?!;\\-]+");

        private StringCleaning() {
        }

        public static String stripPunct(String base) {
            return punctPattern.matcher(base).replaceAll("");
        }
    }


    public CustomPreprocessor() {
    }

    public String preProcess(String token) {
        String s = StringCleaning.stripPunct(token).toLowerCase();
        return s;
    }
}
