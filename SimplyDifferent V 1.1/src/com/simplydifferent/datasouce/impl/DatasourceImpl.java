package com.simplydifferent.datasouce.impl;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.simplydifferent.datasouce.intr.Datasource;

public class DatasourceImpl extends HibernateDaoSupport implements Datasource {

	@Override
	public HibernateTemplate getTemplate() {
		// TODO Auto-generated method stub
		
		return getHibernateTemplate();
	}

	@Override
	public void save(Object object) {
		getTemplate().save(object);
		getTemplate().flush();		
	}

	@Override
	public void saveOrUpdate(Object object) {
		getTemplate().saveOrUpdate(object);
		getTemplate().flush();
	}

	@Override
	public void update(Object object) {
		getTemplate().update(object);
		getTemplate().flush();
	}

	@Override
	public void delete(Object object) {
		getTemplate().delete(object);
		getTemplate().flush();
	}
	
	public Session getHibernateSession(){
		return getSession();
	}
}
