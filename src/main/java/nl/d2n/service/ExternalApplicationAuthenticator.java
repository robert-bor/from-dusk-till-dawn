package nl.d2n.service;

import nl.d2n.model.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

@Aspect
public class ExternalApplicationAuthenticator {

    public static final String SITE_KEY_ARGUMENT_NAME = "siteKey";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalApplicationAuthenticator.class);

    @Autowired
    private SiteKeyChecker siteKeyChecker;

    @Pointcut("execution(@nl.d2n.model.AuthenticateExternalApplication * *(..))")
    public void methodRequiringExternalApplicationAuthentication() {}

    @Around("methodRequiringExternalApplicationAuthentication()")
    public Object authenticateExternalApplication(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthenticateExternalApplication annotation = method.getAnnotation(AuthenticateExternalApplication.class);

        ExternalApplication externalApplication = siteKeyChecker.getExternalApplication(
            getSiteKey(
                method.getName(),
                signature.getParameterNames(),
                signature.getMethod().getParameterTypes(),
                joinPoint.getArgs()
            ));

        if (annotation.checkSite()) {
            checkExternalApplicationAgainstAllowedApplications(method.getName(), externalApplication, annotation.allow());
        }

        LOGGER.info("Granted access on "+method.getName()+" to "+externalApplication);

        return joinPoint.proceed();
    }

    public void checkExternalApplicationAgainstAllowedApplications(
                String methodName,
                ExternalApplication externalApplication,
                ExternalApplication[] allowedExternalApplications) throws ApplicationException{
        for (ExternalApplication allowedExternalApplication : allowedExternalApplications) {
            if (allowedExternalApplication.equals(externalApplication)) {
                return;
            }
        }
        LOGGER.error("Method "+methodName+" does not allow access to "+externalApplication);
        throw new ApplicationException(D2NErrorCode.EXTERNAL_APPLICATION_NOT_ALLOWED_ACCESS);
    }

    public String getSiteKey(String methodName, String[] parameterNames, Class[] parameterTypes, Object[] arguments) throws ApplicationException {
        int siteKeyArgumentPosition = getSiteKeyArgumentPosition(methodName, parameterNames);
        verifySiteKeyType(methodName, parameterTypes, siteKeyArgumentPosition);
        return (String)arguments[siteKeyArgumentPosition];
    }

    private void verifySiteKeyType(String methodName, Class[] argumentTypes, int siteKeyArgumentPosition) throws ApplicationException {
        if (!String.class.equals(argumentTypes[siteKeyArgumentPosition])) {
            LOGGER.error("Method "+methodName+" has a "+SITE_KEY_ARGUMENT_NAME+" which is not of type String");
            throw new ApplicationException(D2NErrorCode.UNSUPPORTED_OPERATION);
        }
    }

    private int getSiteKeyArgumentPosition(String methodName, String[] parameterNames) throws ApplicationException {
        int count = 0;
        for (String parameterName : parameterNames) {
            if (SITE_KEY_ARGUMENT_NAME.equals(parameterName)) {
                return count;
            }
            count++;
        }
        LOGGER.error("Method "+methodName+" does not have a "+SITE_KEY_ARGUMENT_NAME+" argument");
        throw new ApplicationException(D2NErrorCode.UNSUPPORTED_OPERATION);
    }

    public void setSiteKeyChecker(SiteKeyChecker siteKeyChecker) { this.siteKeyChecker = siteKeyChecker; }
}
