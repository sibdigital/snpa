package ru.p03.snpa.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils<T> {

    public static String[] listStringToMasString(List<String> stringList){
        String[] mas = new String[stringList.size()];
        for(int i=0; i < stringList.size();i++){
            mas[i] = stringList.get(i);
        }
        return mas;
    }

    // Объединяет 2 листа логическим И
    public static List<String> concatTagMas(List<String> firstList, List<String> secondList){
        List<String> resultList = new ArrayList<>();
        for(int i = 0; i < firstList.size(); i++){
            if(haveStringInList(firstList.get(i), secondList))
                resultList.add(firstList.get(i));
        }
        return resultList;

    }

    // удаляет все повторяющиеся значения
    public static List<String> distinctString(List<String> stringList) {
        List<String> stringListResult = new ArrayList<>();

        for(int i=0; i<stringList.size(); i++){
            if(!haveStringInList(stringList.get(i), stringListResult))
                stringListResult.add(stringList.get(i));
        }

        return stringListResult;
    }

    public static boolean haveStringInList(String string, List<String> stringList){
        for(int i=0; i<stringList.size(); i++)
            if(stringList.get(i).equals(string)) return true;
        return false;
    }

    public List<T> masToList(T[] mas){
        List<T> list = new ArrayList<>();
        for(int i=0; i<mas.length; i++) {
            list.add(mas[i]);
        }
        return list;
    }

}
