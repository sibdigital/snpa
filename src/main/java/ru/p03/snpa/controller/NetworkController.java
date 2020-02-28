package ru.p03.snpa.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.p03.snpa.entity.*;
import ru.p03.snpa.entity.forms.SearchForm;
import ru.p03.snpa.entity.from1c.LifeSituation;
import ru.p03.snpa.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/network")
public class NetworkController {
    @Autowired
    private ClsActionRepository clsActionRepository;
    @Autowired
    private ClsLifeSituationRepository clsLifeSituationRepository;
    @Autowired
    private ClsPaymentTypeRepository clsPaymentTypeRepository;
    @Autowired
    private RegPractice2Repository regPractice2Repository;
    @Autowired
    private RegPracticeAttributeRepository regPracticeAttributeRepository;
    @Autowired
    private ClsAttributeValueRepository clsAttributeValueRepository;
    @Autowired
    private ClsPracticePracticeRepository clsPracticePracticeRepository;
    @Autowired
    private Gson gson;
    @GetMapping
    public String main(Model model, HttpServletRequest request, @ModelAttribute("id") Long id){
        Optional<RegPractice> regPractice = regPractice2Repository.findById(id);
//        Optional<RegPractice> regPractice = regPractice2Repository.findByCode("z000000610");
        regPractice.ifPresent(practice -> {
            model.addAttribute("regPractice", practice.getName());
            Iterable<ClsPracticePractice> clsPracticePracticeChildren = clsPracticePracticeRepository.findAllByPractice1Code(practice.getCode());
            Iterable<ClsPracticePractice> clsPracticePracticeParents = clsPracticePracticeRepository.findAllByPractice2Code(practice.getCode());
            Iterable<RegPracticeAttribute> regPracticeAttributes = regPracticeAttributeRepository.findAllByCodePractice(practice.getCode());
            Map<String, String> children = new HashMap<>();
            Map<String, String> parents = new HashMap<>();
            Map<String, String> attributes = new HashMap<>();

            clsPracticePracticeChildren.forEach(element -> {
                String code = element.getPractice2Code();
                Optional<RegPractice> regPracticeChild = regPractice2Repository.findByCode(code);
                regPracticeChild.ifPresent(childPractice -> children.put(childPractice.getName(), element.getType()));
            });

            clsPracticePracticeParents.forEach(element -> {
                String code = element.getPractice1Code();
                Optional<RegPractice> regPracticeParent = regPractice2Repository.findByCode(code);
                regPracticeParent.ifPresent(parentPractice -> parents.put(parentPractice.getName(), element.getType()));
            });

            regPracticeAttributes.forEach(element-> {
                String code = element.getCodeAttribute();
//                payment = 1
//                action = 2
//                situation = 3
//                value = 4
                switch (element.getAttributeType()){
                    case 1:
                        Optional<ClsPaymentType> attributeTypeP = clsPaymentTypeRepository.findByCode(element.getCodeAttribute());
                        attributeTypeP.ifPresent(attribute -> attributes.put(attribute.getName(), "Тип выплаты"));
                        break;
                    case 2:
                        Optional<ClsAction> attributeTypeA = clsActionRepository.findByCode(element.getCodeAttribute());
                        attributeTypeA.ifPresent(attribute -> attributes.put(attribute.getName(), "Действие"));
                        break;
                    case 3:
                        Optional<ClsLifeSituation> attributeTypeL = clsLifeSituationRepository.findByCode(element.getCodeAttribute());
                        attributeTypeL.ifPresent(attribute -> attributes.put(attribute.getName(), "Жизненная ситуация"));
                        break;
                    case 4:
                        Optional<ClsAttributeValue> attributeTypeV = clsAttributeValueRepository.findByCode(element.getCodeAttribute());
                        attributeTypeV.ifPresent(attribute -> attributes.put(attribute.getName(), "Значение"));
                        break;
                }
//                Optional<RegPractice> regPracticeParent = regPractice2Repository.findByCode(code);

            });

            request.setAttribute("regPracticeChildren", children);
            request.setAttribute("regPracticeParents", parents);
            request.setAttribute("regPracticeAttributes", attributes);
        });
        return "network";
    }
}
