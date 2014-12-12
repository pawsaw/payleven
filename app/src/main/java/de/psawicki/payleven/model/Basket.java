package de.psawicki.payleven.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Basket {
	
	public static class ProductInBasket {
		
		public final Product product;
		public int amount;
		
		public ProductInBasket(Product product, int amount) {
			super();
			this.product = product;
			this.amount = amount;
		}
		
		public float getTotalPrice() {
			return amount * product.price;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((product == null) ? 0 : product.hashCode());
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
			ProductInBasket other = (ProductInBasket) obj;
			if (product == null) {
				if (other.product != null)
					return false;
			} else if (!product.equals(other.product))
				return false;
			return true;
		}
	}
	
	// The total basket price. "Lazy Calculation" on the first access. Reset on basket change.
	private Float totalBasketPrice = null;

	// O(1) access to an existing product in basket
	private final Map<Product, ProductInBasket> productsInBasket = new HashMap<>();
	
	// Access to sorted products in the basket
	private final TreeSet<ProductInBasket> productsInBasketSorted = new TreeSet<>(new Comparator<ProductInBasket>() {

		@Override
		public int compare(ProductInBasket lhs, ProductInBasket rhs) {
			return lhs.product.name.compareTo(rhs.product.name);
		}
	});
	
	
	public void addProductToBasket(Product product, int amount) {
		
		ProductInBasket productInBasket = productsInBasket.get(product);
		if (productInBasket != null) {
			// update the product in basket
			productInBasket.amount += amount;
		} else {
			// if the product doesn't exist in the basket yet, put it in
			productInBasket = new ProductInBasket(product, amount);
			productsInBasket.put(product, productInBasket);
			productsInBasketSorted.add(productInBasket);
		}
		
		totalBasketPrice = null;
	}
	
	public void removeProductFromBasket(Product product, int amount) {
		
		ProductInBasket productInBasket = productsInBasket.get(product);
		if (productInBasket != null) {
			// update the product in basket
			productInBasket.amount -= amount;
			if (productInBasket.amount <= 0) {
				productsInBasket.remove(productInBasket);
				productsInBasketSorted.remove(productInBasket);
			}
			
			totalBasketPrice = null;
		} 
	}
	
	public float getTotalBasketPrice() {
		if (totalBasketPrice == null) {
			totalBasketPrice = 0f;
			for (ProductInBasket currProductInBasket : productsInBasketSorted) {
				totalBasketPrice += currProductInBasket.getTotalPrice();
			}
		}
		
		return totalBasketPrice;
	}

    public static JSONObject toJSON(Basket basket) {
        JSONObject result = new JSONObject();

        try {
            JSONArray productsInBasketJSON = new JSONArray();
            for (ProductInBasket currProductInBasket : basket.productsInBasketSorted) {
                JSONObject productJSON = new JSONObject();

                productJSON.put("id", currProductInBasket.product.id);
                productJSON.put("name", currProductInBasket.product.name);
                productJSON.put("price", currProductInBasket.product.price);
                JSONObject currProductInBasketJSON = new JSONObject();
                currProductInBasketJSON.put("product", productJSON);
                currProductInBasketJSON.put("amount", currProductInBasket.amount);
                currProductInBasketJSON.put("totalPrice", currProductInBasket.getTotalPrice());
                productsInBasketJSON.put(currProductInBasketJSON);

            }

            result.put("productsInBasket", productsInBasketJSON);
            result.put("totalBasketPrice", basket.getTotalBasketPrice());
        } catch (JSONException e) {
            // WON'T HAPPEN. DO NOTHING!
        }

        return result;
    }

    public static Basket fromJSON(JSONObject basketJSON) {
        Basket basket = new Basket();

        try {
            JSONArray productsInBasketJSON = basketJSON.getJSONArray("productsInBasket");
            if (productsInBasketJSON != null) {
                for (int i = 0; i < productsInBasketJSON.length(); i++) {
                    JSONObject productInBasketJSON = productsInBasketJSON.getJSONObject(i);
                    JSONObject productJSON = productInBasketJSON.getJSONObject("product");
                    Product product = new Product(
                            productJSON.getString("id"),
                            productJSON.getString("name"),
                            (float) productJSON.getDouble("price"));
                    int amount = productInBasketJSON.getInt("amount");
                    basket.addProductToBasket(product, amount);
                }
            }

        } catch (JSONException e) {
            //TODO: error handling
        }

        return basket;
    }

}
