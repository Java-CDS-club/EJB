package edu.pjwstk.sri.lab2.test;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import edu.pjwstk.sri.lab2.model.Category;
import edu.pjwstk.sri.lab2.model.Product;
import edu.pjwstk.sri.lab2.dao.CategoryDao;
import edu.pjwstk.sri.lab2.dao.ProductDao;;

@Named("testBean")
@RequestScoped
public class TestBean implements Serializable {
	@PersistenceContext(unitName = "sri2-persistence-unit")
	private EntityManager em;
	
	@Inject
	private ProductDao productDao;
	@Inject
	private CategoryDao categoryDao;
	@Inject
	private Cart cart;
	
	public TestBean() {
	}
	
		private List<Product> products;
		private List<Category> categories;
		private String newProductName = "New Product";
		private String newCategoryName = "New Category";
		private Long idNewProduct, idNewCategory;
		
	public void test() {
		 products = productDao.listAll(0, 10);
		 categories = categoryDao.getCategories();

//DAO----------------------------------
		 
		//Listowanie wszystkich kategorii w bazie przy pomocy metody z CategoryDao
		printCategories();
			
		//Listowanie wszystkich produktow w bazie
		printProducts();
		
		//Dodawanie nowej kategorii
		idNewCategory = addCategory();
		
		//Edytowanie kategorii
		editCategory(idNewCategory);
		
		//Usuwanie kategorii
		deleteCategory(idNewCategory);
		
		//Dodawanie nowego produktu
		idNewProduct = addProduct();
		
		//Edytowanie produktu
		updateProduct(idNewProduct);
		
		//Usuwanie produktu
		deleteProduct(idNewProduct);
		
		
//ZAMOWIENIA---------------------------
	
		Product p1 = products.get(0);
		Product p2 = products.get(1);
		Product p3 = products.get(2);
		
		//Pierwsze zamowienie
		cart.addProductToCart(p1, 1);
		cart.addProductToCart(p2, 3);
		System.out.println("Cart:------------------------- ");
		cart.showCart();
		System.out.println("END Cart------------------------- ");
		
		cart.finishOrder();
			//po zlozeniu zamowienia koszyk powinien byc pusty
		System.out.println("Cart:------------------------- ");
		cart.showCart();
		System.out.println("END Cart------------------------- ");
		
		//Drugie zamowienie. Pierwszy produkt przekracza ilosc magazynowa
		cart.addProductToCart(p1, 5);
		cart.addProductToCart(p2, 3);
		
		System.out.println("Cart:------------------------- ");
		cart.showCart();
		System.out.println("END Cart------------------------- ");
		
		cart.finishOrder();
		
			//Koszyk powinien byc pelen, bo nie doszlo do finalizacji zamowienia
		System.out.println("Cart:------------------------- ");
		cart.showCart();
		System.out.println("END Cart------------------------- ");
	}
	
	private long addCategory(){
		Category newCategory = new Category();
		newCategory.setName(newCategoryName);
		newCategory.setParentCategory(categories.get(1));
		categoryDao.create(newCategory);
		
		printActualCategories();
		
		return newCategory.getId();
	}
	
	private void editCategory(long categoryId){
		Category category = categoryDao.findById(categoryId);
		category.setName("Nowa Kategoria");
		categoryDao.update(category);
		printActualCategories();
	}
	
	private void deleteCategory(long categoryId){
		categoryDao.deleteById(categoryId);
		printActualCategories();
	}
	
	private Long addProduct(){
		Product newProduct = new Product();
		newProduct.setCategory(categories.get(1));
		newProduct.setName(newProductName);
		newProduct.setStock(10);
		productDao.create(newProduct);
		products = productDao.listAll(0, 10);
		
		System.out.println("Products after create------------------------- ");
		for(int i = 0; i < products.size(); i++){
			System.out.println(products.get(i));
		}
		System.out.println("END Products------------------------- ");
		
		return newProduct.getId();
	}
	
	private void updateProduct(Long productId){
		Product product = productDao.findById(productId);
		product.setName("Nowy Produkt");
		productDao.update(product);
		printProducts();
	}
	
	private void deleteProduct(Long productId){
		productDao.deleteById(productId);
		products = productDao.listAll(0, 10);
		
		printProducts();
	}
	
	private void printCategories(){
		System.out.println("Categories------------------------- ");
		categories = categoryDao.getCategories();
		for(int i = 0; i < categories.size(); i++){
			System.out.println(categories.get(i));
		}
		System.out.println("END Categories------------------------- ");
	}
	
	private void printActualCategories(){
		//Dla uzytku testow pobieram rzeczywisty stan bazy, bez korzystania z buforowanej listy kategorii.
		System.out.println("Categories------------------------- ");
		categories = categoryDao.getCategories();
		TypedQuery<Category> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.parentCategory LEFT JOIN FETCH c.childCategories ORDER BY c.id",
						Category.class);
		System.out.println(findAllQuery.getResultList());
		System.out.println("END Categories------------------------- ");
	}
	
	private void printProducts(){
		System.out.println("Products------------------------- ");
		for(int i = 0; i < products.size(); i++){
			System.out.println(products.get(i));
		}
		System.out.println("END Products------------------------- ");
	}

}
