package de.psawicki.payleven.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class Catalog {

    public final ArrayList<Category> categories = new ArrayList<>();
    public final ArrayList<Product> products = new ArrayList<>();

    public void sortCategories() {
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category lhs, Category rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });
    }

    public void sortProducts() {
        Collections.sort(products, new Comparator<Product>() {

            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });
    }

}
