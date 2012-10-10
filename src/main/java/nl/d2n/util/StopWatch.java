package nl.d2n.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class StopWatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopWatch.class);

    @Pointcut("execution(public * nl.d2n.dao.*.*(..)) || execution(public * nl.d2n.reader.*.*(..))")
    public void mainMethod() {}

    @Around("mainMethod()")
    public Object watchPerformance(ProceedingJoinPoint joinpoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object returnObject = joinpoint.proceed();
        long end = System.currentTimeMillis();
        long timeClocked = end - start;
//        LOGGER.info("@PERF "+joinpoint.getSignature().toString() + ": " + (timeClocked) + " milliseconds.");
        if (timeClocked > 1000) {
            LOGGER.warn(joinpoint.getSignature().toString() + ": " + (timeClocked) + " milliseconds.");
        }
        return returnObject;
    }
}
