package com.pi.server.DatabaseManagment.OrganisationsappDB;

import com.pi.server.DatabaseManagment.DAO_Basic;
import com.pi.server.Models.Organisationsapp.FirebaseCrypt_Termin_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DAO_MariaDB_TerminOrganisationsapp_Impl implements DAO_Basic<FirebaseCrypt_Termin_entity>, DAO_Organisationsapp<FirebaseCrypt_Termin_entity> {

    private final Logger log = LoggerFactory.getLogger(DAO_MariaDB_TerminOrganisationsapp_Impl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public FirebaseCrypt_Termin_entity get(long id) {
        return null;
    }

    @Override
    public FirebaseCrypt_Termin_entity get(String id) {
        return entityManager.find(FirebaseCrypt_Termin_entity.class, id);
    }

    @Override
    public FirebaseCrypt_Termin_entity get_withNutzerName(String name) {
        return null;
    }

    @Override
    public List<FirebaseCrypt_Termin_entity> getAll_withStartAndEndTime(long startTime, long endTime) {
        //String queryString = "SELECT t FROM " + Termin_FirebaseCrypt.TableName +" t WHERE (t.lRksIjfMsVs <= " + endTime + " AND t.pSqDjfpLRlf >= " + startTime + ") OR  (t.pSwqbSJFfwf != " + Termin_FirebaseCrypt.REPETITION_SINGLE + " AND t.lRksIjfMsVs = t.pSqDjfpLRlf) OR (t.nGdfkDcnkDn = " + Termin_FirebaseCrypt.TYPE_AUFGABE + " AND t.lRksIjfMsVs <= " + endTime + " AND (t.pwKdIwldhHw = " + Termin_FirebaseCrypt.TASK_NOT_DONE + " OR (t.pwKdIwldhHw <= " + endTime + " AND t.pwKdIwldhHw >= " + startTime + "))) ORDER BY t.nGdfkDcnkDn DESC, t.lRksIjfMsVs ASC";

        String terminIstInnerhalbDesZeitintervalls = "(t.lRksIjfMsVs <= " + endTime + " AND t.pSqDjfpLRlf >= " + startTime + ")";
        String terminWirdWiederholtUndStartzeitGleichEndzeit = "(t.pSwqbSJFfwf != " + FirebaseCrypt_Termin_entity.REPETITION_SINGLE + " AND t.lRksIjfMsVs = t.pSqDjfpLRlf)";

        String taskNichtErledigtOderErledigungszeitInnerhalbDesZeitintervalls = "(t.pwKdIwldhHw = " + FirebaseCrypt_Termin_entity.TASK_NOT_DONE + " OR (t.pwKdIwldhHw <= " + endTime + " AND t.pwKdIwldhHw >= " + startTime + "))";
        String terminIstTaskUndStartZeitKleinerIntervallEndZeitUndNichtErledigt = "(t.nGdfkDcnkDn = " + FirebaseCrypt_Termin_entity.TYPE_AUFGABE + " AND t.lRksIjfMsVs <= " + endTime + " AND " + taskNichtErledigtOderErledigungszeitInnerhalbDesZeitintervalls + ")";

        String whereClause =  terminIstInnerhalbDesZeitintervalls + " OR " + terminWirdWiederholtUndStartzeitGleichEndzeit + " OR " + terminIstTaskUndStartZeitKleinerIntervallEndZeitUndNichtErledigt;
        String queryString = "SELECT t FROM " + FirebaseCrypt_Termin_entity.TableName +" t WHERE " + whereClause + " ORDER BY t.nGdfkDcnkDn DESC, t.lRksIjfMsVs ASC";

        TypedQuery<FirebaseCrypt_Termin_entity> query = entityManager.createQuery(queryString, FirebaseCrypt_Termin_entity.class);
        return query.getResultList();
    }

    @Override
    public List<FirebaseCrypt_Termin_entity> getAll() {
        return null;
    }

    @Transactional
    @Override
    public void save(FirebaseCrypt_Termin_entity t_save) {
        FirebaseCrypt_Termin_entity termin_found = entityManager.find(FirebaseCrypt_Termin_entity.class, t_save.getmDkwpOsHXdk()); // with Crypted Id
        if (termin_found == null)
            entityManager.persist(t_save);
    }

    @Transactional
    @Override
    public void update(FirebaseCrypt_Termin_entity t_alt, FirebaseCrypt_Termin_entity t_neu) {
        entityManager.merge(t_neu);
    }

    @Transactional
    @Override
    public void delete(FirebaseCrypt_Termin_entity t_del) {
        entityManager.remove(entityManager.contains(t_del) ? t_del : entityManager.merge(t_del));
    }
}
