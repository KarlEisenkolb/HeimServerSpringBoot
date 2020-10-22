package com.pi.server.Models.Organisationsapp;

import java.util.ArrayList;
import java.util.List;

public class Mav_DayMitTerminen {

    private long time_utc;
    private List<Mav_TerminDecrypted> terminlist = new ArrayList<>();

    public Mav_DayMitTerminen(){}

    public Mav_DayMitTerminen(long time_utc){
        this.time_utc = time_utc;
    }

    public void add_termin(Mav_TerminDecrypted termin){
        terminlist.add(termin);
    }

    public long getTime_utc() {
        return time_utc;
    }

    public void setTime_utc(long time_utc) {
        this.time_utc = time_utc;
    }

    public List<Mav_TerminDecrypted> getTerminlist() {
        return terminlist;
    }

    public void setTerminlist(List<Mav_TerminDecrypted> terminlist) {
        this.terminlist = terminlist;
    }
}
