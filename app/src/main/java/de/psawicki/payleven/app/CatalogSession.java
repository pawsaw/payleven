package de.psawicki.payleven.app;

import android.content.Context;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.psawicki.payleven.model.Catalog;
import de.psawicki.payleven.model.Product;

/**
 * Created by Pawel Sawicki on 12.12.14.
 */
public class CatalogSession {

    private Catalog catalog = new Catalog();

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public static interface IOnCatalogChangedListener {
        void catalogChanged(Catalog catalog);
    }

    private List<IOnCatalogChangedListener> catalogChangedListeners = new ArrayList<>();

    public synchronized void addOnCatalogChangedListener(IOnCatalogChangedListener l) {
        catalogChangedListeners.add(l);
    }

    public synchronized void removeOnCatalogChangedListener(IOnCatalogChangedListener l) {
        catalogChangedListeners.remove(l);
    }

    public synchronized void notifyOnCatalogChange() {
        for (IOnCatalogChangedListener l : catalogChangedListeners) {
            l.catalogChanged(catalog);
        }
    }


}
