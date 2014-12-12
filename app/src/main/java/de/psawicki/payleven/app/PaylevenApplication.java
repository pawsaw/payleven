package de.psawicki.payleven.app;

import android.app.Application;

import de.psawicki.payleven.model.Catalog;

/**
 * Created by pawel on 12.12.14.
 */
public class PaylevenApplication extends Application {

    private BasketSession basketSession = null;

    public BasketSession getBasketSession() {
        return basketSession;
    }

    private Catalog catalog;

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        basketSession = new BasketSession();
    }
}
