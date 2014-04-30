package hu.gcs.example.upgrade.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class BookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(final Book book) {
        entityManager.persist(book);
    }

}
