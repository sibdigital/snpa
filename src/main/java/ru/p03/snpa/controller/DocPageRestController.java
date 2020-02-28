package ru.p03.snpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.p03.snpa.entity.RegPracticeRating;
import ru.p03.snpa.entity.forms.DocPageForm;
import ru.p03.snpa.repository.RegPracticeRatingRepository;

import java.util.Optional;

@RestController
public class DocPageRestController {

    private static final Logger log = LoggerFactory.getLogger(DocPageRestController.class);

    @Autowired
    RegPracticeRatingRepository regPracticeRatingRepository;

    @PostMapping("/updateDocRating")
    public String updateDocRating(@RequestBody DocPageForm docPageForm) {
        try {
            Optional<RegPracticeRating> regPracticeRatingOptional = regPracticeRatingRepository.findById(docPageForm.getRatingId());
            if (regPracticeRatingOptional.isPresent()) {
                regPracticeRatingOptional.get().setStatus(docPageForm.getStatus());
                if (docPageForm.getComment() != null && !docPageForm.getComment().isEmpty()) {
                    regPracticeRatingOptional.get().setComment(docPageForm.getComment());
                }
                regPracticeRatingRepository.save(regPracticeRatingOptional.get());
            }
            return "OK";
        } catch (Exception e) {
            log.error("updateSearchStatistic Error: " + e.toString());
            return "ERROR";
        }
    }
}
