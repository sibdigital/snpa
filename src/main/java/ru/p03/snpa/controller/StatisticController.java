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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                HashMap<String, Integer> statMap = new HashMap<String, Integer>();
                statMap.put("ALL", 0);
                statMap.put("Q", 0);
                statMap.put("P", 0);
                statMap.put("Z", 0);
                statMap.put("R", 0);
                statMap.put("F", 0);

                HashMap<String, Integer> statTagMap = new HashMap<String, Integer>();
                statTagMap.put("TAG_L", 0);
                statTagMap.put("TAG_P", 0);
                statTagMap.put("TAG_A", 0);

                list.forEach(item -> {
                    if(item.getActionTags().length > 0) {
                        List<ClsAction> itemList = new ArrayList<>();
                        for(int i=0; i<item.getActionTags().length; i++){
                            itemList.add(clsActionRepository.findFirstByCode(item.getActionTags()[i]));
                        }
                        item.setActionTagList(itemList);
                        statTagMap.put("TAG_A", statTagMap.get("TAG_A") + 1);
                    }
                    if(item.getLifeSituationTags().length > 0) {
                        List<ClsLifeSituation> itemList = new ArrayList<>();
                        for(int i=0; i<item.getLifeSituationTags().length; i++){
                            itemList.add(clsLifeSituationRepository.findFirstByCode(item.getLifeSituationTags()[i]));
                        }
                        item.setLifeSituationTagList(itemList);
                        statTagMap.put("TAG_L", statTagMap.get("TAG_L") + 1);
                    }
                    if(item.getPaymentTypeTags() != null  && item.getPaymentTypeTags().length> 0) {
                        List<ClsPaymentType> itemList = new ArrayList<>();
                        for(int i=0; i<item.getPaymentTypeTags().length; i++){
                            itemList.add(clsPaymentTypeRepository.findFirstByCode(item.getPaymentTypeTags()[i]));
                        }
                        item.setPaymentTypeTagList(itemList);
                        statTagMap.put("TAG_P", statTagMap.get("TAG_P") + 1);
                    }

                    // подсчет кол-ва по типам
                    if(item.getSearchType()!=null && !item.getSearchType().isEmpty()) {
                        statMap.put(item.getSearchType(), statMap.get(item.getSearchType()) + 1);
                    }
                });

                Integer statSum = 0, tagSum = 0;

                for(Map.Entry<String, Integer> item : statMap.entrySet()) {
                    statSum += item.getValue();
                }
                for(Map.Entry<String, Integer> item : statTagMap.entrySet()) {
                    tagSum += item.getValue();
                }

                // итоги
                request.setAttribute("statMap", statMap.entrySet());
                request.setAttribute("statTagMap", statTagMap.entrySet());
                request.setAttribute("statSum", statSum);
                request.setAttribute("tagSum", tagSum);

                HashMap<String, String> sTypeMap = new HashMap<>();
                sTypeMap.put("SUM", "Всего");
                sTypeMap.put("ALL", "Все");
                sTypeMap.put("Z", "Федеральные законы");
                sTypeMap.put("R", "НПА");
                sTypeMap.put("P", "Письма");
                sTypeMap.put("Q", "Вопрос-ответ");
                sTypeMap.put("F", "F");
                sTypeMap.put("TAG_A", "Тег действия");
                sTypeMap.put("TAG_P", "Тег виды выплат");
                sTypeMap.put("TAG_L", "Тег жизн.ситуации");

                request.setAttribute("sTypeMap", sTypeMap);
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
