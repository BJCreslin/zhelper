package ru.zhelper.zhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zhelper.zhelper.controllers.exeptions.BadRequestException;
import ru.zhelper.zhelper.models.dto.Error;
import ru.zhelper.zhelper.models.dto.ProcurementDto;
import ru.zhelper.zhelper.models.dto.Success;
import ru.zhelper.zhelper.services.ProcurementService;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/chrome"})
public class ChromeExtensionController {
    private static final Logger logger = LoggerFactory.getLogger(ChromeExtensionController.class);
    private static final String POST_FROM_IP = "Post from, procurement {}";
    private static final String POSTED_PROCUREMENT = "Procurement {} posted.";
    private static final String ERROR_FROM_PARSING = "Error parsing";
    public static final String PROCUREMENT_IS_INVALID = "Procurement is invalid.";
    public static final String PROCUREMENT_WAS_SAVED = "Procurement was saved";
    private final ProcurementService service;

    public ChromeExtensionController(ProcurementService service) {
        this.service = service;
    }

    @PostMapping(value = "/")
    @Validated
    public ResponseEntity newProcurement(@Valid @RequestBody ProcurementDto procurementDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getFieldErrors();
            var responseObject = new Error();
            responseObject.setCode(-1);
            var message = errors.stream().map(error -> "@" + error.getField().toUpperCase() + ": " + error.getDefaultMessage()).collect(Collectors.toList());
            responseObject.setMessage(PROCUREMENT_IS_INVALID);
            responseObject.setCause(message.toString());
            logger.error(procurementDto.toString());
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(POST_FROM_IP, procurementDto);
        }
        try {
            /* Todo: добавить сервис сохраняющий ДТО в базу */

            if (logger.isDebugEnabled()) {
                logger.debug(POSTED_PROCUREMENT, procurementDto);
            }
            var success = new Success();
            success.setMessage(PROCUREMENT_WAS_SAVED);
            success.setCode(1);
            return new ResponseEntity<>(success, HttpStatus.CREATED);
        } catch (BadDataParsingException exception) {
            logger.error(ERROR_FROM_PARSING, exception);
            throw new BadRequestException(ERROR_FROM_PARSING, exception);
        }

    }

    @GetMapping("/code/")
    @ResponseStatus(HttpStatus.OK)
    public String code(@RequestParam("code") Integer code) {

        return "dsdsds";
    }
}
