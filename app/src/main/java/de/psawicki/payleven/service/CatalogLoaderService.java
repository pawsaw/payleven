package de.psawicki.payleven.service;

import android.app.IntentService;
import android.content.Intent;

public class CatalogLoaderService extends IntentService {

	private static final String TAG = "CatalogLoaderService";
	
	public CatalogLoaderService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}

}
