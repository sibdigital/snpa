package ru.p03.snpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.p03.snpa.entity.*;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.forms.ResultForm;
import ru.p03.snpa.entity.forms.SearchForm;
import ru.p03.snpa.entity.forms.TagForm;
import ru.p03.snpa.repository.*;
import ru.p03.snpa.utils.DateUtils;
import ru.p03.snpa.utils.ListUtils;
import ru.p03.snpa.word2vec.Word2VecModelInitializer;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static ru.p03.snpa.utils.ListUtils.*;

/*
* Идея такая:
* 1     по тегам подготавливается массив codePractice
* 1.1   приходит массив тегов [tag1, tag2, tag3, ... ,tagN]
* 1.2   составляются массивы потомков тэгов {[tag11, tag12, tag121, tag122, tag123], [tag21, tag22 ...], ... ,[tagN1, tagN2, ...]}
* 1.3   находим все codePractice (tag1 OR [tag11, tag12, tag121, tag122, tag123]) AND (tag2 OR [tag21, tag22 ...]) AND ... AND (tagN OR [tagN1, tagN2, ...])
* 1.4   удаляем повторяющиеся codePractice (хз, можно наверное distinct сделать)
* 2     по полученному массиву запускается полнотекстовый поиск
* */

@RestController
public class SearchRestController {
    @Autowired
    private RegPracticeRepository regPracticeRepository;
    @Autowired
    private RegPracticeAttributeRepository regPracticeAttributeRepository;
    @Autowired
    private ClsActionRepository clsActionRepository;
    @Autowired
    private ClsLifeSituationRepository clsLifeSituationRepository;
    @Autowired
    private ClsPaymentTypeRepository clsPaymentTypeRepository;
    @Autowired
    private RegPractice2Repository regPractice2Repository;
    @Autowired
    private RegSearchStatisticRepository regSearchStatisticRepository;
    @Autowired
    private ClsAttributeValueRepository clsAttributeValueRepository;

    @Autowired
    private TagFormRepository tagFormRepository;

    @Autowired
    private ClsQuestionRepository clsQuestionRepository;


