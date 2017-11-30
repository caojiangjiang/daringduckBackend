package cn.daringduck.communitybuilder.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Adds a get item and getPage to all the services
 * @author jochem
 *
 * @param <T>
 * @param <ID>
 */
public abstract class GenericService <T, ID extends Serializable> {
	public static final int PAGE_SIZE = 25;

	private final JpaRepository<T, ID> repo;

	public GenericService(JpaRepository<T, ID> repo) {
		this.repo = repo;
	}
	
	// Methods
	public T get(ID id) {
		return repo.findOne(id);
	}
	
	public Page<T> getPage(int page) {
		return repo.findAll(new PageRequest(page, PAGE_SIZE));
	}
	
}
