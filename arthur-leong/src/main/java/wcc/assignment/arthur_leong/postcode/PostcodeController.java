package wcc.assignment.arthur_leong.postcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("v1/postcode")
public class PostcodeController {
    private final PostcodeImplementation postcodeImplementation;
    private static final Logger logger = LoggerFactory.getLogger(PostcodeController.class);

    @Autowired
    public PostcodeController(PostcodeImplementation postcodeImplementation) {
        this.postcodeImplementation = postcodeImplementation;
    }

    @GetMapping
    public ResponseEntity<Postcode> getPostcode(@RequestParam String postcode1,@RequestParam String postcode2){
        try {
            return new ResponseEntity<>(postcodeImplementation.callCalculateDistance(postcode1,postcode2),HttpStatus.OK);
        }catch (IndexOutOfBoundsException exc) {
            logger.info("GET Postcode API called failed");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage() , exc);
        }
    }

}
