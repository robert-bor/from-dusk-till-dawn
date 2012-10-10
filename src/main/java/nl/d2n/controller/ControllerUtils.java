package nl.d2n.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import nl.d2n.model.ApplicationException;
import org.slf4j.Logger;

public class ControllerUtils {

    public static void logParameters(String error, Logger logger, HttpServletRequest request) {
        Enumeration parameterNames = request.getParameterNames();
        logger.error(error);
        logger.error("Source IP: "+request.getRemoteAddr()+" called "+request.getMethod()+" "+request.getRequestURI());
        while (parameterNames.hasMoreElements()) {
            String key = (String)parameterNames.nextElement();
            String value = request.getParameter(key);
            logger.error("  "+key+"="+value);
        }
    }
    public static void logParameters(ApplicationException error, Logger logger, HttpServletRequest request) {
        logParameters(error.getError().toString()+(error.getDescription() == null ? "" : ": "+error.getDescription()), logger, request);
    }
}
