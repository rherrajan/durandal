package tk.icudi.durandal.web;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");
    private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
    
    private EMF() {}

    public static EntityManagerFactory getEMF() {
        return emfInstance;
    }

    public static PersistenceManagerFactory getPMF() {
        return pmfInstance;
    }
}
