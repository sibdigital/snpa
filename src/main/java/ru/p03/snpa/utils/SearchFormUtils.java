package ru.p03.snpa.utils;

import ru.p03.snpa.entity.forms.SearchForm;

public class SearchFormUtils {

    public static boolean isHaveTags(SearchForm searchForm){
        if((searchForm.getSearchTagList()==null)||(searchForm.getSearchTagList().length==0)) {
            return false;
        }
        return true;
    }
}
