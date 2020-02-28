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
import ru.p03.snpa.entity.forms.ResponseForm;
import ru.p03.snpa.entity.from1c.Practice;
import ru.p03.snpa.repository.RegPracticeRepository;
import ru.p03.snpa.services.ControllerAPI;
import ru.p03.snpa.services.ServiceAPI;
import ru.p03.snpa.utils.DateUtils;
import ru.p03.snpa.utils.ListUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@RestController
@RequestMapping("importer")
public class PracticeImport {

    @Autowired
    RegPracticeRepository regPracticeRepository;
    private static final Logger log = LoggerFactory.getLogger(PracticeImport.class);

    @GetMapping("startImportPractice")
    @Scheduled(cron = "${scheduler.cron.startImportPractice}")
    public ResponseForm startImportPractice() {
        ResponseForm responseForm = new ResponseForm();
        try {
            responseForm.setSuccess(true);
            log.info("startImportPractice Start");

            ServiceAPI serviceAPI = ControllerAPI.getApi("http://10.3.30.152/skol/hs/");
            log.info("serviceAPI.getAllPractice().execute() Start");
            Response<Practice> practiceResponse = serviceAPI.getAllPractice().execute();
            log.info("serviceAPI.getAllPractice().execute() End");

            if (practiceResponse.isSuccessful()) {
                Practice practice = practiceResponse.body();
                ListUtils<RegPractice> regPracticeListUtils = new ListUtils<>();
                log.info("deleteAll - saveAll Start");
                regPracticeRepository.deleteAll();

                List<RegPractice> rpl =  this.processRegPractice(practice.getRegPractices());
                regPracticeRepository.saveAll(rpl);

                log.info("Insert idParent Start");
                Iterable<RegPractice> regPracticeIterable = regPracticeRepository.findAll();
                for (RegPractice regPractice : regPracticeIterable) {
                    Optional<RegPractice> regPracticeParentOptional
                            = regPracticeRepository.findFirstByCodeAndDocType(regPractice.getCodeParent(), regPractice.getParentDocType());

                    if (regPracticeParentOptional.isPresent()) {
                        regPractice.setIdParent(regPracticeParentOptional.get().getId());
                        regPracticeRepository.save(regPractice);
                    }
                }

                log.info("update ts_columns Start.");
                //update ts_content column
                regPracticeRepository.updateTsColumns();
                log.info("update ts_columns End.");

                responseForm.setData("INSERT " + practice.getRegPractices().length + " row");
                log.info("startImportPractice INSERT " + practice.getRegPractices().length + " row");
            } else {
                responseForm.setSuccess(false);
                responseForm.setData(practiceResponse.message());
                log.error("startImportPractice " + practiceResponse.message());
                return responseForm;
            }

            return responseForm;
        } catch (Exception e) {
            responseForm.setSuccess(false);
            responseForm.setData(e.toString());
            log.error("startImportPractice " + e.toString());
            return responseForm;
        }
    }

    private List<RegPractice> processRegPractice(RegPractice[] arr){
        Arrays.stream(arr).forEach(elem ->{
            if (elem.getDateStart() == null){
                elem.setDateStart(DateUtils.MIN_DATE);
            }
            if (elem.getDateEnd() == null){
                elem.setDateEnd(DateUtils.MAX_DATE);
            }
        });
        return Arrays.asList(arr);
    }

}
