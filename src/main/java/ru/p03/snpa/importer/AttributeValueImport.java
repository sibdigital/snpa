package ru.p03.snpa.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import ru.p03.snpa.entity.ClsAttributeValue;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.AttributeValue;
import ru.p03.snpa.repository.ClsAttributeTypeRepository;
import ru.p03.snpa.repository.ClsAttributeValueRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;
import ru.p03.snpa.utils.ListUtils;

@Component
@RestController
@RequestMapping("importer")
public class AttributeValueImport {
    @Value("${url.import}")
    private String URL_IMPORT;

    @Autowired
    ClsAttributeValueRepository clsAttributeValueRepository;
    private static final Logger log = LoggerFactory.getLogger(ActionImport.class);

    @GetMapping("startImportAttributeValue")
    @Scheduled(cron = "${scheduler.cron.startImportAttributeValue}")
    public ResponseForm startImportAttributeValue() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportAttributeValue Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi(URL_IMPORT);

            Response<AttributeValue> AttributeValueResponse = serviceAPI.getAllAttributeValue().execute();
            if (AttributeValueResponse.isSuccessful()) {
                AttributeValue AttributeValue = AttributeValueResponse.body();
                ListUtils<ClsAttributeValue> clsAttributeValueListUtils = new ListUtils<>();
                clsAttributeValueRepository.deleteAll();
                clsAttributeValueRepository.saveAll(clsAttributeValueListUtils.masToList(AttributeValue.getClsAttributeValues()));
                responseForm.setData("INSERT " + AttributeValue.getClsAttributeValues().length + " row");
                log.info("startImportAttributeValue INSERT " + AttributeValue.getClsAttributeValues().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(AttributeValueResponse.message());
                log.error("startImportAttributeValue " + AttributeValueResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e){
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportAttributeValue " + e.toString());
            return responseForm;
        }
    }

}
