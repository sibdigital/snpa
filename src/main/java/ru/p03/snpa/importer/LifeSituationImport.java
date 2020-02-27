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
import ru.p03.snpa.entity.ClsLifeSituation;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.LifeSituation;
import ru.p03.snpa.repository.ClsLifeSituationRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;
import ru.p03.snpa.utils.ListUtils;

@Component
@RestController
@RequestMapping("importer")
public class LifeSituationImport {

    @Autowired
    ClsLifeSituationRepository clsLifeSituationRepository;
    private static final Logger log = LoggerFactory.getLogger(LifeSituationImport.class);

    @GetMapping("startImportLifeSituation")
    @Scheduled(cron = "${scheduler.cron.startImportLifeSituation}")
    public ResponseForm startImportLifeSituation() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportLifeSituation Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi("http://10.3.30.152/skol/hs/");
            Response<LifeSituation> lifeSituationResponse = serviceAPI.getAllLifeSituation().execute();
            if (lifeSituationResponse.isSuccessful()) {
                LifeSituation lifeSituation = lifeSituationResponse.body();
                ListUtils<ClsLifeSituation> clsLifeSituationListUtils = new ListUtils<>();
                clsLifeSituationRepository.deleteAll();
                clsLifeSituationRepository.saveAll(clsLifeSituationListUtils.masToList(lifeSituation.getClsLifeSituations()));
                responseForm.setData("INSERT " + lifeSituation.getClsLifeSituations().length + " row");
                log.info("startImportLifeSituation INSERT " + lifeSituation.getClsLifeSituations().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(lifeSituationResponse.message());
                log.error("startImportLifeSituation " + lifeSituationResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e) {
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportLifeSituation " + e.toString());
            return responseForm;
        }
    }

}
