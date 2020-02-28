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
import ru.p03.snpa.entity.ClsPracticePractice;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.PracticePractice;
import ru.p03.snpa.repository.ClsPracticePracticeRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;
import ru.p03.snpa.utils.ListUtils;

@Component
@RestController
@RequestMapping("importer")
public class PracticePracticeImport {

    @Autowired
    ClsPracticePracticeRepository clsPracticePracticeRepository;
    private static final Logger log = LoggerFactory.getLogger(PracticeImport.class);

    @GetMapping("startImportPracticePractice")
    @Scheduled(cron = "${scheduler.cron.startImportPracticePractice}")
    public ResponseForm startImportPracticePractice() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportPracticePractice Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi("http://10.3.30.152/skol/hs/");
            log.info("serviceAPI.getAllPractice().execute() Start");
            Response<PracticePractice> practicePracticeResponse = serviceAPI.getAllPracticePractice().execute();
            log.info("serviceAPI.getAllPractice().execute() End");

            if (practicePracticeResponse.isSuccessful()) {
                PracticePractice practicePractice = practicePracticeResponse.body();
                ListUtils<ClsPracticePractice> clsPracticePracticeListUtils = new ListUtils<>();

                log.info("deleteAll - saveAll Start");
                clsPracticePracticeRepository.deleteAll();
                clsPracticePracticeRepository.saveAll(clsPracticePracticeListUtils.masToList(practicePractice.getClsPracticePractices()));

                responseForm.setData("INSERT " + practicePractice.getClsPracticePractices().length + " row");
                log.info("startImportPracticePractice INSERT " + practicePractice.getClsPracticePractices().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(practicePracticeResponse.message());
                log.error("startImportPracticePractice " + practicePracticeResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e) {
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportPracticePractice " + e.toString());
            return responseForm;
        }
    }

}
