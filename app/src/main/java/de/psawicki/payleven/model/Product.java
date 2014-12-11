package de.psawicki.payleven.model;

import java.util.Comparator;
import java.util.TreeSet;

public class Product {

	public final String id;
	public final String name;
	public final float price;
	
	// sorted iterable
	public final TreeSet<Category> categories = new TreeSet<>(new Comparator<Category>() {

		@Override
		public int compare(Category lhs, Category rhs) {
			return lhs.name.compareTo(rhs.name);
		}
	});
	
	public Product(String id, String name, float price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
