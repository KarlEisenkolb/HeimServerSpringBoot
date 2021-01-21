package com.pi.server.models.organisationsapp;

import com.pi.server.security_handling.Crypt;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.pi.server.security_handling.Crypt.CRYPT_USE_DEFAULT_KEY;

@Entity(name = FirebaseCrypt_Termin_entity.TableName)
@Table(name = FirebaseCrypt_Termin_entity.TableName)
public class FirebaseCrypt_Termin_entity {

    public final static String TableName = "organisationsapp_termin_crypt";

    public final static int TYPE_AUFGABE        = 0;
    public final static int TYPE_TERMIN         = 1;
    public final static int TYPE_GEBURTSTAG     = 2;
    public final static int TYPE_URLAUB         = 3;
    public final static int TYPE_SCHULFERIEN    = 4;
    public final static int TYPE_FEIERTRAG      = 5;

    public final static int REPETITION_SINGLE   = 0;
    public final static int REPETITION_DAY      = 1;
    public final static int REPETITION_WEEK     = 2;
    public final static int REPETITION_MONTH    = 3;
    public final static int REPETITION_YEAR     = 4;

    public final static int TASK_NOT_DONE       = 0; // wenn Task-Erledigungszeit nicht gesetzt

    public final static int TYPE_SECOND_PRIVAT         = 0;
    public final static int TYPE_SECOND_BERUFLICH      = 1;
    public final static int TYPE_SECOND_OTHER          = 3;

    public final static int IMPORTANCE_WICHTIG      = 0;
    public final static int IMPORTANCE_UNWICHTIG    = 1;

    public static final String START_TIME_IN_MILLIS         = "lRksIjfMsVs";
    public static final String END_TIME_IN_MILLIS_ON_DAY    = "iDhwMpxsHos";
    public static final String END_TIME_IN_MILLIS           = "pSqDjfpLRlf";
    public static final String ID                           = "mDkwpOsHXdk";
    public static final String NAME                         = "pKqSoynVsqp";
    public static final String DESCRIPTION                  = "rSlwbaTqdKs";
    public static final String BESITZER                     = "kWfpwsBSEsw";
    public static final String SHARED_TERMIN_NUTZER_LIST    = "gWoVdLmsswl";
    public static final String TYPE                         = "nGdfkDcnkDn";
    public static final String TYPE_SECOND                  = "wjWkFvpoASs";
    public static final String IMPORTANCE                   = "sdKwXpeIjns";
    public static final String WIEDERHOLUNGS_INTERVALL      = "pSwqbSJFfwf";
    public static final String TASK_ERLEDIGUNGSZEIT         = "pwKdIwldhHw";
    public static final String YEAR_OF_BIRTH                = "uRqLbXpUbDb";

    @Column(name="starttimemillis_utc")
    private long lRksIjfMsVs; // startTimeMillis UTC
    @Column(name="endtimemillisonday_utc")
    private String iDhwMpxsHos; // endTimeMillisOnDay UTC
    @Column(name="endtimemillis_utc")
    private long pSqDjfpLRlf; // endTimeMillis UTC
    @Id
    @Column(name="id_firebase")
    private String mDkwpOsHXdk; // id
    @Column(name="name")
    private String pKqSoynVsqp; // name
    @Column(name="description")
    private String rSlwbaTqdKs; // description
    @Column(name="besitzer")
    private String kWfpwsBSEsw; // besitzer
    @CollectionTable(name="organisationsapp_sharedterminnutzer")
    @ElementCollection(fetch=FetchType.EAGER)
    private List<String> gWoVdLmsswl = new ArrayList<>(); // sharedTerminNutzerList
    @Column(name="type")
    private long nGdfkDcnkDn; // type // Aufgabe, Termin, Urlaub, Feiertag, Schulferien, Geburtstag
    @Column(name="type_second")
    private String wjWkFvpoASs; // type_second // Privat, Beruflich
    @Column(name="importance")
    private String sdKwXpeIjns; // importance // Wichtig, Unwichtig
    @Column(name="wiederholungsintervall")
    private long pSwqbSJFfwf; // wiederholungsIntervall //wird im Server im Gegensatz zur App nicht verschlüsselt (für queries)
    @Column(name="erledigungsdatum")
    private long pwKdIwldhHw; // erledigungsdatum // Erledigungsdatum des Tasks UTC
    @Column(name="geburtsjahr")
    private String uRqLbXpUbDb; // Geburtsjahr für Type Geburtstag und Anzeigen des Alters am Geburtstag

