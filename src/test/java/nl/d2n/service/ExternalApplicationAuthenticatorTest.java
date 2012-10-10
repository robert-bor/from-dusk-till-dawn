package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ExternalApplicationAuthenticatorTest extends SpringContextTestCase {

    @Autowired
    private ExternalApplicationAuthenticator authenticator;

    @Test
    public void testMethodWithoutASiteKey() {
        checkForWronglyImplementedMethod(
            D2NErrorCode.UNSUPPORTED_OPERATION,
            "someMethod",
            new String[]{ "notASiteKey" },
            new Class[] { String.class},
            new Object[] { "some value" } );
    }

    @Test
    public void testMethodWithANonStringSiteKey() {
        checkForWronglyImplementedMethod(
            D2NErrorCode.UNSUPPORTED_OPERATION,
            "someMethod",
            new String[]{ "siteKey" },
            new Class[] { Integer.class},
            new Object[] { "CAFEBABE" } );
    }

    @Test
    public void getSiteKey() throws ApplicationException {
        String siteKey = authenticator.getSiteKey(
            "someMethod",
            new String[]{ "siteKey" },
            new Class[] { String.class},
            new Object[] { "CAFEBABE" } );
        assertEquals("CAFEBABE", siteKey);
    }

    @Test
    public void externalApplicationDisallowedAccess() {
        try {
            authenticator.checkExternalApplicationAgainstAllowedApplications(
                    "someMethod",
                    ExternalApplication.WIKI,
                    new ExternalApplication[] { ExternalApplication.ATLAS, ExternalApplication.OVAL_OFFICE } );
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.EXTERNAL_APPLICATION_NOT_ALLOWED_ACCESS, err.getError());
        }
    }

    @Test
    public void externalApplicationAllowedAccess() {
        try {
            authenticator.checkExternalApplicationAgainstAllowedApplications(
                "someMethod",
                ExternalApplication.WIKI,
                new ExternalApplication[] { ExternalApplication.WIKI, ExternalApplication.OVAL_OFFICE } );
        } catch (ApplicationException err) {
            fail("Should not have thrown an exception: "+err.getError());
        }
    }

    protected void checkForWronglyImplementedMethod(D2NErrorCode expectedError, String methodName, String[] argumentNames,
                                                    Class[] argumentTypes, Object[] arguments) {
        try {
            authenticator.getSiteKey(methodName, argumentNames, argumentTypes, arguments);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(expectedError, err.getError());
        }
    }

}
