package ru.p03.snpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.p03.snpa.entity.RegPractice;
import ru.p03.snpa.entity.SearchStatistic;
import ru.p03.snpa.entity.forms.SearchForm;
import ru.p03.snpa.repository.RegPracticeRepository;
import ru.p03.snpa.repository.SearchStatisticRepository;
import ru.p03.snpa.utils.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class StatisticRestController {

    @Autowired
    private SearchStatisticRepository searchStatisticRepository;

    @GetMapping(value = "/statistic/search")
    public List<SearchStatistic> getStat(@RequestParam("searchDateOfDocumentStart") String dateStart,
                                         @RequestParam("searchDateOfDocumentEnd") String dateEnd){
        try {
            Iterable<SearchStatistic> list = searchStatisticRepository
                .findAllBySearchDateTimeAfterAndSearchDateTimeBeforeOrderBySearchDateTimeDesc(
                        DateUtils.getDateFromString(dateStart),
                        DateUtils.getDateFromString(dateEnd)
//                        DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart()),
//                        DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd())
                );
            return StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        //} catch (ParseException e){
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