    public long getlRksIjfMsVs() { return lRksIjfMsVs; }
    public String getiDhwMpxsHos() { return iDhwMpxsHos; }
    public long getpSqDjfpLRlf() { return pSqDjfpLRlf; }
    public String getmDkwpOsHXdk() { return mDkwpOsHXdk; }
    public String getpKqSoynVsqp() { return pKqSoynVsqp; }
    public String getrSlwbaTqdKs() { return rSlwbaTqdKs; }
    public String getkWfpwsBSEsw() { return kWfpwsBSEsw; }
    public List<String> getgWoVdLmsswl() { return gWoVdLmsswl; }
    public long getnGdfkDcnkDn() { return nGdfkDcnkDn; }
    public String getWjWkFvpoASs() { return wjWkFvpoASs; }
    public String getSdKwXpeIjns() { return sdKwXpeIjns; }
    public long getpSwqbSJFfwf() { return pSwqbSJFfwf; }
    public long getPwKdIwldhHw() { return pwKdIwldhHw; }
    public String getuRqLbXpUbDb() { return uRqLbXpUbDb; }

    public FirebaseCrypt_Termin_entity(){}

    public FirebaseCrypt_Termin_entity(long startTimeInMillis,
                                       long endTimeInMillisOnDay,
                                       long endTimeInMillis,
                                       String id,
                                       String name,
                                       String description,
                                       String besitzer,
                                       List<String> sharedTerminNutzerList,
                                       long type,
                                       long type_second,
                                       long importance,
                                       long wiederholungsIntervall,
                                       long geburtsjahr)
    {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.lRksIjfMsVs = startTimeInMillis;
        this.iDhwMpxsHos = crypt.encryptLong(endTimeInMillisOnDay);
        this.pSqDjfpLRlf = endTimeInMillis;
        this.mDkwpOsHXdk = crypt.encryptString(id);
        this.pKqSoynVsqp = crypt.encryptString(name);
        this.rSlwbaTqdKs = crypt.encryptString(description);
        this.kWfpwsBSEsw = crypt.encryptString(besitzer);
        for (String otherUser : sharedTerminNutzerList)
            this.gWoVdLmsswl.add(crypt.encryptString(otherUser));
        this.nGdfkDcnkDn = type;
        this.wjWkFvpoASs = crypt.encryptLong(type_second);
        this.sdKwXpeIjns = crypt.encryptLong(importance);
        this.pSwqbSJFfwf = wiederholungsIntervall;
        this.pwKdIwldhHw = 0; // erledigungsdatum // Erledigungsdatum des Tasks UTC // Initialisiert als NICHT erledigt
        this.uRqLbXpUbDb = crypt.encryptLong(geburtsjahr);
    }

    public void setlRksIjfMsVs(long lRksIjfMsVs) {
        this.lRksIjfMsVs = lRksIjfMsVs;
    }
    public void setiDhwMpxsHos(String iDhwMpxsHos) {
        this.iDhwMpxsHos = iDhwMpxsHos;
    }
    public void setpSqDjfpLRlf(long pSqDjfpLRlf) {
        this.pSqDjfpLRlf = pSqDjfpLRlf;
    }
    public void setmDkwpOsHXdk(String mDkwpOsHXdk) {
        this.mDkwpOsHXdk = mDkwpOsHXdk;
    }
    public void setpKqSoynVsqp(String pKqSoynVsqp) {
        this.pKqSoynVsqp = pKqSoynVsqp;
    }
    public void setrSlwbaTqdKs(String rSlwbaTqdKs) {
        this.rSlwbaTqdKs = rSlwbaTqdKs;
    }
    public void setkWfpwsBSEsw(String kWfpwsBSEsw) {
        this.kWfpwsBSEsw = kWfpwsBSEsw;
    }
    public void setgWoVdLmsswl(List<String> gWoVdLmsswl) {
        this.gWoVdLmsswl = gWoVdLmsswl;
    }
    public void setnGdfkDcnkDn(String nGdfkDcnkDn) {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.nGdfkDcnkDn = crypt.decryptLong(nGdfkDcnkDn);
    }
    public void setWjWkFvpoASs(String wjWkFvpoASs) {
        this.wjWkFvpoASs = wjWkFvpoASs;
    }
    public void setSdKwXpeIjns(String sdKwXpeIjns) {
        this.sdKwXpeIjns = sdKwXpeIjns;
    }
    public void setpSwqbSJFfwf(String pSwqbSJFfwf) {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.pSwqbSJFfwf = crypt.decryptLong(pSwqbSJFfwf);
    } //wird im Server im Gegensatz zur App nicht verschlüsselt (für queries)
    public void setPwKdIwldhHw(long pwKdIwldhHw) {
        this.pwKdIwldhHw = pwKdIwldhHw;
    }
    public void setuRqLbXpUbDb(String uRqLbXpUbDb) {
        this.uRqLbXpUbDb = uRqLbXpUbDb;
    }

