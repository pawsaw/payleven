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
import de.psawicki.payleven.app.CatalogSession;
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

        CatalogSession catalogSession = paylevenApplication.getCatalogSession();
        BasketSession basketSession = paylevenApplication.getBasketSession();

        catalogListAdapter = new CatalogListAdapter(getActivity().getLayoutInflater(), catalogSession.getCatalog(), basketSession.getBasket());
        catalogSession.addOnCatalogChangedListener(catalogListAdapter);
        basketSession.addOnBasketChangedListener(catalogListAdapter);
        basketSession.addOnBasketChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        CatalogSession catalogSession = paylevenApplication.getCatalogSession();
        BasketSession basketSession = paylevenApplication.getBasketSession();

        basketSession.addOnBasketChangedListener(this);
        basketSession.removeOnBasketChangedListener(catalogListAdapter);
        catalogSession.removeOnCatalogChangedListener(catalogListAdapter);
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
            Product selectedProduct = paylevenApplication.getCatalogSession().getCatalog().categories.get(groupPosition).products.get(childPosition);
            paylevenApplication.getBasketSession().addProductToBasket(selectedProduct, 1);
            return true;
        }
    };
}
