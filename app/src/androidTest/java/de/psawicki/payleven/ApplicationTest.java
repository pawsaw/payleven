package de.psawicki.payleven;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.io.IOException;

import de.psawicki.payleven.model.Catalog;
import de.psawicki.payleven.rest.JSONConnectionException;
import de.psawicki.payleven.rest.PaylevenREST;
import de.psawicki.payleven.rest.PaylevenRESTException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testPaylevenREST_loadCatalog() throws IOException, PaylevenRESTException {
        Catalog catalog = PaylevenREST.loadCatalog();
        assertNotNull(catalog);
    }

}