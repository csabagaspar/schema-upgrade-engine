package hu.gcs.example.upgrade.engine.resource;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManegerResource {

    @Produces
    @PersistenceContext
    private EntityManager entityManager;
}
