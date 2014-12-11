package de.psawicki.payleven.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.psawicki.payleven.model.Catalog;
import de.psawicki.payleven.model.Category;
import de.psawicki.payleven.model.Product;



public class PaylevenREST {
	
	public static final String URL_BASE = "http://psawicki.net/payleven/";	
	
	public static final String RES_CATALOG = PaylevenREST.URL_BASE + "paylevenCatalog.json";
	
	public static Catalog loadCatalog() throws JSONConnectionException, IOException, PaylevenRESTException {
		Catalog result = new Catalog();
		
		try {
			
			JSONObject response = JSONConnectionUtil.getJSON(PaylevenREST.RES_CATALOG);
			
			Map<String, Product> productById = new HashMap<>(); // used for associating Product with Category in O(1)
			
			JSONArray productsJSONArray = response.getJSONArray("products");
			for (int i = 0; i < productsJSONArray.length(); i++) {
				JSONObject currProductJson = productsJSONArray.getJSONObject(i);
				Product product = PaylevenREST.productFromJson(currProductJson);
				result.products.add(product);
				productById.put(product.id, product);
			}
			
			JSONArray categoriesJSONArray = response.getJSONArray("categories");
			for (int i = 0; i < categoriesJSONArray.length(); i++) {
				JSONObject currCategoryJson = categoriesJSONArray.getJSONObject(i);
				Category category = PaylevenREST.categoryFromJson(currCategoryJson, productById);
				result.categories.add(category);
			}
			
		} catch (JSONException e) {
			throw new PaylevenRESTException(e);
		}
		
		return result;
	}
	
	private static Product productFromJson(JSONObject json) throws PaylevenRESTException, JSONException {
        
        String[] requiredParameters = new String[] { "id", "name", "price"};
        for (String requiredParameter : requiredParameters) {
            if (!json.has(requiredParameter)) {
                throw new PaylevenRESTException("Missing the required '" + requiredParameter + "' parameter.");
            }
        }
        
        String[] stringParameters = new String[] { "id", "name"};
        for (String stringParameter : stringParameters) {
            
            String requiredParameterValue = json.getString(stringParameter).trim();
            if (requiredParameterValue.isEmpty()) {
                throw new PaylevenRESTException("The required parameter '" + stringParameter + "' has to be non empty. Given: '" + requiredParameterValue + "'.");
            }
        }
        
        String id = json.getString("id");
        String name = json.getString("name");
        float price = (float) json.getDouble("price");
        
        return new Product(id, name, price);
    }
	
	private static Category categoryFromJson(JSONObject json, Map<String, Product> productById) throws PaylevenRESTException, JSONException {
        
		String[] requiredParameters = new String[] { "id", "name", "products"};
        for (String requiredParameter : requiredParameters) {
            if (!json.has(requiredParameter)) {
                throw new PaylevenRESTException("Missing the required '" + requiredParameter + "' parameter.");
            }
        }
        
        String[] stringParameters = new String[] { "id", "name"};
        for (String stringParameter : stringParameters) {
            
            String requiredParameterValue = json.getString(stringParameter).trim();
            if (requiredParameterValue.isEmpty()) {
                throw new PaylevenRESTException("The required parameter '" + stringParameter + "' has to be non empty. Given: '" + requiredParameterValue + "'.");
            }
        }
        
        String id = json.getString("id");
        String name = json.getString("name");
        
        Category category = new Category(id, name);
        
        JSONArray productsJSONArray = json.getJSONArray("products");
        for (int i = 0; i < productsJSONArray.length(); i++) {
			String currProductId = productsJSONArray.getString(i);
			Product product = productById.get(currProductId);
			if (product == null) {
				throw new PaylevenRESTException("The product with the ID '" + currProductId + "' cannot be associated with the category with the ID '" + id + "'.");
            }
			
			product.categories.add(category);
			category.products.add(product);
		}
        
        return category;
    }
	

}
