package com.umss.siiu.core.config;

import com.umss.siiu.core.dto.OperationResultDto;
import com.umss.siiu.core.exceptions.BlockedFileException;
import com.umss.siiu.core.exceptions.InvalidFileUploadException;
import com.umss.siiu.core.exceptions.NotFoundException;
import com.umss.siiu.core.util.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception e) {
        logger.error("Internal server error", e);
        String detailMessage = e.getMessage();
        if (detailMessage == null || detailMessage.trim().isEmpty()) {
            return new ResponseEntity<>(new OperationResultDto<>("messages.genericExceptions.internalServerError"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(detailMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        logger.error("Some parameters don't meet the expected rules", e);
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return new ResponseEntity<>(new OperationResultDto<>("messages.genericExceptions.argumentNotValid", errors),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public final ResponseEntity<Object> handleMaxUploadSizeExceptions(Exception e) {
        return new ResponseEntity<>(new OperationResultDto<>("messages.genericExceptions.maxUploadSizeExceeded"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResultException.class)
    public final ResponseEntity<Object> handleNoResultExceptions(Exception e) {
        String detailMessage = e.getMessage();
        if (detailMessage == null || detailMessage.trim().isEmpty()) {
            return new ResponseEntity<>(new OperationResultDto<>("messages.genericExceptions.noResult"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(detailMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<String> handleNotFoundExceptions(Exception e) {
        return new ResponseEntity<>(ApplicationConstants.ERROR_PROCESSING_REQUEST, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileUploadException.class)
    public final ResponseEntity<String> handleInvalidFileUploadExceptions(Exception e) {
        String detailMessage = e.getMessage();
        if (detailMessage == null || detailMessage.trim().isEmpty()) {
            detailMessage = ApplicationConstants.ERROR_PROCESSING_REQUEST;
        }
        return new ResponseEntity<>(detailMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BlockedFileException.class)
    public final ResponseEntity<String> handleBlockedFileExceptions(Exception e) {
        String detailMessage = e.getMessage();
        if (detailMessage == null || detailMessage.trim().isEmpty()) {
            detailMessage = "The file is in use by another user, please wait to be unlocked";
        }
        return new ResponseEntity<>(detailMessage, HttpStatus.LOCKED);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public final ResponseEntity<String> handleObjectOptimisticLockingFailureExceptions(Exception e) {
        return new ResponseEntity<>("It seems that another user could have modified the values before you, please try" +
                " again refreshing the browser", HttpStatus.CONFLICT);
    }
}
