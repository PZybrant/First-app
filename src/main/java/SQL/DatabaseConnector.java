package SQL;

import JFXControllers.AlertController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class DatabaseConnector {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    public DatabaseConnector() {}

    public void createConnection(){
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("ClockworkPersistence");
            entityManager = entityManagerFactory.createEntityManager();
        } catch (Exception ex){
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}

