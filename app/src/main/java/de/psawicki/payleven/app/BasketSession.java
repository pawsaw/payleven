package de.psawicki.payleven.app;

import android.content.Context;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import de.psawicki.payleven.model.Basket;

/**
 * Created by pawel on 12.12.14.
 */
public class BasketSession {

    private static final String PREFKEY_BASKET = "basket";

    public Basket basket = new Basket();

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
    }

    public void saveSession(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREFKEY_BASKET, Basket.toJSON(basket).toString()).commit();
    }


}
