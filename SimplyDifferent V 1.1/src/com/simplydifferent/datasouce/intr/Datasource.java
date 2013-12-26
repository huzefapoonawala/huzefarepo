package com.simplydifferent.datasouce.intr;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

public interface Datasource {
	
	public HibernateTemplate getTemplate();
	
	public void save(Object object);
	
	public void update(Object object);
	
	public void saveOrUpdate(Object object);
	
	public void delete(Object object);
	
	public Session getHibernateSession();
}
