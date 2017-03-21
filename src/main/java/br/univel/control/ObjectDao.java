package br.univel.control;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

/**
 * Dao de objetos do hibernate
 *
 * @author Eduardo
 *
 */
public final class ObjectDao {
	private static Session session;
	private static ObjectDao objetoDao;

	private ObjectDao() {

	}

	/**
	 * Singleton do DAO
	 *
	 * @return
	 */
	public static synchronized ObjectDao getObjectDao() {
		if (objetoDao == null) {
			objetoDao = new ObjectDao();
		}
		return objetoDao;
	}

	/**
	 * Retorna a session
	 *
	 * @return
	 */
	public static Session getSession() {
		return session;
	}

	/**
	 * incluir objeto
	 *
	 * @param objetoDao
	 */
	public static void incluir(final Object objetoDao) {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.save(objetoDao);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new RuntimeException(e);
		} finally {
			session.close();
		}
	}

	/**
	 * Alterar objeto
	 *
	 * @param objetoDao
	 */
	public static void alterar(final Object objetoDao) {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(objetoDao);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new RuntimeException(e);
		} finally {
			session.close();
		}
	}

	/**
	 * excluir objeto
	 *
	 * @param objetoDao
	 */
	public static void excluir(final Object objetoDao) {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.delete(objetoDao);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new RuntimeException(e);
		} finally {
			session.close();
		}
	}

	/**
	 * executar uma query
	 *
	 * @param parameterQuery
	 * @return
	 */
	public static Object consultarByQuery(final String parameterQuery) {
		Object objeto = null;
		try {
			session = HibernateUtil.getSession();
			objeto = session.createQuery(parameterQuery).uniqueResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			session.close();
		}
		return objeto;
	}

	/**
	 * executar uma query
	 *
	 * @param parameterQuery
	 * @return
	 */
	public static List<?> listar(final String parameterQuery) {
		List<Object> lista = new ArrayList<Object>();
		try {
			session = HibernateUtil.getSession();
			lista = session.createQuery(parameterQuery).list();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			session.close();
		}
		return lista;
	}
}
