package ru.p03.snpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.p03.snpa.entity.*;
import ru.p03.snpa.entity.forms.RelatedDocument;
import ru.p03.snpa.entity.forms.TagForm;
import ru.p03.snpa.repository.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class DocPageController {
    @Autowired
    RegPracticeRepository regPracticeRepository;
    @Autowired
    ClsActionRepository clsActionRepository;
    @Autowired
    ClsPaymentTypeRepository clsPaymentTypeRepository;
    @Autowired
    ClsLifeSituationRepository clsLifeSituationRepository;
    @Autowired
    RegPracticeAttributeRepository regPracticeAttributeRepository;
    @Autowired
    ClsPracticePracticeRepository clsPracticePracticeRepository;
    @Autowired
    ClsAttributeValueRepository clsAttributeValueRepository;
    public static final int BUFFER_SIZE = 4096;

    @GetMapping("/docPage")
    public String docPage(HttpServletRequest request, @ModelAttribute("id") Long id) {

        Optional<RegPractice> regPracticeOptional = regPracticeRepository.findById(id);
        if (regPracticeOptional.isPresent()) {

            Optional<RegPractice> regPracticeParentOptional
                    = regPracticeRepository.findFirstByCodeAndDocType(regPracticeOptional.get().getCodeParent(), regPracticeOptional.get().getParentDocType());

            // если есть родитель
            if (regPracticeParentOptional.isPresent()) {
                request.setAttribute("idParent", regPracticeParentOptional.get().getId());
                if (regPracticeParentOptional.get().getDateEnd() != null)
                    if (regPracticeParentOptional.get().getDateEnd().before(new Date()))
                        request.setAttribute("expiredDate", regPracticeParentOptional.get().getDateEnd());
            }

            // ищем все связанные документы
            Iterable<ClsPracticePractice> clsPracticePracticeIterable12 = clsPracticePracticeRepository.findAllByPractice1Code(regPracticeOptional.get().getCode());
            request.setAttribute("relatedDocumentList12", getRelatedDocuments(clsPracticePracticeIterable12, "1to2"));
            Iterable<ClsPracticePractice> clsPracticePracticeIterable21 = clsPracticePracticeRepository.findAllByPractice2Code(regPracticeOptional.get().getCode());
            request.setAttribute("relatedDocumentList21", getRelatedDocuments(clsPracticePracticeIterable21, "2to1"));

            // ищем всех потомков и сортируем по позиции
            request.setAttribute("regPracticeChildrenIterable", getMyChild(regPracticeOptional.get()));

            request.setAttribute("regPractice", regPracticeOptional.get());
            request.setAttribute("tagFormList", getTagFormList(regPracticeOptional.get()));
        }

        return "doc-page";
    }

    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, @ModelAttribute("id") Long id, @ModelAttribute("number") Integer number) throws IOException {
        Optional<RegPractice> regPracticeOptional = regPracticeRepository.findById(id);
        String repName;
        if (regPracticeOptional.isPresent())
            repName = regPracticeOptional.get().getFiles()[number - 1];
        else
            return;

        // construct the complete absolute path of the file
        String fullPath = "\\\\10.3.30.151\\UsersOtd\\SNPA\\" + repName;
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);
        response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // get output stream of the response
        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();
    }


    private List<RelatedDocument> getRelatedDocuments(Iterable<ClsPracticePractice> clsPracticePracticeIterable, String type) {
        List<RelatedDocument> relatedDocumentList = new ArrayList<>();

        for (ClsPracticePractice clsPracticePractice : clsPracticePracticeIterable) {
            RelatedDocument relatedDocument = new RelatedDocument();
            relatedDocument.setType(clsPracticePractice.getType());

            if (type.equals("1to2")) {
                relatedDocument.setRegPractice(regPracticeRepository.findFirstByCode(clsPracticePractice.getPractice2Code()).get());
            }
            if (type.equals("2to1")) {
                relatedDocument.setRegPractice(regPracticeRepository.findFirstByCode(clsPracticePractice.getPractice1Code()).get());
            }

            relatedDocumentList.add(relatedDocument);
        }

        return relatedDocumentList;
    }

    private List<TagForm> getTagFormList(RegPractice regPractice) {
        List<TagForm> tagFormList = new ArrayList<>();
        Iterable<RegPracticeAttribute> regPracticeAttributeAIterable
                = regPracticeAttributeRepository.findAllByCodePracticeAndAttributeType(regPractice.getCode(), 2);
        Iterable<RegPracticeAttribute> regPracticeAttributeLIterable
                = regPracticeAttributeRepository.findAllByCodePracticeAndAttributeType(regPractice.getCode(), 3);
        Iterable<RegPracticeAttribute> regPracticeAttributePIterable
                = regPracticeAttributeRepository.findAllByCodePracticeAndAttributeType(regPractice.getCode(), 1);
        Iterable<RegPracticeAttribute> regPracticeAttributeVIterable
                = regPracticeAttributeRepository.findAllByCodePracticeAndAttributeType(regPractice.getCode(), 4);

        String[] attributeCodeA = getCodeAttributeFrom(regPracticeAttributeAIterable);
        String[] attributeCodeL = getCodeAttributeFrom(regPracticeAttributeLIterable);
        String[] attributeCodeP = getCodeAttributeFrom(regPracticeAttributePIterable);
        String[] attributeCodeV = getCodeAttributeFrom(regPracticeAttributeVIterable);

        Iterable<ClsAction> clsActionIterable = clsActionRepository.findAllByCodeIn(attributeCodeA);
        Iterable<ClsLifeSituation> clsLifeSituationIterable = clsLifeSituationRepository.findAllByCodeIn(attributeCodeL);
        Iterable<ClsPaymentType> clsPaymentTypeIterable = clsPaymentTypeRepository.findAllByCodeIn(attributeCodeP);
        Iterable<ClsAttributeValue> clsAttributeValueIterable = clsAttributeValueRepository.findAllByCodeIn(attributeCodeV);

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

    private TagForm getTagForm(ClsAction clsAction) {
        TagForm tagForm = new TagForm();
        tagForm.setName(clsAction.getName());
        tagForm.setCode(clsAction.getCode());
        tagForm.setType("A");
        return tagForm;
    }

    private TagForm getTagForm(ClsPaymentType clsPaymentType) {
        TagForm tagForm = new TagForm();
        tagForm.setName(clsPaymentType.getName());
        tagForm.setCode(clsPaymentType.getCode());
        tagForm.setType("P");
        return tagForm;
    }

    private TagForm getTagForm(ClsLifeSituation clsLifeSituation) {
        TagForm tagForm = new TagForm();
        tagForm.setName(clsLifeSituation.getName());
        tagForm.setCode(clsLifeSituation.getCode());
        tagForm.setType("L");
        return tagForm;
    }

    private TagForm getTagForm(ClsAttributeValue clsAttributeValue) {
        TagForm tagForm = new TagForm();
        tagForm.setName(clsAttributeValue.getName());
        tagForm.setCode(clsAttributeValue.getCode());
        tagForm.setType("V");
        return tagForm;
    }

    private String[] getCodeAttributeFrom(Iterable<RegPracticeAttribute> regPracticeAttributeIterable) {
        int size = 0;
        for(RegPracticeAttribute regPracticeAttribute : regPracticeAttributeIterable){
            size++;
        }
        String[] codeAttribute = new String[size];

        int i = 0;
        for (RegPracticeAttribute regPracticeAttribute : regPracticeAttributeIterable) {
            codeAttribute[i] = regPracticeAttribute.getCodeAttribute();
            i++;
        }

        return codeAttribute;
    }

    private Iterable<RegPractice> getMyChild(RegPractice regPractice) {
        Page<RegPractice> regPracticeChildrenPage = null;

        if (regPractice.getDocType().equals("n"))
            regPracticeChildrenPage = regPracticeRepository.findAllByCodeParentAndDocType(
                    PageRequest.of(0, 100500, Sort.by(Sort.Direction.ASC, "posNum")),
                    regPractice.getCode(),
                    "n");

        if (regPractice.getDocType().equals("p"))
            regPracticeChildrenPage = regPracticeRepository.findAllByCodeParentAndDocType(
                    PageRequest.of(0, 100500, Sort.by(Sort.Direction.ASC, "posNum")),
                    regPractice.getCode()
                    , "p");

        if (regPractice.getDocType().equals("z"))
            regPracticeChildrenPage = regPracticeRepository.findAllByCodeParentAndDocType(
                    PageRequest.of(0, 100500, Sort.by(Sort.Direction.ASC, "posNum")),
                    regPractice.getCode()
                    , "z");

        if (regPracticeChildrenPage != null)
            return regPracticeChildrenPage.getContent();


        return new ArrayList<>();
    }
}
