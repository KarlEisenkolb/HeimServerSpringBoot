package com.pi.server.Models.Organisationsapp;

import com.pi.server.SecurityHandling.Crypt;

import java.util.ArrayList;
import java.util.List;

import static com.pi.server.SecurityHandling.Crypt.CRYPT_USE_DEFAULT_KEY;

public class FirebaseCrypt_Nutzer {

    public static final String NO_TOKEN = "no_token";

    public static final String NAME                     = "oSodwHoHSmx";
    public static final String ID                       = "kHObgqSdsno";
    public static final String TOKEN_MESSAGING_LIST     = "iFwpcRndwlS";

    private String oSodwHoHSmx; // Name
    private String kHObgqSdsno; // document ID
    private List<String> iFwpcRndwlS; // token für Firebase Messaging (Liste weil ein Nutzer mehrere Devices haben kann)

    public FirebaseCrypt_Nutzer(){}

    public FirebaseCrypt_Nutzer(String name, String id){
        Crypt cryptNormal = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.oSodwHoHSmx = cryptNormal.encryptString(name);
        this.kHObgqSdsno = cryptNormal.encryptString(id);
        this.iFwpcRndwlS = new ArrayList<>();
    }

    public String getoSodwHoHSmx() { return oSodwHoHSmx; }
    public String getkHObgqSdsno() { return kHObgqSdsno; }
    public List<String> getiFwpcRndwlS() { return iFwpcRndwlS; }

    public String gibName() {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptString(getoSodwHoHSmx());
    }

    public String gibId() {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        return crypt.decryptString(getkHObgqSdsno());
    }

    public List<String> gibFirebaseMessagingNutzerTokenList() {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        List<String> tokenList = new ArrayList<>();
        for (String token : getiFwpcRndwlS())
            tokenList.add(crypt.decryptString(token));
        return tokenList;
    }

    public void setzeName(String name) {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.oSodwHoHSmx = crypt.encryptString(name);
    }

    public void setzeId(String id) {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        this.kHObgqSdsno = crypt.encryptString(id);
    }

    public void setzeFirebaseMessagingNutzerTokenList(List<String> tokenList) {
        this.iFwpcRndwlS = new ArrayList<>();
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        for (String token : tokenList)
            this.iFwpcRndwlS.add(crypt.encryptString(token));
    }
}