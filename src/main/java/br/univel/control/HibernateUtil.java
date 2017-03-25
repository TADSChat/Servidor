package br.univel.control;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Carregar hibernate
 *
 * @author Eduardo
 *
 */
public final class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static StandardServiceRegistry registry;

	private HibernateUtil() {

	}

	/**
	 * criando sessao hibernate
	 *
	 * @return
	 */
	private static SessionFactory buildSessionFactory() {

		registry = new StandardServiceRegistryBuilder().configure().build();

		SessionFactory sessionFactory = null;
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();
		}
		return sessionFactory;
	}

	/**
	 * get do singleton
	 *
	 * @return
	 */
	public static synchronized Session getSession() {
		if (sessionFactory == null) {
			sessionFactory = buildSessionFactory();
		}
		return sessionFactory.openSession();
	}

	/**
	 * Destruir sessao do hibernate
	 */
	public static void killSession() {
		StandardServiceRegistryBuilder.destroy(registry);
	}
}