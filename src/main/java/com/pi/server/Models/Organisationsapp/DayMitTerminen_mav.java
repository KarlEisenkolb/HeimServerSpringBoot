package com.pi.server.Models.Organisationsapp;

import java.util.ArrayList;
import java.util.List;

public class DayMitTerminen_mav {

    private long time_utc;
    private List<TerminDecrypted_mav> terminlist = new ArrayList<>();

    public DayMitTerminen_mav(){}

    public DayMitTerminen_mav(long time_utc){
        this.time_utc = time_utc;
    }

    public void add_termin(TerminDecrypted_mav termin){
        terminlist.add(termin);
    }

    public long getTime_utc() {
        return time_utc;
    }

    public void setTime_utc(long time_utc) {
        this.time_utc = time_utc;
    }

    public List<TerminDecrypted_mav> getTerminlist() {
        return terminlist;
    }

    public void setTerminlist(List<TerminDecrypted_mav> terminlist) {
        this.terminlist = terminlist;
    }
}
