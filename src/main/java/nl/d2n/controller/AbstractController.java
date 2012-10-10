package nl.d2n.controller;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.GameClock;
import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    private GameClock gameClock;
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleApplicationException(ApplicationException err, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!err.getError().isSkipLogging()) {
            ControllerUtils.logParameters(err, LOGGER, request);
        }
        writeJsonStringToResponse(err.getError().toJson(err.getDescription()), response);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleMissingParameterException(TypeMismatchException err, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ControllerUtils.logParameters(D2NErrorCode.WRONG_VALUE.toString()+": "+err.getMessage(), LOGGER, request);
        writeJsonStringToResponse(D2NErrorCode.WRONG_VALUE.toJson(err.getValue().toString()), response);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleMissingParameterException(MissingServletRequestParameterException err, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ControllerUtils.logParameters(D2NErrorCode.MISSING_PARAMETER.toString()+": "+err.getMessage(), LOGGER, request);
        writeJsonStringToResponse(D2NErrorCode.MISSING_PARAMETER.toJson(err.getParameterName()), response);
    }

    @ExceptionHandler({StaleStateException.class, JpaOptimisticLockingFailureException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleOptimisticLockingException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ControllerUtils.logParameters("Optimistic Locking Failure exception", LOGGER, request);
        writeJsonStringToResponse(D2NErrorCode.SYSTEM_ERROR.toJson(), response);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleUnspecifiedExceptions(Throwable err, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ControllerUtils.logParameters(err.getMessage(), LOGGER, request);
        LOGGER.error("", err);
        writeJsonStringToResponse(D2NErrorCode.SYSTEM_ERROR.toJson(), response);
    }

    protected void writeJsonStringToResponse(String json, HttpServletResponse response) throws IOException {
        response.setContentType("application/j-son");
        response.setHeader("Date", gameClock.isInitialized() ? gameClock.getJavaScriptDate() : "");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
    }
}