    private static final Logger log = LoggerFactory.getLogger(SearchRestController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @PostMapping("/getTagFormList")
    public List<TagForm> getTagFormList(@RequestBody RegPractice regPractice) {
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
        for(RegPracticeAttribute regPracticeAttribute : regPracticeAttributeIterable) {
            size++;
        }
        String[] codeAttribute = new String[size];

        int i = 0;
        for(RegPracticeAttribute regPracticeAttribute : regPracticeAttributeIterable) {
            codeAttribute[i] = regPracticeAttribute.getCodeAttribute();
            i++;
        }

        return codeAttribute;
    }

    @PostMapping("/search")
    public ResultForm search(@RequestBody SearchForm searchForm) {
        ResultForm resultForm = new ResultForm();
        resultForm.setSuccess(true);
        long start = System.currentTimeMillis();
        log.info(dateFormat.format(new Date()) + ": start search");
        log.info(searchForm.toString());

        try {
            Iterable<RegPractice> regPracticeIterable = null;
            // if 0 search settings
            if ((searchForm.getSearchTagList().length == 0)
                    &&(searchForm.getSearchType().equals("ALL"))
                    &&(searchForm.getSearchRelevance().equals("ALL"))
                    &&(searchForm.getSearchDateOfDocumentStart().equals(""))
                    &&(searchForm.getSearchDateOfDocumentEnd().equals(""))
                    &&(searchForm.getSearchText().equals("")))
            return resultForm;

            //предварительный поиск по вопросам и получение из подходящих вопросов тегов
            if (!searchForm.getSearchText().equals("")) {
                Iterable<ClsQuestion> clsQuestions = clsQuestionRepository.findAllByContentAndKeywords(searchForm.getSearchText());
                final Collection<String> suggestionsForWord = Word2VecModelInitializer.getInitedWord2VecAdvisor()
                        .getSuggestionsForWord("возраст", 3);
                List<String> tagList = new ArrayList<String>();
                tagList.addAll(Arrays.asList(searchForm.getSearchTagList()));
               // clsQuestions.forEach(clsQuestion -> tagList.add('Q' + clsQuestion.getCode()));
                if (clsQuestions.iterator().hasNext() ){
                    tagList.add('Q' + clsQuestions.iterator().next().getCode());
                    searchForm.setSearchTagList( tagList.toArray(new String[tagList.size()]) );
                }
            }

            // search start
            if (searchForm.getSearchTagList().length != 0) {
                log.info(dateFormat.format(new Date()) + ": start filterByTagsAndSearchType");
                regPracticeIterable = filterByTagsAndSearchType(searchForm.getSearchTagList(), searchForm.getSearchType());
                log.info(dateFormat.format(new Date()) + ": end filterByTagsAndSearchType");

                if (!searchForm.getSearchRelevance().equals("ALL")) {
                    log.info(dateFormat.format(new Date()) + ": start filterBySearchRelevance");
                    regPracticeIterable = filterBySearchRelevance(regPracticeIterable, searchForm.getSearchRelevance());
                    log.info(dateFormat.format(new Date()) + ": end filterBySearchRelevance");
                }

                if ((!searchForm.getSearchDateOfDocumentStart().equals("")) || (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                    log.info(dateFormat.format(new Date()) + ": start filterBySearchDateOfDocument");
                    regPracticeIterable = filterBySearchDateOfDocument(regPracticeIterable,
                            searchForm.getSearchDateOfDocumentStart(), searchForm.getSearchDateOfDocumentEnd());
                    log.info(dateFormat.format(new Date()) + ": end filterBySearchDateOfDocument");
                }
            } else {
                // если searchText не пустой
                if (!searchForm.getSearchText().equals("")) {
                    // если вдруг нет фильтров
                    if ((searchForm.getSearchType().equals("ALL"))
                            &&(searchForm.getSearchRelevance().equals("ALL"))
                            &&(searchForm.getSearchDateOfDocumentStart().equals(""))
                            &&(searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                        log.info(dateFormat.format(new Date()) + ": start filterBySearchText");
                        if (searchForm.getSearchText().equals("sortByDate"))
                            resultForm.setRegPractice2Iterable(regPractice2Repository.findAllByFullTextSearchOrderByDateOfDocument(searchForm.getSearchText()));
                        else
                            resultForm.setRegPractice2Iterable(regPractice2Repository.findAllByFullTextSearchOrderByRelevance(searchForm.getSearchText()));

                        long l = System.currentTimeMillis() - start;
                        resultForm.setTime(String.valueOf(l));
                        log.info(dateFormat.format(new Date()) + ": end filterBySearchText");
                        saveRegSearchStatistic(searchForm, resultForm);
                        return resultForm;
                    }

                    // если указан тип поиска
                    if ((!searchForm.getSearchType().equals("ALL"))
                            &&(searchForm.getSearchRelevance().equals("ALL"))
                            &&(searchForm.getSearchDateOfDocumentStart().equals(""))
                            &&(searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                        log.info(dateFormat.format(new Date()) + ": start filterBySearchTextAndSearchType");
                        if (searchForm.getSearchType().equals("P")) {
                            if (searchForm.getSearchText().equals("sortByDate"))
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllByFullTextSearchAndDocTypeOrderByDateOfDocument(searchForm.getSearchText(), "p"));
                            else
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllByFullTextSearchAndDocTypeOrderByRelevance(searchForm.getSearchText(), "p"));
                        }
                        if (searchForm.getSearchType().equals("R")) {
                            if (searchForm.getSearchText().equals("sortByDate"))
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllByFullTextSearchAndDocTypeOrderByDateOfDocument(searchForm.getSearchText(), "n"));
                            else
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllByFullTextSearchAndDocTypeOrderByRelevance(searchForm.getSearchText(), "n"));
                        }
                        if (searchForm.getSearchType().equals("Z")) {
                            if (searchForm.getSearchText().equals("sortByDate"))
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllByFullTextSearchAndDocTypeOrderByDateOfDocument(searchForm.getSearchText(), "z"));
                            else
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllByFullTextSearchAndDocTypeOrderByRelevance(searchForm.getSearchText(), "z"));
                        }
                        resultForm.setTime(String.valueOf(System.currentTimeMillis() - start));
                        log.info(dateFormat.format(new Date()) + ": end filterBySearchTextAndSearchType");
                        saveRegSearchStatistic(searchForm, resultForm);
                        return resultForm;
                    }

                    // если указана актуальность
                    if ((searchForm.getSearchType().equals("ALL"))
                            &&(!searchForm.getSearchRelevance().equals("ALL"))
                            &&(searchForm.getSearchDateOfDocumentStart().equals(""))
                            &&(searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                        log.info(dateFormat.format(new Date()) + ": start filterBySearchTextAndSearchRelevance");
                        if (searchForm.getSearchRelevance().equals("VALID")) {
                            if (searchForm.getSearchText().equals("sortByDate"))
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllValidByFullTextSearchOrderByDateOfDocument(searchForm.getSearchText(), new Date()));
                            else
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllValidByFullTextSearchOrderByRelevance(searchForm.getSearchText(), new Date()));
                        }
                        if (searchForm.getSearchRelevance().equals("EXPIRED")) {
                            if (searchForm.getSearchText().equals("sortByDate"))
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllExpiredByFullTextSearchOrderByDateOfDocument(searchForm.getSearchText(), new Date()));
                            else
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllExpiredByFullTextSearchOrderByRelevance(searchForm.getSearchText(), new Date()));
                        }
                        if (searchForm.getSearchRelevance().equals("INVALID")) {
                            if (searchForm.getSearchText().equals("sortByDate"))
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllInvalidByFullTextSearchOrderByDateOfDocument(searchForm.getSearchText(), new Date()));
                            else
                                resultForm.setRegPractice2Iterable(regPractice2Repository
                                        .findAllInvalidByFullTextSearchOrderByRelevance(searchForm.getSearchText(), new Date()));
                        }
                        resultForm.setTime(String.valueOf(System.currentTimeMillis() - start));
                        log.info(dateFormat.format(new Date()) + ": end filterBySearchTextAndSearchRelevance");
                        saveRegSearchStatistic(searchForm, resultForm);
                        return resultForm;
                    }
                }

                if (searchForm.getSearchType().equals("ALL")) {
                    regPracticeIterable = filterSearchTypeAllBySearchRelevanceAndDateOfDocument(searchForm);
                }
                if (searchForm.getSearchType().equals("P")) {
                    regPracticeIterable = filterSearchTypePBySearchRelevanceAndDateOfDocument(searchForm);
                }
                if (searchForm.getSearchType().equals("R")) {
                    regPracticeIterable = filterSearchTypeRBySearchRelevanceAndDateOfDocument(searchForm);
                }
                if (searchForm.getSearchType().equals("Z")) {
                    regPracticeIterable = filterSearchTypeZBySearchRelevanceAndDateOfDocument(searchForm);
                }

            }

            // end search findByText
            if (!searchForm.getSearchText().equals("")) {
                log.info(dateFormat.format(new Date()) + ": start filterBySearchTextCodeIn");

                if (searchForm.getSearchText().equals("sortByDate"))
                    resultForm.setRegPractice2Iterable(regPractice2Repository
                            .findAllByFullTextSearchAndCodeInOrderByDateOfDocument(searchForm.getSearchText(),
                            ListUtils.listStringToMasString(getPracticeCodeFromRegPracticeIterable(regPracticeIterable))));
                else
                    resultForm.setRegPractice2Iterable(regPractice2Repository
                            .findAllByFullTextSearchAndCodeInOrderByRelevance(searchForm.getSearchText(),
                            ListUtils.listStringToMasString(getPracticeCodeFromRegPracticeIterable(regPracticeIterable))));

                resultForm.setTime(String.valueOf(System.currentTimeMillis() - start));
                log.info(dateFormat.format(new Date()) + ": end filterBySearchTextCodeIn");
                saveRegSearchStatistic(searchForm, resultForm);
                return resultForm;
            }

            resultForm.setRegPractice2Iterable(regPracticeIterable);
            resultForm.setTime(String.valueOf(System.currentTimeMillis() - start));
            saveRegSearchStatistic(searchForm, resultForm);
            return resultForm;

        } catch (Exception e) {
            resultForm.setSuccess(true);
            resultForm.setData(e.toString());
            System.out.println(e.toString());
            return resultForm;
        }
    }

    private void saveRegSearchStatistic(SearchForm searchForm, ResultForm resultForm) {
        try {
            RegSearchStatistic regSearchStatistic = searchFormToSearchStatistic(searchForm);
            regSearchStatistic.setResults(ListUtils.listStringToMasString(getPracticeCodeFromRegPracticeIterable(resultForm.getRegPractice2Iterable())));
            regSearchStatistic.setResultsCount(regSearchStatistic.getResults().length);
            regSearchStatistic.setProceedTime(Long.valueOf(resultForm.getTime()));
            regSearchStatisticRepository.save(regSearchStatistic);
            //
            resultForm.setSearchId(regSearchStatistic.getId());
        } catch (Exception e){
            log.error("saveRegSearchStatistic Error: " + e.toString());
        }
    }

    @PostMapping("/updateSearchStatistic")
    public String updateSearchStatistic(@RequestBody SearchForm searchForm) {
        try {
            Optional<RegSearchStatistic> regSearchStatisticOptional = regSearchStatisticRepository.findById(searchForm.getSearchId());
            if (regSearchStatisticOptional.isPresent()) {
                regSearchStatisticOptional.get().setStatus(searchForm.getStatus());
                if (searchForm.getComment() != null && !searchForm.getComment().isEmpty()) {
                    regSearchStatisticOptional.get().setComment(searchForm.getComment());
                }
                regSearchStatisticRepository.save(regSearchStatisticOptional.get());
            }
            return "OK";
        } catch (Exception e) {
            log.error("updateSearchStatistic Error: " + e.toString());
            return "ERROR";
        }
    }

    @PostMapping("/getTopTagsByPractices")
    public ResponseForm getTopTagsByPractices(@RequestBody SearchForm searchForm) {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            String[] idPractices = searchForm.getIdPractices();
            Iterable<TagForm> tagFormIterable = tagFormRepository.findTopTags(idPractices);
            responseForm.setObject(tagFormIterable);
            return responseForm;
        } catch (Exception e) {
            responseForm.setSuccess(false);
            responseForm.setData(e.getLocalizedMessage());
            return responseForm;
        }
    }


    // делает как написано в шапке (логическое И)
    // возвращает список Practice с учётом тегов и типа поиска(ALL, P, R)
    private Iterable<RegPractice> filterByTagsAndSearchType(String[] tags, String searchType) {
        // сначала для первого тега вытаскиваем список codePractice
        Iterable<RegPracticeAttribute> regPracticeAttributeIterable
                = getRegPracticeAttributeByTypeAndMasCodeAndSearchType(tags[0], getAllMyChild(tags[0].substring(1), tags[0].charAt(0)), searchType);
        List<String> codePracticeList = getPracticeCode(regPracticeAttributeIterable);

        // если теги ещё есть
        for (int i = 1; i < tags.length; i++){
            regPracticeAttributeIterable
                    = getRegPracticeAttributeByTypeAndMasCodeAndSearchType(tags[i], getAllMyChild(tags[i].substring(1), tags[i].charAt(0)), searchType);
            codePracticeList = concatTagMas(codePracticeList, getPracticeCode(regPracticeAttributeIterable));
            if (codePracticeList.size() == 0)
                return new ArrayList<>();
        }
        String[] codes = listStringToMasString(distinctString(codePracticeList));
        return regPracticeRepository.findAllByCodeInOrderByDateOfDocumentDesc(codes);
        //return regPracticeRepository.findAllByCodeInOrderByDateOfDocumentDesc(listStringToMasString(distinctString(codePracticeList)));
    }
    
    private List<RegPractice> filterBySearchRelevance(Iterable<RegPractice> regPracticeIterable, String searchRelevance) {
        List<RegPractice> regPracticeList = new ArrayList<>();

        Date date = new Date();
        if (searchRelevance.equals("ALL"))
            return (List<RegPractice>) regPracticeIterable;

        //Действующие
        if (searchRelevance.equals("VALID"))
            for (RegPractice regPractice : regPracticeIterable) {
                if ((regPractice.getDateStart() != null) || (regPractice.getDateEnd() != null)) {
                    // если обе даты != null
                    if ((regPractice.getDateStart() != null) && (regPractice.getDateEnd() != null)) {
                        if ((regPractice.getDateStart().before(date)) && (regPractice.getDateEnd().after(date))) {
                            regPracticeList.add(regPractice);
                        }
                    } else {
                        // если одна из дат != null
                        if (regPractice.getDateStart() != null) {
                            if (regPractice.getDateStart().before(date))
                                regPracticeList.add(regPractice);
                        }

                        if (regPractice.getDateEnd() != null) {
                            if (regPractice.getDateEnd().after(date))
                                regPracticeList.add(regPractice);
                        }
                    }
                } else {
                    // если обе даты == null
                    regPracticeList.add(regPractice);
                }
            }

        //Утратившие силу
        if (searchRelevance.equals("EXPIRED"))
            for (RegPractice regPractice : regPracticeIterable) {
                if (regPractice.getDateEnd() != null)
                    if (regPractice.getDateEnd().before(date))
                        regPracticeList.add(regPractice);
            }

        //Не вступившие в силу
        if (searchRelevance.equals("INVALID"))
            for (RegPractice regPractice : regPracticeIterable) {
                if (regPractice.getDateStart() != null)
                    if (regPractice.getDateStart().after(date))
                        regPracticeList.add(regPractice);
            }

        return regPracticeList;
    }
    
    private List<RegPractice> filterBySearchDateOfDocument(Iterable<RegPractice> regPracticeIterable, String searchDateOfDocumentStart,
                                                           String searchDateOfDocumentEnd) throws ParseException {
        List<RegPractice> regPracticeList = new ArrayList<>();

        Date dateOfDocumentStart;
        Date dateOfDocumentEnd;

        if ((!searchDateOfDocumentStart.equals("")) && (!searchDateOfDocumentEnd.equals(""))) {
            dateOfDocumentStart = DateUtils.getDateFromString(searchDateOfDocumentStart);
            dateOfDocumentEnd = DateUtils.getDateFromString(searchDateOfDocumentEnd);
            for (RegPractice regPractice : regPracticeIterable) {
                if ((regPractice.getDateOfDocument() == null)
                        ||(regPractice.getDateOfDocument().after(dateOfDocumentStart))
                        &&(regPractice.getDateOfDocument().before(dateOfDocumentEnd))) {
                    regPracticeList.add(regPractice);
                }
            }
            return regPracticeList;
        }

        if ((!searchDateOfDocumentStart.equals(""))) {
            dateOfDocumentStart = DateUtils.getDateFromString(searchDateOfDocumentStart);
            for (RegPractice regPractice : regPracticeIterable) {
                if ((regPractice.getDateOfDocument() == null) || (regPractice.getDateOfDocument().after(dateOfDocumentStart))) {
                    regPracticeList.add(regPractice);
                }
            }
            return regPracticeList;
        }

        if ((!searchDateOfDocumentEnd.equals(""))) {
            dateOfDocumentEnd = DateUtils.getDateFromString(searchDateOfDocumentEnd);
            for (RegPractice regPractice : regPracticeIterable) {
                if ((regPractice.getDateOfDocument() == null) || (regPractice.getDateOfDocument().before(dateOfDocumentEnd))) {
                    regPracticeList.add(regPractice);
                }
            }
            return regPracticeList;
        }

        return regPracticeList;
    }

    private Iterable<RegPractice> filterSearchTypeAllBySearchRelevanceAndDateOfDocument(SearchForm searchForm) throws ParseException {
        Date dateOfDocumentStart;
        Date dateOfDocumentEnd;

        if (searchForm.getSearchRelevance().equals("ALL")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllByDateOfDocumentAfterAndDateOfDocumentBeforeOrderByDateOfDocumentDesc(dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllByDateOfDocumentAfterOrderByDateOfDocumentDesc(dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllByDateOfDocumentBeforeOrderByDateOfDocumentDesc(dateOfDocumentEnd);
            }
        }

        //Действующие
        if (searchForm.getSearchRelevance().equals("VALID")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBefore(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllValidAndDateOfDocumentAfter(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllValidAndDateOfDocumentBefore(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllValid(new Date());
            }
        }

        //Утратившие силу
        if (searchForm.getSearchRelevance().equals("EXPIRED")) {

            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBefore(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentAfter(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentBefore(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllExpired(new Date());
            }
        }

        //Не вступившие в силу
        if (searchForm.getSearchRelevance().equals("INVALID")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBefore(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentAfter(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentBefore(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllInvalid(new Date());
            }
        }

        return new ArrayList<>();
    }
    
    private Iterable<RegPractice> filterSearchTypePBySearchRelevanceAndDateOfDocument(SearchForm searchForm) throws ParseException {
        Date dateOfDocumentStart;
        Date dateOfDocumentEnd;

        if (searchForm.getSearchRelevance().equals("ALL")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllByDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(dateOfDocumentStart, dateOfDocumentEnd, "p");
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllByDateOfDocumentAfterAndDocTypeOrderByDateOfDocumentDesc(dateOfDocumentStart, "p");
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllByDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(dateOfDocumentEnd, "p");
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllByDocTypeOrderByDateOfDocumentDesc("p");
            }
        }

        //Действующие
        if (searchForm.getSearchRelevance().equals("VALID")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllValidAndDateOfDocumentAfterAndDocTypeP(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllValidAndDateOfDocumentBeforeAndDocTypeP(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                System.out.println("Вот тута будет");
                return regPracticeRepository.findAllValidAndDocTypeP(new Date());
            }
        }

        //Утратившие силу
        if (searchForm.getSearchRelevance().equals("EXPIRED")) {

            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP(new Date(), dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentAfterAndDocTypeP(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentBeforeAndDocTypeP(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllExpiredAndDocTypeP(new Date());
            }
        }

        //Не вступившие в силу
        if (searchForm.getSearchRelevance().equals("INVALID")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeP(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentAfterAndDocTypeP(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentBeforeAndDocTypeP(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllInvalidAndDocTypeP(new Date());
            }
        }
        return new ArrayList<>();

    }

    private Iterable<RegPractice> filterSearchTypeRBySearchRelevanceAndDateOfDocument(SearchForm searchForm) throws ParseException {
        Date dateOfDocumentStart;
        Date dateOfDocumentEnd;

        if (searchForm.getSearchRelevance().equals("ALL")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllByDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(
                        dateOfDocumentStart, dateOfDocumentEnd, "n");
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllByDateOfDocumentAfterAndDocTypeOrderByDateOfDocumentDesc(dateOfDocumentStart, "n");
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllByDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(dateOfDocumentEnd, "n");
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllByDocTypeOrderByDateOfDocumentDesc("n");
            }
        }

        //Действующие
        if (searchForm.getSearchRelevance().equals("VALID")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllValidAndDateOfDocumentAfterAndDocTypeR(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllValidAndDateOfDocumentBeforeAndDocTypeR(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllValidAndDocTypeR(new Date());
            }
        }

        //Утратившие силу
        if (searchForm.getSearchRelevance().equals("EXPIRED")) {

            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentAfterAndDocTypeR(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentBeforeAndDocTypeR(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllExpiredAndDocTypeR(new Date());
            }

        }

        //Не вступившие в силу
        if (searchForm.getSearchRelevance().equals("INVALID")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeR(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentAfterAndDocTypeR(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentBeforeAndDocTypeR(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllInvalidAndDocTypeR(new Date());
            }
        }

        return new ArrayList<>();
    }

    private Iterable<RegPractice> filterSearchTypeZBySearchRelevanceAndDateOfDocument(SearchForm searchForm){

        Date dateOfDocumentStart = DateUtils.MIN_DATE_PLUS_DAY;
        try{
            dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
        }catch (ParseException ex){
            log.error(ex.toString());
        }

        Date dateOfDocumentEnd = DateUtils.MAX_DATE_MINUS_DAY;
        try{
            dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
        }catch (ParseException ex){
            log.error(ex.toString());
        }

        if (searchForm.getSearchRelevance().equals("ALL")) {
            return regPracticeRepository.findAllByDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(
                    dateOfDocumentStart, dateOfDocumentEnd, "z"
            );
//            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
//                //dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
//                //dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
//                return regPracticeRepository.findAllByDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(dateOfDocumentStart, dateOfDocumentEnd, "z");
//            }
//
//            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
//                //dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
//                return regPracticeRepository.findAllByDateOfDocumentAfterAndDocTypeOrderByDateOfDocumentDesc(dateOfDocumentStart, "z");
//            }
//
//            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
//                //dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
//                return regPracticeRepository.findAllByDateOfDocumentBeforeAndDocTypeOrderByDateOfDocumentDesc(dateOfDocumentEnd, "z");
//            }
//
//            // если dateOfDocument никакая не указана в поисковом запросе
//            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
//                return regPracticeRepository.findAllByDocTypeOrderByDateOfDocumentDesc("z");
//            }
        }

        //Действующие
        if (searchForm.getSearchRelevance().equals("VALID")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                //dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                //dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllValidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                //dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllValidAndDateOfDocumentAfterAndDocTypeZ(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                //dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllValidAndDateOfDocumentBeforeAndDocTypeZ(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                System.out.println("Вот тута будет");
                return regPracticeRepository.findAllValidAndDocTypeZ(new Date());
            }
        }

        //Утратившие силу
        if (searchForm.getSearchRelevance().equals("EXPIRED")) {

            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                //dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                //dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ(new Date(), dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                //dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentAfterAndDocTypeZ(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                //dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllExpiredAndDateOfDocumentBeforeAndDocTypeZ(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllExpiredAndDocTypeZ(new Date());
            }
        }

        //Не вступившие в силу
        if (searchForm.getSearchRelevance().equals("INVALID")) {
            if ((!searchForm.getSearchDateOfDocumentStart().equals("")) && (!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                //dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                //dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentAfterAndDateOfDocumentBeforeAndDocTypeZ(new Date(),dateOfDocumentStart, dateOfDocumentEnd);
            }

            if ((!searchForm.getSearchDateOfDocumentStart().equals(""))) {
                //dateOfDocumentStart = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentAfterAndDocTypeZ(new Date(), dateOfDocumentStart);
            }

            if ((!searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                //dateOfDocumentEnd = DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd());
                return regPracticeRepository.findAllInvalidAndDateOfDocumentBeforeAndDocTypeZ(new Date(), dateOfDocumentEnd);
            }

            // если dateOfDocument никакая не указана в поисковом запросе
            if ((searchForm.getSearchDateOfDocumentStart().equals("")) && (searchForm.getSearchDateOfDocumentEnd().equals(""))) {
                return regPracticeRepository.findAllInvalidAndDocTypeZ(new Date());
            }
        }
        return new ArrayList<>();

    }

    // возвращает список потомков (code принимает без L A P)
    private List<String> getAllMyChild(String code, char c) {
        List<String> codeChildAttribute = new ArrayList<>();
        if (c == 'L') {
            Iterable<ClsLifeSituation> clsLifeSituationIterable = clsLifeSituationRepository.findAllByParentCode(code);
            for (ClsLifeSituation clsLifeSituation : clsLifeSituationIterable) {
                codeChildAttribute.add(clsLifeSituation.getCode());
                if (clsLifeSituationRepository.findFirstByParentCode(clsLifeSituation.getCode()).isPresent()) {
                    codeChildAttribute.addAll(getAllMyChild(clsLifeSituation.getCode(), 'L'));
                }
            }
        }
        if (c=='A') {
            Iterable<ClsAction> clsActionIterable = clsActionRepository.findAllByParentCode(code);
            for(ClsAction clsAction : clsActionIterable){
                codeChildAttribute.add(clsAction.getCode());
                if (clsActionRepository.findFirstByParentCode(clsAction.getCode()).isPresent()) {
                    codeChildAttribute.addAll(getAllMyChild(clsAction.getCode(), 'A'));
                }
            }
        }
        if (c == 'P') {
            Iterable<ClsPaymentType> clsPaymentTypeIterable = clsPaymentTypeRepository.findAllByParentCode(code);
            for(ClsPaymentType clsPaymentType : clsPaymentTypeIterable){
                codeChildAttribute.add(clsPaymentType.getCode());
                if (clsPaymentTypeRepository.findFirstByParentCode(clsPaymentType.getCode()).isPresent()) {
                    codeChildAttribute.addAll(getAllMyChild(clsPaymentType.getCode(), 'P'));
                }
            }
        }

        return codeChildAttribute;
    }

    // возвращает список practiceCode из входящего списка
    private List<String> getPracticeCode(Iterable<RegPracticeAttribute> regPracticeAttributeIterable) {
        List<String> codePractice = new ArrayList<>();
        for (RegPracticeAttribute regPracticeAttribute : regPracticeAttributeIterable)
            codePractice.add(regPracticeAttribute.getCodePractice());
        return codePractice;
    }
    
    private List<String> getPracticeCodeFromRegPracticeIterable(Iterable<RegPractice> regPracticeIterable) {
        List<String> codePractice = new ArrayList<>();
        for (RegPractice regPractice : regPracticeIterable)
            codePractice.add(regPractice.getCode());
        return codePractice;
    }

    private Iterable<RegPracticeAttribute> getRegPracticeAttributeByTypeAndMasCodeAndSearchType(String parentCodeTag,
                                                                                                List<String> tagChildList, String searchType) {
        Iterable<RegPracticeAttribute> regPracticeAttributeIterable = null;
        tagChildList.add(parentCodeTag.substring(1));

        if (searchType.equals("ALL")) {
            if (parentCodeTag.charAt(0) == 'L') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeIn(3, listStringToMasString(tagChildList));
            if (parentCodeTag.charAt(0) == 'A') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeIn(2, listStringToMasString(tagChildList));
            if (parentCodeTag.charAt(0) == 'P') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeIn(1, listStringToMasString(tagChildList));
            if (parentCodeTag.charAt(0) == 'V') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeIn(4, listStringToMasString(tagChildList));
            if (parentCodeTag.charAt(0) == 'Q') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeIn(5, listStringToMasString(tagChildList));

        }

        if (searchType.equals("P")) {
            if (parentCodeTag.charAt(0) == 'L') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeInAndDocType(3, listStringToMasString(tagChildList), "p");
            if (parentCodeTag.charAt(0) == 'A') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeInAndDocType(2, listStringToMasString(tagChildList), "p");
            if (parentCodeTag.charAt(0) == 'P') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeInAndDocType(1, listStringToMasString(tagChildList), "p");
            if (parentCodeTag.charAt(0) == 'V') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeInAndDocType(4, listStringToMasString(tagChildList), "p");
        }

        if (searchType.equals("R")) {
            if (parentCodeTag.charAt(0) == 'L') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeInAndDocType(3, listStringToMasString(tagChildList), "n");
            if (parentCodeTag.charAt(0) == 'A') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeInAndDocType(2, listStringToMasString(tagChildList), "n");
            if (parentCodeTag.charAt(0) == 'P') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeInAndDocType(1, listStringToMasString(tagChildList), "n");
            if (parentCodeTag.charAt(0) == 'V') regPracticeAttributeIterable
                    = regPracticeAttributeRepository.findAllByAttributeTypeAndCodeAttributeInAndDocType(4, listStringToMasString(tagChildList), "n");
        }

        return regPracticeAttributeIterable;
    }


    private RegSearchStatistic searchFormToSearchStatistic(SearchForm searchForm) throws ParseException {
        RegSearchStatistic regSearchStatistic = new RegSearchStatistic();
        regSearchStatistic.setSearchDateTime(new Date());
        regSearchStatistic.setSearchText(searchForm.getSearchText());
        regSearchStatistic.setSearchType(searchForm.getSearchType());
        regSearchStatistic.setSearchRelevance(searchForm.getSearchRelevance());
        regSearchStatistic.setSearchSortType(searchForm.getSearchSortType());

        if (!searchForm.getSearchDateOfDocumentStart().equals(""))
            regSearchStatistic.setSearchDateOfDocumentStart(DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentStart()));

        if (!searchForm.getSearchDateOfDocumentEnd().equals(""))
            regSearchStatistic.setSearchDateOfDocumentEnd(DateUtils.getDateFromString(searchForm.getSearchDateOfDocumentEnd()));

        List<String> lifeSituationList = new ArrayList<>();
        List<String> actionList = new ArrayList<>();
        List<String> paymentTypeList = new ArrayList<>();
        String[] tagMas = searchForm.getSearchTagList();
        for (int i = 0; i < tagMas.length; i++) {
            if (tagMas[i].charAt(0) == 'L') lifeSituationList.add(tagMas[i].substring(1));
            if (tagMas[i].charAt(0) == 'A') actionList.add(tagMas[i].substring(1));
            if (tagMas[i].charAt(0) == 'P') paymentTypeList.add(tagMas[i].substring(1));
        }

        regSearchStatistic.setLifeSituationTags(ListUtils.listStringToMasString(lifeSituationList));
        regSearchStatistic.setActionTags(ListUtils.listStringToMasString(actionList));
        regSearchStatistic.setPaymentTypeTags(ListUtils.listStringToMasString(paymentTypeList));

        return regSearchStatistic;
    }
}



