package be.kuleuven.researchpad.dao;

import java.util.List;

import be.kuleuven.researchpad.domain.Base;

/**
 * Interface to define generic database operations.
 *
 * @param <T>
 */

public interface GenericJpaDao<T extends Base, ID> {

	/**
	 * Get the object by id.
	 * 
	 * @param id 
	 * 			The id of the object.
	 * @return The object.
	 */
	public abstract T findById(final ID id);

	/**
	 * Get all objects in the database.
	 * 
	 * @return {@link List}
	 * 			List of all the objects.
	 */
	public abstract List<T> findAll();

	/**
	 * Create a new object in the database.
	 * 
	 * @param entity 
	 * 			The object that needs to be created.
	 */
	public abstract void create(final T entity);

	/**
	 * Update an existing object in the database.
	 * 
	 * @param entity
	 * 			The object that needs to be updated.
	 * @return entity
	 * 			The updated object.
	 */
	public abstract T update(final T entity);

	/**
	 * Delete an object in the database.
	 * 
	 * @param entity
	 * 			The object that needs to be deleted.
	 */
	public abstract void delete(final T entity);

	/**
	 * Delete an object in the database by id.
	 * 
	 * @param entityId
	 * 			The id of the object that needs to be deleted.
	 */
	public abstract void deleteById(final ID entityId);

}