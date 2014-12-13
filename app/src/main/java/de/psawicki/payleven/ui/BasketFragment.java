package de.psawicki.payleven.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.psawicki.payleven.R;
import de.psawicki.payleven.app.BasketSession;
import de.psawicki.payleven.app.PaylevenApplication;
import de.psawicki.payleven.model.Basket;
import de.psawicki.payleven.model.Product;

public class BasketFragment extends Fragment implements BasketSession.IOnBasketChangedListener {

    private PaylevenApplication paylevenApplication = null;
    private BasketListAdapter basketListAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paylevenApplication = (PaylevenApplication) getActivity().getApplication();
        basketListAdapter = new BasketListAdapter(getActivity().getLayoutInflater(), paylevenApplication.getBasketSession().getBasket());
        paylevenApplication.getBasketSession().addOnBasketChangedListener(basketListAdapter);
        paylevenApplication.getBasketSession().addOnBasketChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        paylevenApplication.getBasketSession().addOnBasketChangedListener(this);
        paylevenApplication.getBasketSession().addOnBasketChangedListener(basketListAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View basketFragmentView = inflater.inflate(R.layout.fragment_basket, container, false);
        ListView basketListView = (ListView) basketFragmentView.findViewById(R.id.listView_basket);
        basketListView.setAdapter(basketListAdapter);
        basketListView.setOnItemClickListener(onProductRemoveFromBasketListener);

        return basketFragmentView;
    }

    @Override
    public void basketChanged(Basket basket) {

    }

    private final AdapterView.OnItemClickListener onProductRemoveFromBasketListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Product selectedProduct = paylevenApplication.getBasketSession().getBasket().productsInBasketSorted.get(position).product;
            paylevenApplication.getBasketSession().removeProductFromBasket(selectedProduct, 1);
        }
    };


}
