package de.psawicki.payleven.app;

import android.content.Context;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.psawicki.payleven.model.Basket;
import de.psawicki.payleven.model.Product;

/**
 * Created by Pawel Sawicki on 12.12.14.
 */
public class BasketSession {

    private static final String PREFKEY_BASKET = "basket";

    private Basket basket = new Basket();

    public Basket getBasket() {
        return basket;
    }

    public void reloadSession(Context context) {
        String basketJSONString = PreferenceManager.getDefaultSharedPreferences(context).getString(PREFKEY_BASKET, null);
        if (basketJSONString != null) {
            try {
                basket = Basket.fromJSON(new JSONObject(basketJSONString));
            } catch (JSONException e) {
                //TODO: error handling
            }
        } else {
            basket = new Basket();
        }

        notifyOnBasketChange();
    }

    public void saveSession(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREFKEY_BASKET, Basket.toJSON(basket).toString()).commit();
    }

    public void addProductToBasket(Product product, int quantity) {
        basket.addProductToBasket(product, quantity);
        notifyOnBasketChange();
    }

    public void removeProductFromBasket(Product product, int quantity) {
        basket.removeProductFromBasket(product, quantity);
        notifyOnBasketChange();
    }

    public static interface IOnBasketChangedListener {
        void basketChanged(Basket basket);
    }

    private List<IOnBasketChangedListener> basketChangedListeners = new ArrayList<>();

    public synchronized void addOnBasketChangedListener(IOnBasketChangedListener l) {
        basketChangedListeners.add(l);
    }

    public synchronized void removeOnBasketChangedListener(IOnBasketChangedListener l) {
        basketChangedListeners.remove(l);
    }

    private synchronized void notifyOnBasketChange() {
        for (IOnBasketChangedListener l : basketChangedListeners) {
            l.basketChanged(basket);
        }
    }


}
