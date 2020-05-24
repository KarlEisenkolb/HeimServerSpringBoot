package com.pi.server.Models.Organisationsapp;

import com.pi.server.SecurityHandling.Crypt;

import java.util.ArrayList;
import java.util.List;

import static com.pi.server.SecurityHandling.Crypt.CRYPT_USE_DEFAULT_KEY;

public class Termin_FirebaseCrypt {

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

    private long lRksIjfMsVs; // startTimeMillis UTC
    private String iDhwMpxsHos; // endTimeMillisOnDay UTC
    private long pSqDjfpLRlf; // endTimeMillis UTC
    private String mDkwpOsHXdk; // id
    private String pKqSoynVsqp; // name
    private String rSlwbaTqdKs; // description
    private String kWfpwsBSEsw; // besitzer
    private List<String> gWoVdLmsswl = new ArrayList<>(); // sharedTerminNutzerList
    private String nGdfkDcnkDn; // type // Aufgabe, Termin, Urlaub, Feiertag, Schulferien, Geburtstag
    private String wjWkFvpoASs; // type_second // Privat, Beruflich
    private String sdKwXpeIjns; // importance // Wichtig, Unwichtig
    private String pSwqbSJFfwf; // wiederholungsIntervall
    private long pwKdIwldhHw; // erledigungsdatum // Erledigungsdatum des Tasks UTC
    private String uRqLbXpUbDb; // Geburtsjahr für Type Geburtstag und Anzeigen des Alters am Geburtstag

    public long getlRksIjfMsVs() { return lRksIjfMsVs; }
    public String getiDhwMpxsHos() { return iDhwMpxsHos; }
    public long getpSqDjfpLRlf() { return pSqDjfpLRlf; }
    public String getmDkwpOsHXdk() { return mDkwpOsHXdk; }
    public String getpKqSoynVsqp() { return pKqSoynVsqp; }
    public String getrSlwbaTqdKs() { return rSlwbaTqdKs; }
    public String getkWfpwsBSEsw() { return kWfpwsBSEsw; }
    public List<String> getgWoVdLmsswl() { return gWoVdLmsswl; }
    public String getnGdfkDcnkDn() { return nGdfkDcnkDn; }
    public String getwjWkFvpoASs() { return wjWkFvpoASs; }
    public String getsdKwXpeIjns() { return sdKwXpeIjns; }
    public String getpSwqbSJFfwf() { return pSwqbSJFfwf; }
    public long getpwKdIwldhHw() { return pwKdIwldhHw; }
    public String getuRqLbXpUbDb() { return uRqLbXpUbDb; }

    public Termin_FirebaseCrypt(){}

    public Termin_FirebaseCrypt(long startTimeInMillis,
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
        this.nGdfkDcnkDn = crypt.encryptLong(type);
        this.wjWkFvpoASs = crypt.encryptLong(type_second);
        this.sdKwXpeIjns = crypt.encryptLong(importance);
        this.pSwqbSJFfwf = crypt.encryptLong(wiederholungsIntervall);
        this.pwKdIwldhHw = 0; // erledigungsdatum // Erledigungsdatum des Tasks UTC // Initialisiert als NICHT erledigt
        this.uRqLbXpUbDb = crypt.encryptLong(geburtsjahr);
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
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptLong(getnGdfkDcnkDn());}
    public long gibType_Second(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptLong(getwjWkFvpoASs());}
    public long gibImportance(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptLong(getsdKwXpeIjns());}
    public long gibWiederholungsIntervall(){
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptLong(getpSwqbSJFfwf());}
    public long gibErledigungsTime(){
        return getpwKdIwldhHw();}
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
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.nGdfkDcnkDn = crypt.encryptLong(type);
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
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.pSwqbSJFfwf = crypt.encryptLong(wiederholungsIntervall);
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