package ru.p03.snpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.p03.snpa.entity.ClsAction;
import ru.p03.snpa.entity.ClsAttributeValue;
import ru.p03.snpa.entity.ClsLifeSituation;
import ru.p03.snpa.entity.ClsPaymentType;
import ru.p03.snpa.entity.forms.SearchForm;
import ru.p03.snpa.entity.forms.TagForm;
import ru.p03.snpa.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SearchController {

    @Autowired
    private ClsActionRepository clsActionRepository;
    @Autowired
    private ClsLifeSituationRepository clsLifeSituationRepository;
    @Autowired
    private ClsPaymentTypeRepository clsPaymentTypeRepository;
    @Autowired
    private RegPractice2Repository regPractice2Repository;
    @Autowired
    private ClsAttributeValueRepository clsAttributeValueRepository;

    private static final Logger log = LoggerFactory.getLogger(SearchRestController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @GetMapping("/search")
    private String searchGet(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm){
        request.setAttribute("searchForm", searchForm);
        request.setAttribute("tagList", getTagFormList());
        request.setAttribute("numbers", getNumbers());

        return "search";
    }

    @GetMapping(value = "/searchByTagAttributeValueName", produces = "application/x-www-form-urlencoded;charset=utf-8")
    private String searchByTagAttributeValueName(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm) {
        if (searchForm.getSearchTagNameList() == null) return "redirect:search";

        List<String> attributeCodeV = new ArrayList<>();
        for (int i = 0; i < searchForm.getSearchTagNameList().length; i++) {
            if(i==0) continue;
            byte[] b = Base64.getDecoder().decode(searchForm.getSearchTagNameList()[i].replaceAll(" ", "+"));
            Optional<ClsAttributeValue> clsAttributeValueOptional = clsAttributeValueRepository.findFirstByName(new String(b, StandardCharsets.UTF_8));
            clsAttributeValueOptional.ifPresent(clsAttributeValue -> attributeCodeV.add("V" + clsAttributeValue.getCode()));
        }

        StringBuilder url = new StringBuilder("search?");
        for (int i = 0; i < attributeCodeV.size(); i++) {
            url.append("searchTagList=").append(attributeCodeV.get(i));
            if (i != attributeCodeV.size() - 1) url.append("&");
        }

        return "redirect:" + url;
    }

    private Map<String, String> getNumbers(){
        Iterable<String> numberIterable = regPractice2Repository.findAllNumbersByDocTypeIn(new String[]{"p", "n"});

        Map<String, String> map = new HashMap<>();
        for(String number : numberIterable){
            map.put(number, "");
        }
        return map;
    }

    // подготовка списка тегов
    private List<TagForm> getTagFormList() {
        List<TagForm> tagFormList = new ArrayList<>();
        Iterable<ClsAction> clsActionIterable = clsActionRepository.findAll();
        Iterable<ClsLifeSituation> clsLifeSituationIterable = clsLifeSituationRepository.findAll();
        Iterable<ClsPaymentType> clsPaymentTypeIterable = clsPaymentTypeRepository.findAll();
        Iterable<ClsAttributeValue> clsAttributeValueIterable = clsAttributeValueRepository.findAll();
        for (ClsAction clsAction : clsActionIterable) {
            tagFormList.add(getTagForm(clsAction));
        }
        for (ClsLifeSituation clsLifeSituation : clsLifeSituationIterable) {
            tagFormList.add(getTagForm(clsLifeSituation));
        }
        for (ClsPaymentType clsPaymentType : clsPaymentTypeIterable) {
            tagFormList.add(getTagForm(clsPaymentType));
        }
        for (ClsAttributeValue clsAttributeValue : clsAttributeValueIterable) {
            tagFormList.add(getTagForm(clsAttributeValue));
        }

        return tagFormList;
    }
    private TagForm getTagForm(ClsAction clsAction){
        TagForm tagForm = new TagForm();
        tagForm.setName(clsAction.getName() + " (Действие)");
        tagForm.setCode("A" + clsAction.getCode());
        tagForm.setType("A");
        tagForm.setParentCode("A" + clsAction.getParentCode());
        return tagForm;
    }
    private TagForm getTagForm(ClsPaymentType clsPaymentType){
        TagForm tagForm = new TagForm();
        tagForm.setName(clsPaymentType.getName() + " (Вид выплаты)");
        tagForm.setCode("P" + clsPaymentType.getCode());
        tagForm.setType("P");
        tagForm.setParentCode("P" + clsPaymentType.getParentCode());
        return tagForm;
    }
    private TagForm getTagForm(ClsLifeSituation clsLifeSituation){
        TagForm tagForm = new TagForm();
        tagForm.setName(clsLifeSituation.getName() + " (Жизненная ситуация)");
        tagForm.setCode("L" + clsLifeSituation.getCode());
        tagForm.setType("L");
        tagForm.setParentCode("L" + clsLifeSituation.getParentCode());
        return tagForm;
    }
    private TagForm getTagForm(ClsAttributeValue clsAttributeValue) {
        TagForm tagForm = new TagForm();
        tagForm.setName(clsAttributeValue.getName() + " (Атрибут КС)");
        tagForm.setCode("V" + clsAttributeValue.getCode());
        tagForm.setType("V");
        return tagForm;
    }

}
