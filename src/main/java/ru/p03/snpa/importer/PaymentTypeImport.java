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
import ru.p03.snpa.entity.ClsPaymentType;
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.PaymentType;
import ru.p03.snpa.repository.ClsPaymentTypeRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;
import ru.p03.snpa.utils.ListUtils;

@Component
@RestController
@RequestMapping("importer")
public class PaymentTypeImport {
    @Value("${url.import}")
    private String URL_IMPORT;

    @Autowired
    ClsPaymentTypeRepository clsPaymentTypeRepository;

    private static final Logger log = LoggerFactory.getLogger(PaymentTypeImport.class);

    @GetMapping("startImportPaymentType")
    @Scheduled(cron = "${scheduler.cron.startImportPaymentType}")
    public ResponseForm startImportPaymentType() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportPaymentType Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi(URL_IMPORT);
            Response<PaymentType> paymentTypeResponse = serviceAPI.getAllPaymentType().execute();
            if (paymentTypeResponse.isSuccessful()) {
                PaymentType paymentType = paymentTypeResponse.body();
                ListUtils<ClsPaymentType> clsPaymentTypeListUtils = new ListUtils<>();
                clsPaymentTypeRepository.deleteAll();
                clsPaymentTypeRepository.saveAll(clsPaymentTypeListUtils.masToList(paymentType.getClsPaymentTypes()));
                responseForm.setData("INSERT " + paymentType.getClsPaymentTypes().length + " row");
                log.info("startImportPaymentType INSERT " + paymentType.getClsPaymentTypes().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(paymentTypeResponse.message());
                log.error("startImportPaymentType " + paymentTypeResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e) {
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportPaymentType " + e.toString());

            return responseForm;
        }
    }

}
