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
import ru.p03.snpa.entity.ClsAction;
import ru.p03.snpa.entity.ClsAttributeType;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.Action;
import ru.p03.snpa.entity.from1c.AttributeType;
import ru.p03.snpa.repository.ClsAttributeTypeRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;
import ru.p03.snpa.utils.ListUtils;

@Component
@RestController
@RequestMapping("importer")
public class AttributeTypeImport {
    @Value("${url.import}")
    private String URL_IMPORT;

    @Autowired
    ClsAttributeTypeRepository clsAttributeTypeRepository;
    private static final Logger log = LoggerFactory.getLogger(ActionImport.class);

    @GetMapping("startImportAttributeType")
    @Scheduled(cron = "${scheduler.cron.startImportAttributeType}")
    public ResponseForm startImportAttributeType() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportAttributeType Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi(URL_IMPORT);

            Response<AttributeType> attributeTypeResponse = serviceAPI.getAllAttributeType().execute();
            if (attributeTypeResponse.isSuccessful()) {
                AttributeType attributeType = attributeTypeResponse.body();
                ListUtils<ClsAttributeType> clsAttributeTypeListUtils = new ListUtils<>();
                clsAttributeTypeRepository.deleteAll();
                clsAttributeTypeRepository.saveAll(clsAttributeTypeListUtils.masToList(attributeType.getClsAttributeTypes()));
                responseForm.setData("INSERT " + attributeType.getClsAttributeTypes().length + " row");
                log.info("startImportAttributeType INSERT " + attributeType.getClsAttributeTypes().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(attributeTypeResponse.message());
                log.error("startImportAttributeType " + attributeTypeResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e){
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportAttributeType " + e.toString());
            return responseForm;
        }
    }

}