    public long gibStartTimeInMillis(){
        return getlRksIjfMsVs(); }
    public long gibEndTimeInMillisOnDay(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptLong(getiDhwMpxsHos()); }
    public long gibEndTimeInMillis(){
        return getpSqDjfpLRlf(); }
    public String gibId(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptString(getmDkwpOsHXdk());}
    public String gibName(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptString(getpKqSoynVsqp());}
    public String gibDescription(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptString(getrSlwbaTqdKs());}
    public String gibBesitzer(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptString(getkWfpwsBSEsw());}
    public List<String> gibSharedTerminNutzerList()
    {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        List<String> sharedTerminNutzerList = new ArrayList<>();
        for (String otherUser : getgWoVdLmsswl())
            sharedTerminNutzerList.add(crypt.decryptString(otherUser));
        return sharedTerminNutzerList;
    }
    public long gibType(){
        return getnGdfkDcnkDn();}
    public long gibType_Second(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptLong(getWjWkFvpoASs());}
    public long gibImportance(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptLong(getSdKwXpeIjns());}
    public long gibWiederholungsIntervall(){
        return getpSwqbSJFfwf();}
    public long gibErledigungsTime(){
        return getPwKdIwldhHw();}
    public long gibGeburtsjahr(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptLong(getuRqLbXpUbDb());}
    //==========================================================================

    public void setzeStartTimeInMillis(long startTimeInMillis){
        this.lRksIjfMsVs = startTimeInMillis;
    }
    public void setzeEndTimeInMillisOnDay(long endTimeInMillisOnDay){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.iDhwMpxsHos = crypt.encryptLong(endTimeInMillisOnDay);
    }
    public void setzeEndTimeInMillis(long endTimeInMillis){
        this.pSqDjfpLRlf = endTimeInMillis;
    }
    public void setzeId(String id){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.mDkwpOsHXdk = crypt.encryptString(id);
    }
    public void setzeName(String name){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.pKqSoynVsqp = crypt.encryptString(name);
    }
    public void setzeDescription(String description){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.rSlwbaTqdKs = crypt.encryptString(description);
    }
    public void setzeBesitzer(String besitzer){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.kWfpwsBSEsw = crypt.encryptString(besitzer);
    }
    public void setzeSharedTerminNutzerList(List<String> sharedTerminNutzerList)
    {
        this.gWoVdLmsswl = new ArrayList<>();
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        for (String otherUser : sharedTerminNutzerList)
            this.gWoVdLmsswl.add(crypt.encryptString(otherUser));
    }
    public void setzeType(long type){
        this.nGdfkDcnkDn = type;
    }
    public void setzeType_Second(long type_second){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.wjWkFvpoASs = crypt.encryptLong(type_second);
    }
    public void setzeImportance(long importance){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.sdKwXpeIjns = crypt.encryptLong(importance);
    }
    public void setzeWiederholungsIntervall(long wiederholungsIntervall){
        this.pSwqbSJFfwf = wiederholungsIntervall;
    }
    public void setzeErledigungsTime(long erledigungszeit){
        this.pwKdIwldhHw = erledigungszeit;
    }
    public void setzeGeburtsjahr(long geburtsjahr){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.uRqLbXpUbDb = crypt.encryptLong(geburtsjahr);
    }

    @Override
    public String toString() {
        return    gibName() + " | "
                + gibDescription() + " | Besitzer: "
                + gibBesitzer() + " | Type: "
                + gibType() + " | Type Second: "
                + gibType_Second() + " | Importance: "
                + gibImportance() + " | Wiederholungsintervall: "
                + gibWiederholungsIntervall() + " | Startzeit: "
                + gibStartTimeInMillis() + " | Absolutendzeit: "
                + gibEndTimeInMillis() + " | Tagesendzeit: "
                + gibEndTimeInMillisOnDay() + " | Erledigungszeit: "
                + gibErledigungsTime() + " | ID: "
                + gibId() + " | Geburtsjahr: "
                + gibGeburtsjahr();
    }
}