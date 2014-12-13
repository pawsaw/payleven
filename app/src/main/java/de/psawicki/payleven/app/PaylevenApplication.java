package de.psawicki.payleven.app;

import android.app.Application;

import de.psawicki.payleven.model.Catalog;

/**
 * Created by Pawel Sawicki on 12.12.14.
 */
public class PaylevenApplication extends Application {

    private BasketSession basketSession = new BasketSession();
    private CatalogSession catalogSession = new CatalogSession();

    public BasketSession getBasketSession() {
        return basketSession;
    }

    public CatalogSession getCatalogSession() {
        return catalogSession;
    }
}
