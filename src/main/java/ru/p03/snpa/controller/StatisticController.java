package ru.p03.snpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.p03.snpa.entity.ClsAction;
import ru.p03.snpa.entity.ClsLifeSituation;
import ru.p03.snpa.entity.ClsPaymentType;
import ru.p03.snpa.entity.RegSearchStatisticView;
import ru.p03.snpa.entity.forms.SearchForm;
import ru.p03.snpa.repository.*;
import ru.p03.snpa.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class StatisticController {

    @Autowired
    private RegSearchStatisticRepository searchStatisticRepository;
    @Autowired
    private RegSearchStatisticViewRepository searchStatisticViewRepository;

    @Autowired
    private ClsActionRepository clsActionRepository;
    @Autowired
    private ClsLifeSituationRepository clsLifeSituationRepository;
    @Autowired
    private ClsPaymentTypeRepository clsPaymentTypeRepository;

    @GetMapping("/statistic")
    private String statistic(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm){

        request.setAttribute("searchForm", searchForm);
        request.setAttribute("contextPath", request.getServerName() + ':' + request.getServerPort());

        try {
            if(searchForm != null
                    && searchForm.getSearchDateOfDocumentStart() != null
                    && searchForm.getSearchDateOfDocumentEnd() != null) {
                Iterable<RegSearchStatisticView> list = searchStatisticViewRepository
                        .findAllBySearchDateTimeAfterAndSearchDateTimeBeforeOrderBySearchDateTimeDesc(
                                DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart()),
                                DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd())
                        );

                list.forEach(item -> {
                    if(item.getActionTags().length > 0) {
                        List<ClsAction> itemList = new ArrayList<>();
                        for(int i=0; i<item.getActionTags().length; i++){
                            itemList.add(clsActionRepository.findFirstByCode(item.getActionTags()[i]));
                        }
                        item.setActionTagList(itemList);
                    }
                    if(item.getLifeSituationTags().length > 0) {
                        List<ClsLifeSituation> itemList = new ArrayList<>();
                        for(int i=0; i<item.getLifeSituationTags().length; i++){
                            itemList.add(clsLifeSituationRepository.findFirstByCode(item.getLifeSituationTags()[i]));
                        }
                        item.setLifeSituationTagList(itemList);
                    }
                    if(item.getPaymentTypeTags() != null  && item.getPaymentTypeTags().length> 0) {
                        List<ClsPaymentType> itemList = new ArrayList<>();
                        for(int i=0; i<item.getPaymentTypeTags().length; i++){
                            itemList.add(clsPaymentTypeRepository.findFirstByCode(item.getPaymentTypeTags()[i]));
                        }
                        item.setPaymentTypeTagList(itemList);
                    }
                });

                request.setAttribute("allStatList", list);
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
