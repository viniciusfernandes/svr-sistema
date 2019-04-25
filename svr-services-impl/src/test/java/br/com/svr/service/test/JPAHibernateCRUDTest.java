package br.com.svr.service.test;

import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;

import br.com.svr.service.entity.Material;

public class JPAHibernateCRUDTest extends JPAHibernateTest {

	@Test
	public void testGetObjectById_success() {
		Material m = em.find(Material.class, 1);
		assertNotNull(m);
	}

}