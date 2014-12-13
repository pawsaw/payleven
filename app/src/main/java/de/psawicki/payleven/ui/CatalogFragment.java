package de.psawicki.payleven.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import de.psawicki.payleven.R;
import de.psawicki.payleven.app.BasketSession;
import de.psawicki.payleven.app.PaylevenApplication;
import de.psawicki.payleven.model.Basket;
import de.psawicki.payleven.model.Product;

public class CatalogFragment extends Fragment implements BasketSession.IOnBasketChangedListener {

    private PaylevenApplication paylevenApplication = null;
    private CatalogListAdapter catalogListAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paylevenApplication = (PaylevenApplication) getActivity().getApplication();
        catalogListAdapter = new CatalogListAdapter(
                getActivity().getLayoutInflater(),
                paylevenApplication.getCatalog(),
                paylevenApplication.getBasketSession().getBasket());
        paylevenApplication.getBasketSession().addOnBasketChangedListener(catalogListAdapter);
        paylevenApplication.getBasketSession().addOnBasketChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        paylevenApplication.getBasketSession().addOnBasketChangedListener(this);
        paylevenApplication.getBasketSession().removeOnBasketChangedListener(catalogListAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View catalogFragmentView = inflater.inflate(R.layout.fragment_catalog, container, false);
        ExpandableListView catalogExpandableListView = (ExpandableListView) catalogFragmentView.findViewById(R.id.expandableListView_catalog);
        catalogExpandableListView.setOnChildClickListener(onProductSelected);
        catalogExpandableListView.setAdapter(catalogListAdapter);

        return catalogFragmentView;
    }

    @Override
    public void basketChanged(Basket basket) {

    }

    private final ExpandableListView.OnChildClickListener onProductSelected = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Product selectedProduct = paylevenApplication.getCatalog().categories.get(groupPosition).products.get(childPosition);
            paylevenApplication.getBasketSession().addProductToBasket(selectedProduct, 1);
            return true;
        }
    };
}
