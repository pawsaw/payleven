package de.psawicki.payleven.ui;

import android.app.Fragment;

import de.psawicki.payleven.app.BasketSession;
import de.psawicki.payleven.model.Basket;

public class ProductsFragment extends Fragment implements BasketSession.IOnBasketChangedListener {

    @Override
    public void basketChanged(Basket basket) {

    }
}
