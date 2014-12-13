package de.psawicki.payleven.ui;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import de.psawicki.payleven.R;
import de.psawicki.payleven.app.PaylevenApplication;
import de.psawicki.payleven.service.CatalogLoaderService;

public class MainActivity extends Activity {

    private PaylevenApplication paylevenApplication = null;
    private ProgressDialog loadCatalogProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paylevenApplication = (PaylevenApplication) getApplication();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CatalogLoaderService.INTENT_ON_CATALOG_LOAD_BEGIN);
        intentFilter.addAction(CatalogLoaderService.INTENT_ON_CATALOG_LOAD_DONE);
        intentFilter.addAction(CatalogLoaderService.INTENT_ON_CATALOG_LOAD_ERROR);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                onCatalogLoaderServiceResponse,
                intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (paylevenApplication.getCatalogSession().getCatalog().isEmpty()) {
            requestCatalogLoad();
        }
    }

    private void requestCatalogLoad() {
        Intent loadCatalogIntent = new Intent(this, CatalogLoaderService.class);
        loadCatalogIntent.setAction(CatalogLoaderService.INTENT_LOAD_CATALOG);
        startService(loadCatalogIntent);
    }

    private final BroadcastReceiver onCatalogLoaderServiceResponse = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(CatalogLoaderService.INTENT_ON_CATALOG_LOAD_BEGIN)) {
                loadCatalogProgressDialog = ProgressDialog.show(MainActivity.this, "Loading Catalog...", "Loading Catalog from Server...", true, false);
            } else if (action.equals(CatalogLoaderService.INTENT_ON_CATALOG_LOAD_DONE)) {
                loadCatalogProgressDialog.dismiss();
                paylevenApplication.getCatalogSession().notifyOnCatalogChange();
                Toast.makeText(MainActivity.this, "The Catalog has been loaded.", Toast.LENGTH_LONG);
            } else { // error
                loadCatalogProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, "The Catalog cannot be loaded.", Toast.LENGTH_LONG);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                } finally {
                    MainActivity.this.finish();
                }
            }

        }
    };

}
