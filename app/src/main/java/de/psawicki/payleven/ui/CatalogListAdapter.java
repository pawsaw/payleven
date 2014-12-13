package de.psawicki.payleven.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.psawicki.payleven.R;
import de.psawicki.payleven.app.BasketSession;
import de.psawicki.payleven.model.Basket;
import de.psawicki.payleven.model.Catalog;
import de.psawicki.payleven.model.Product;

/**
 * Created by Pawel Sawicki on 13.12.14.
 */
public class CatalogListAdapter extends BaseExpandableListAdapter implements BasketSession.IOnBasketChangedListener {

    static private class CategoryViewHolder {
        TextView categoryNameTextView;
    }

    static private class ProductViewHolder {
        TextView productNameTextView;
        TextView inBasketTextView;
        TextView pricePerUnitTextView;
        TextView totalPriceTextView;
    }

    private LayoutInflater layoutInflater;

    private Catalog catalog;
    private Basket basket;

    public CatalogListAdapter(LayoutInflater layoutInflater, Catalog catalog, Basket basket) {
        this.layoutInflater = layoutInflater;
        this.catalog = catalog;
        this.basket = basket;
    }

    @Override
    public int getGroupCount() {
        return catalog.categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return catalog.categories.get(groupPosition).products.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return catalog.categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return catalog.categories.get(groupPosition).products.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return catalog.categories.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return catalog.categories.get(groupPosition).products.get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        CategoryViewHolder categoryViewHolder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_category, null);
            categoryViewHolder = new CategoryViewHolder();
            categoryViewHolder.categoryNameTextView = (TextView) convertView.findViewById(R.id.textView_categoryName);
            convertView.setTag(categoryViewHolder);
        }

        categoryViewHolder = (CategoryViewHolder) convertView.getTag();
        categoryViewHolder.categoryNameTextView.setText(catalog.categories.get(groupPosition).name);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ProductViewHolder productViewHolder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_product, null);
            productViewHolder = new ProductViewHolder();
            productViewHolder.productNameTextView = (TextView) convertView.findViewById(R.id.textView_productName);
            productViewHolder.inBasketTextView = (TextView) convertView.findViewById(R.id.textView_quantityValue);
            productViewHolder.pricePerUnitTextView = (TextView) convertView.findViewById(R.id.textView_pricePerUnitValue);
            productViewHolder.totalPriceTextView = (TextView) convertView.findViewById(R.id.textView_totalPriceValue);
        }

        Product product = catalog.categories.get(groupPosition).products.get(childPosition);
        Basket.ProductInBasket productInBasket = basket.productsInBasket.get(product);

        productViewHolder = (ProductViewHolder) convertView.getTag();
        productViewHolder.productNameTextView.setText(product.name);
        productViewHolder.inBasketTextView.setText(productInBasket != null ? productInBasket.quantity : 0);
        productViewHolder.pricePerUnitTextView.setText(String.format(".2f", product.price));
        productViewHolder.totalPriceTextView.setText(String.format(".2f", (productInBasket != null ? productInBasket.getTotalPrice() : 0)));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void basketChanged(Basket basket) {
        this.basket = basket;
        notifyDataSetChanged();
    }
}
