package de.psawicki.payleven.model;

import java.util.Comparator;
import java.util.TreeSet;

public class Catalog {

	public final TreeSet<Category> categories = new TreeSet<>(new Comparator<Category>() {

		@Override
		public int compare(Category lhs, Category rhs) {
			return lhs.name.compareTo(rhs.name);
		}
	});
	
	public final TreeSet<Product> products = new TreeSet<>(new Comparator<Product>() {

		@Override
		public int compare(Product lhs, Product rhs) {
			return lhs.name.compareTo(rhs.name);
		}
	});
	
}
