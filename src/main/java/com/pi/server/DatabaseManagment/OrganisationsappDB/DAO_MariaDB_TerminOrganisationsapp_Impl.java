package com.pi.server.DatabaseManagment.OrganisationsappDB;

import com.pi.server.DatabaseManagment.DAO_Basic;
import com.pi.server.Models.Organisationsapp.Termin_FirebaseCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_TerminOrganisationsapp_Impl implements DAO_Basic<Termin_FirebaseCrypt>, DAO_Organisationsapp<Termin_FirebaseCrypt> {

    private final Logger log = LoggerFactory.getLogger(DAO_MariaDB_TerminOrganisationsapp_Impl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Termin_FirebaseCrypt get(long id) {
        return null;
    }

    @Override
    public Termin_FirebaseCrypt get(String id) {
        return entityManager.find(Termin_FirebaseCrypt.class, id);
    }

    @Override
    public Termin_FirebaseCrypt get_withNutzerName(String name) {
        return null;
    }

    @Override
    public List<Termin_FirebaseCrypt> getAll_withStartAndEndTime(long startTime, long endTime) {
        String queryString = "SELECT t FROM " + Termin_FirebaseCrypt.TableName +" t WHERE (t.lRksIjfMsVs <= " + endTime + " AND t.pSqDjfpLRlf >= " + startTime + ") OR  (t.pSwqbSJFfwf != " + Termin_FirebaseCrypt.REPETITION_SINGLE + " AND t.lRksIjfMsVs = t.pSqDjfpLRlf) ORDER BY t.nGdfkDcnkDn DESC, t.lRksIjfMsVs ASC";
        TypedQuery<Termin_FirebaseCrypt> query = entityManager.createQuery(queryString, Termin_FirebaseCrypt.class);
        return query.getResultList();
    }

    @Override
    public List<Termin_FirebaseCrypt> getAll() {
        return null;
    }

    @Transactional
    @Override
    public void save(Termin_FirebaseCrypt t_save) {
        Termin_FirebaseCrypt termin_found = entityManager.find(Termin_FirebaseCrypt.class, t_save.getmDkwpOsHXdk()); // with Crypted Id
        if (termin_found == null)
            entityManager.persist(t_save);
    }

    @Transactional
    @Override
    public void update(Termin_FirebaseCrypt t_alt, Termin_FirebaseCrypt t_neu) {
        entityManager.merge(t_neu);
    }

    @Transactional
    @Override
    public void delete(Termin_FirebaseCrypt t_del) {
        entityManager.remove(entityManager.contains(t_del) ? t_del : entityManager.merge(t_del));
    }
}
