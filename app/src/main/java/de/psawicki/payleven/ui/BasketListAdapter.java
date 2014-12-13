package de.psawicki.payleven.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import de.psawicki.payleven.R;
import de.psawicki.payleven.app.BasketSession;
import de.psawicki.payleven.model.Basket;
import de.psawicki.payleven.model.Catalog;
import de.psawicki.payleven.model.Product;

/**
 * Created by Pawel Sawicki on 13.12.14.
 */
public class BasketListAdapter extends BaseAdapter implements BasketSession.IOnBasketChangedListener {

    static private class ProductViewHolder {
        TextView productNameTextView;
        TextView inBasketTextView;
        TextView pricePerUnitTextView;
        TextView totalPriceTextView;
    }

    private LayoutInflater layoutInflater;

    private Basket basket;

    public BasketListAdapter(LayoutInflater layoutInflater, Basket basket) {
        this.layoutInflater = layoutInflater;
        this.basket = basket;
    }

    @Override
    public int getCount() {
        return basket.productsInBasketSorted.size();
    }

    @Override
    public Object getItem(int position) {
        return basket.productsInBasketSorted.get(position);
    }

    @Override
    public long getItemId(int position) {
        return basket.productsInBasketSorted.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductViewHolder productViewHolder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_product, null);
            productViewHolder = new ProductViewHolder();
            productViewHolder.productNameTextView = (TextView) convertView.findViewById(R.id.textView_productName);
            productViewHolder.inBasketTextView = (TextView) convertView.findViewById(R.id.textView_quantityValue);
            productViewHolder.pricePerUnitTextView = (TextView) convertView.findViewById(R.id.textView_pricePerUnitValue);
            productViewHolder.totalPriceTextView = (TextView) convertView.findViewById(R.id.textView_totalPriceValue);
            convertView.setTag(productViewHolder);
        }

        Product product = basket.productsInBasketSorted.get(position).product;
        Basket.ProductInBasket productInBasket = basket.productsInBasket.get(product);

        productViewHolder = (ProductViewHolder) convertView.getTag();
        productViewHolder.productNameTextView.setText(product.name);
        productViewHolder.inBasketTextView.setText("" + (productInBasket != null ? productInBasket.quantity : 0));
        productViewHolder.pricePerUnitTextView.setText(String.format("%.2f", product.price));
        productViewHolder.totalPriceTextView.setText(String.format("%.2f", (productInBasket != null ? productInBasket.getTotalPrice() : 0.0)));

        return convertView;
    }

    @Override
    public void basketChanged(Basket basket) {
        this.basket = basket;
        notifyDataSetChanged();
    }
}
