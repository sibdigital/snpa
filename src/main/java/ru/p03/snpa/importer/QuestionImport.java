package ru.p03.snpa.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.Question;
import ru.p03.snpa.repository.ClsQuestionRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;

import java.util.Arrays;

@Component
@RestController
@RequestMapping("importer")
public class QuestionImport {

    @Autowired
    ClsQuestionRepository clsQuestionRepository;
    private static final Logger log = LoggerFactory.getLogger(QuestionImport.class);

    @GetMapping("startImportQuestion")
    //@Scheduled(cron = "${scheduler.cron.startImportQuestion}")
    public ResponseForm startImportQuestion() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportQuestion Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi("http://10.3.30.152/skol/hs/");
            log.info("serviceAPI.getAllQuestion().execute() Start");
            Response<Question> questionResponse = serviceAPI.getAllQuestions().execute();
            log.info("serviceAPI.getAllQuestion().execute() End");

            if (questionResponse.isSuccessful()) {
                Question question = questionResponse.body();
                //ListUtils<RegQuestion> regQuestionListUtils = new ListUtils<>();
                log.info("deleteAll - Start");
                clsQuestionRepository.deleteAll();
                log.info("deleteAll - End");
                log.info("saveAll - Start");
                //clsQuestionRepository.saveAll(regQuestionListUtils.masToList(question.getRegQuestions()));
                clsQuestionRepository.saveAll( Arrays.asList(question.getClsQuestion()) ) ;
                log.info("saveAll - End");

//                log.info("Insert idParent Start");
//                Iterable<RegQuestion> regQuestionIterable = regQuestionRepository.findAll();
//                for (RegQuestion regQuestion : regQuestionIterable) {
//                    Optional<RegQuestion> regQuestionParentOptional
//                            = regQuestionRepository.findFirstByCodeAndDocType(regQuestion.getCodeParent(), regQuestion.getParentDocType());
//
//                    if (regQuestionParentOptional.isPresent()) {
//                        regQuestion.setIdParent(regQuestionParentOptional.get().getId());
//                        regQuestionRepository.save(regQuestion);
//                    }
//                }

                log.info("update ts_columns Start.");
                //update ts_content column
                clsQuestionRepository.updateTsColumns();
                log.info("update ts_columns End.");

                responseForm.setData("INSERT " + question.getClsQuestion().length + " row");
                log.info("startImportQuestion INSERT " + question.getClsQuestion().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(questionResponse.message());
                log.error("startImportQuestion " + questionResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e) {
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportQuestion " + e.toString());
            return responseForm;
        }
    }

}
