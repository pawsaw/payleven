package de.psawicki.payleven.model;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Category {

	public final String id;
	public final String name;
	
	// sorted iterable
	public final TreeSet<Product> products = new TreeSet<>(new Comparator<Product>() {

		@Override
		public int compare(Product lhs, Product rhs) {
			return lhs.name.compareTo(rhs.name);
		}
	});
	
	public Category(String id, String name) {
		super();
		this.id = id;
		this.name = name;
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
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
