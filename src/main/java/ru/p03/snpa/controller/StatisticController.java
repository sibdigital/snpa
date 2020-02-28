package ru.p03.snpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.p03.snpa.entity.SearchStatistic;
import ru.p03.snpa.entity.SearchStatisticView;
import ru.p03.snpa.entity.forms.SearchForm;
import ru.p03.snpa.repository.SearchStatisticRepository;
import ru.p03.snpa.repository.SearchStatisticViewRepository;
import ru.p03.snpa.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class StatisticController {

    @Autowired
    private SearchStatisticRepository searchStatisticRepository;
    @Autowired
    private SearchStatisticViewRepository searchStatisticViewRepository;

    @GetMapping("/statistic")
    private String statistic(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm){

        request.setAttribute("searchForm", searchForm);
        request.setAttribute("contextPath", request.getServerName() + ':' + request.getServerPort());

        try {
            if(searchForm != null
                    && searchForm.getSearchDateOfDocumentStart() != null
                    && searchForm.getSearchDateOfDocumentEnd() != null) {
                Iterable<SearchStatisticView> list = searchStatisticViewRepository
                        .findAllBySearchDateTimeAfterAndSearchDateTimeBeforeOrderBySearchDateTimeDesc(
                                DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart()),
                                DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd())
                        );
                request.setAttribute("allStatList", StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList()));
            }
        } catch (ParseException e) {}

        return "statistic";
    }

    @GetMapping("/statisticPivot")
    private String statisticPivot(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm){

        request.setAttribute("searchForm", searchForm);
        request.setAttribute("contextPath", request.getServerName() + ':' + request.getServerPort());

        return "statistic-pivot";
    }
}
