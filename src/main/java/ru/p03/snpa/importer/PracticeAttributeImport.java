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
import ru.p03.snpa.entity.RegPractice;
import ru.p03.snpa.entity.RegPracticeAttribute;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.Practice;
import ru.p03.snpa.entity.from1c.PracticeAttribute;
import ru.p03.snpa.repository.RegPracticeAttributeRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;
import ru.p03.snpa.utils.ListUtils;

@Component
@RestController
@RequestMapping("importer")
public class PracticeAttributeImport {

    @Autowired
    RegPracticeAttributeRepository regPracticeAttributeRepository;
    private static final Logger log = LoggerFactory.getLogger(PracticeImport.class);

    @GetMapping("startImportPracticeAttribute")
    @Scheduled(cron = "${scheduler.cron.startImportPracticeAttribute}")
    public ResponseForm startImportPracticeAttribute() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportPracticeAttribute Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi("http://10.3.30.152/skol/hs/");
            log.info("serviceAPI.getAllPracticeAttribute().execute() Start");
            Response<PracticeAttribute> practiceAttributeResponse = serviceAPI.getAllPracticeAttribute().execute();
            log.info("serviceAPI.getAllPracticeAttribute().execute() End");

            if (practiceAttributeResponse.isSuccessful()) {
                PracticeAttribute practiceAttribute = practiceAttributeResponse.body();
                ListUtils<RegPracticeAttribute> regPracticeAttributeListUtils = new ListUtils<>();
                regPracticeAttributeRepository.deleteAll();
                regPracticeAttributeRepository.saveAll(regPracticeAttributeListUtils.masToList(practiceAttribute.getRegPracticeAttributes()));
                responseForm.setData("INSERT " + practiceAttribute.getRegPracticeAttributes().length + " row");
                log.info("startImportPracticeAttribute INSERT " + practiceAttribute.getRegPracticeAttributes().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(practiceAttributeResponse.message());
                log.error("startImportPracticeAttribute " + practiceAttributeResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e) {
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportPracticeAttribute " + e.toString());
            return responseForm;
        }
    }



}
