package test;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import com.simplydifferent.vo.Product;


public class MasterDAOTest {

	public static void main(String[] args) {
		Product p1 = new Product(), p2 = new Product();
		p1.setId(1);
		p2.setId(2);
		try {
			PropertyUtils.setProperty(p2, "id", PropertyUtils.getProperty(p1, "id"));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(p1.getId()+" "+p2.getId());
		
	}
}
