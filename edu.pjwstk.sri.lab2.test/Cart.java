package edu.pjwstk.sri.lab2.test;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import edu.pjwstk.sri.lab2.dao.ProductDao;
import edu.pjwstk.sri.lab2.dto.OrderItem;
import edu.pjwstk.sri.lab2.model.Product;

@Stateful
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class Cart {
	@Inject
	private ProductDao products;
	@Inject
	private OrderItem orderItem;
	@Resource
	private EJBContext ejbContext;
	
	private List<OrderItem> items = new ArrayList<OrderItem>();
	
	public void addProductToCart(Product product, int amount){
		orderItem = new OrderItem();
		orderItem.setProduct(product);
		orderItem.setAmount(amount);
		items.add(orderItem);
	}
	
	public void removeProductFromCart(Product product){
		items.remove(product);
	}
	
	public void removeCart(){
		items.clear();
	}
	
	public void showCart() {
		for(int i = 0; i < items.size(); i++){
			System.out.println(items.get(i).getProduct().getName() + ", amount: " + items.get(i).getAmount());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void finishOrder(){
		Product product;
		int stock;
		for(int i = 0; i < items.size(); i++){
			product = products.findById(items.get(i).getProduct().getId());
			stock = product.getStock() - items.get(i).getAmount();
			
			if(stock < 0){
				ejbContext.setRollbackOnly();
				return;
			}else{
				product.setStock(stock);
				products.update(product);
			}
		}
		removeCart();
	}
}
