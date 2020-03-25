package edu.pjwstk.sri.lab2.test;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("testBean")
@RequestScoped
public class TestBean implements Serializable {

//	@Inject
//	private CategoryDao catService;
	
	public TestBean() {
	}
	
	public void test() {
//		List<Category> listAll = catService.listAll(null, null);
//		System.out.println(listAll);
	}

}
