package de.psawicki.payleven.app;

import android.app.Application;

import de.psawicki.payleven.model.Catalog;

/**
 * Created by pawel on 12.12.14.
 */
public class PaylevenApplication extends Application {

    private BasketSession basketSession = new BasketSession();

    private Catalog catalog = new Catalog();

    public BasketSession getBasketSession() {
        return basketSession;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Catalog getCatalog() {
        return catalog;
    }

}
