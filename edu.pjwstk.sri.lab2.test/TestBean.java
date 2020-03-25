package edu.pjwstk.sri.lab2.test;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.pjwstk.sri.lab2.model.Category;
import edu.pjwstk.sri.lab2.model.Product;
import edu.pjwstk.sri.lab2.dao.CategoryDao;
import edu.pjwstk.sri.lab2.dao.ProductDao;;

@Named("testBean")
@RequestScoped
public class TestBean implements Serializable {
	
	@Inject
	private ProductDao productDao;
	@Inject
	private CategoryDao categoryDao;
	@Inject
	private Cart shoppingCart;
	
	public TestBean() {
	}
	
	public void test() {
		List<Product> products = productDao.listAll(0, 10);
		List<Category> categories = categoryDao.getCategories();

		System.out.println("Products------------------------- ");
		for(int i = 0; i < products.size(); i++){
			System.out.println(products.get(i));
		}
		System.out.println("END Products------------------------- ");

		System.out.println("Categories------------------------- ");
		for(int i = 0; i < categories.size(); i++){
			System.out.println(categories.get(i));
		}
		System.out.println("END Categories------------------------- ");
		
	}

}
