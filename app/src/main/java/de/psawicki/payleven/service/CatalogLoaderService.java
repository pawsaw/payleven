package de.psawicki.payleven.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import de.psawicki.payleven.app.PaylevenApplication;
import de.psawicki.payleven.model.Catalog;
import de.psawicki.payleven.rest.PaylevenREST;

public class CatalogLoaderService extends IntentService {

    public static final String INTENT_LOAD_CATALOG = "de.psawicki.payleven.service.CatalogLoaderService.LOAD_CATALOG";

    public static final String INTENT_ON_CATALOG_LOAD_BEGIN = "de.psawicki.payleven.service.CatalogLoaderService.ON_CATALOG_LOAD_BEGIN";
    public static final String INTENT_ON_CATALOG_LOAD_DONE = "de.psawicki.payleven.service.CatalogLoaderService.ON_CATALOG_LOAD_DONE";
    public static final String INTENT_ON_CATALOG_LOAD_ERROR = "de.psawicki.payleven.service.CatalogLoaderService.ON_CATALOG_LOAD_ERROR";

	private static final String TAG = "CatalogLoaderService";
	
	public CatalogLoaderService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

        if (intent.getAction().equals(CatalogLoaderService.INTENT_LOAD_CATALOG)) {
            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
            lbm.sendBroadcast(new Intent(CatalogLoaderService.INTENT_ON_CATALOG_LOAD_BEGIN));
            try {
                PaylevenApplication paylevenApplication = (PaylevenApplication) getApplication();
                Catalog catalog = PaylevenREST.loadCatalog();
                paylevenApplication.getCatalogSession().setCatalog(catalog);
                lbm.sendBroadcast(new Intent(CatalogLoaderService.INTENT_ON_CATALOG_LOAD_DONE));
            } catch (Exception ex) {
                Intent onCatalogLoadErrorIntent = new Intent(CatalogLoaderService.INTENT_ON_CATALOG_LOAD_ERROR).putExtra("error", ex.getMessage());
                lbm.sendBroadcast(onCatalogLoadErrorIntent);
            }
        }
    }

}
