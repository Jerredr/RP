package be.kuleuven.researchpad.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import be.kuleuven.researchpad.domain.Base;

public abstract class GenericJpaDaoImpl<T extends Base, ID> implements GenericJpaDao<T, ID> {

	private Class<T> clazz;

	@PersistenceContext
	EntityManager em;

	/**
	 * Constructor.
	 */
	@SuppressWarnings("unchecked")
	public GenericJpaDaoImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.clazz = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

	/**
	 * {@inheritDoc}
	 */
	public T findById(final ID id) {
		return this.em.find(this.clazz, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return this.em.createQuery("FROM " + this.clazz.getName()).getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	public void create(final T entity) {
		this.em.persist(entity);
		em.flush();
		em.refresh(entity);
	}

	/**
	 * {@inheritDoc}
	 */
	public T update(final T entity) {
		return this.em.merge(entity);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(final T entity) {
		this.em.remove(entity);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(final ID entityId) {
		final T entity = this.findById(entityId);

		this.delete(entity);
	}
}