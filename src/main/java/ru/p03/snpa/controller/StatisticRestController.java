package ru.p03.snpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.p03.snpa.entity.RegSearchStatistic;
import ru.p03.snpa.repository.RegSearchStatisticRepository;
import ru.p03.snpa.utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class StatisticRestController {

    @Autowired
    private RegSearchStatisticRepository searchStatisticRepository;

    @GetMapping(value = "/statistic/search")
    public List<RegSearchStatistic> getStat(@RequestParam("searchDateOfDocumentStart") String dateStart,
                                            @RequestParam("searchDateOfDocumentEnd") String dateEnd){
        try {
            Iterable<RegSearchStatistic> list = searchStatisticRepository
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
