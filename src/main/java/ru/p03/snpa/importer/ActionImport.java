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
import ru.p03.snpa.entity.ClsAction;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.Action;
import ru.p03.snpa.repository.ClsActionRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;
import ru.p03.snpa.utils.ListUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@RestController
@RequestMapping("importer")
public class ActionImport {

    @Autowired
    ClsActionRepository clsActionRepository;
    private static final Logger log = LoggerFactory.getLogger(ActionImport.class);

    @GetMapping("startImportAction")
    @Scheduled(cron = "${scheduler.cron.startImportAction}")
    public ResponseForm startImportAction() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportAction Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi("http://10.3.30.152/skol/hs/");
            Response<Action> actionResponse = serviceAPI.getAllActions().execute();
            if (actionResponse.isSuccessful()) {
                Action action = actionResponse.body();
                ListUtils<ClsAction> clsActionListUtils = new ListUtils<>();
                clsActionRepository.deleteAll();
                clsActionRepository.saveAll(clsActionListUtils.masToList(action.getClsActions()));
                responseForm.setData("INSERT " + action.getClsActions().length + " row");
                log.info("startImportAction INSERT " + action.getClsActions().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(actionResponse.message());
                log.error("startImportAction " + actionResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e){
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportAction " + e.toString());
            return responseForm;
        }
    }

}
