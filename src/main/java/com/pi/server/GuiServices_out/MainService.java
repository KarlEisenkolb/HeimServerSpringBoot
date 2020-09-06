package com.pi.server.GuiServices_out;

import com.pi.server.DatabaseManagment.PersistingService;
import com.pi.server.Models.Organisationsapp.DayMitTerminen_mav;
import com.pi.server.Models.Organisationsapp.TerminDecrypted_mav;
import com.pi.server.Models.Organisationsapp.Termin_FirebaseCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

    @Autowired
    private PersistingService persistingService;

    public final int DATE_SHORT = 0;
    public final int DATE_LONG = 1;

    public MainService(){}

    public String getTimeAndDateString(int formfactor){
        SimpleDateFormat simpleDateFormat;
        if(formfactor == DATE_SHORT)
            simpleDateFormat = new SimpleDateFormat("'Uhr |' EEE d MMM ");
        else
            simpleDateFormat = new SimpleDateFormat("HH:mm 'Uhr |' EEE d MMM ");

        return simpleDateFormat.format(System.currentTimeMillis());
    }

    public List<String> getDateHeadingStrings(int anzahl_Tageskacheln){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE d MMM");

        ZonedDateTime todayDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        List<String> dateList = new ArrayList<>();
        for(int i = 1; i <= anzahl_Tageskacheln; i++)
            dateList.add(simpleDateFormat.format(todayDate.plus(i, ChronoUnit.DAYS).toInstant().toEpochMilli()));

        return dateList;
    }

    public List getCurrentWeatherContent() {
        return persistingService.getAll(PersistingService.CurrentWeather);
    }

    public List getWeatherHourlyForecastContent(){
        return persistingService.getAll(PersistingService.HourlyWeather);
    }

    public List getWeatherDailyForecastContent(){
        return persistingService.getAll(PersistingService.DailyWeather);
    }

    public List getTermineForDaysToDisplay(int anzahl_Tageskacheln){
        ZonedDateTime todayDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        List<DayMitTerminen_mav> daylist = new ArrayList<>();
        daylist.add(new DayMitTerminen_mav(todayDate.toInstant().toEpochMilli()));
        for(int i = 1; i <= anzahl_Tageskacheln; i++)
            daylist.add(new DayMitTerminen_mav(todayDate.plus(i, ChronoUnit.DAYS).toInstant().toEpochMilli()));

        setTermineInDayList(daylist);
        return daylist;
    }

    private void setTermineInDayList(List<DayMitTerminen_mav> daylist){
        long lastDayInDayListPlusOneDayInMillis = ZonedDateTime.ofInstant(Instant.ofEpochMilli(daylist.get(daylist.size()-1).getTime_utc()), ZoneId.systemDefault()).plus(1, ChronoUnit.DAYS).toInstant().toEpochMilli(); // der Endzeitpunkt soll auch Termin beeinhalten die nach 0:00 des Endtages starten
        List<Termin_FirebaseCrypt> completeTerminList = (List<Termin_FirebaseCrypt>)(Object) persistingService.getAllTermineInTimeframe(daylist.get(0).getTime_utc(), lastDayInDayListPlusOneDayInMillis);

        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'Uhr |' EEE d MMM ");
        //for(Termin_FirebaseCrypt termin : completeTerminList) //debugging listoutput
            //System.out.println(termin.gibName() +"|"+ sdf.format(termin.gibStartTimeInMillis()));

        long todayInMillis = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(); // today in Millis UTC
        for (DayMitTerminen_mav day : daylist){
            long currentDayInMillis = day.getTime_utc();
            long nextDayInMillis = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currentDayInMillis), ZoneId.systemDefault()).plus(1, ChronoUnit.DAYS).toInstant().toEpochMilli();

            for (Termin_FirebaseCrypt termin :completeTerminList) {
                if (currentDayInMillis <= termin.gibStartTimeInMillis() && nextDayInMillis > termin.gibStartTimeInMillis()){
                    day.add_termin(prepareTerminToDisplay(termin, day));
                }else if(termin.gibWiederholungsIntervall() != Termin_FirebaseCrypt.REPETITION_SINGLE && termin.gibStartTimeInMillis() == termin.gibEndTimeInMillis()){
                    addingRepetitionTerminHandling(day, termin);
                }else if(termin.gibType() == Termin_FirebaseCrypt.TYPE_AUFGABE){
                    if (todayInMillis == currentDayInMillis && termin.gibStartTimeInMillis() <= currentDayInMillis && termin.gibErledigungsTime() == Termin_FirebaseCrypt.TASK_NOT_DONE)
                        day.add_termin(prepareTerminToDisplay(termin, day));
                    else if(currentDayInMillis <= termin.gibErledigungsTime() && nextDayInMillis > termin.gibErledigungsTime())
                        day.add_termin(prepareTerminToDisplay(termin, day));
                }
            }
        }
    }

    private void addingRepetitionTerminHandling(DayMitTerminen_mav day, Termin_FirebaseCrypt termin) {
        ZonedDateTime zdt_currentDay = ZonedDateTime.ofInstant(Instant.ofEpochMilli(day.getTime_utc()), ZoneId.systemDefault());
        ZonedDateTime zdt_currentTermin = ZonedDateTime.ofInstant(Instant.ofEpochMilli(termin.gibStartTimeInMillis()), ZoneId.systemDefault());

        switch ((int) termin.gibWiederholungsIntervall()) {
            case (int) Termin_FirebaseCrypt.REPETITION_DAY:
                day.add_termin(prepareTerminToDisplay(termin, day));
                break;
            case (int) Termin_FirebaseCrypt.REPETITION_WEEK:
                if (zdt_currentTermin.getDayOfWeek() == zdt_currentDay.getDayOfWeek()) {
                    day.add_termin(prepareTerminToDisplay(termin, day));
                }
                break;
            case (int) Termin_FirebaseCrypt.REPETITION_MONTH:
                if (zdt_currentTermin.getDayOfMonth() == zdt_currentDay.getDayOfMonth()) {
                    day.add_termin(prepareTerminToDisplay(termin, day));
                }
                break;
            case (int) Termin_FirebaseCrypt.REPETITION_YEAR:
                if (zdt_currentTermin.getDayOfMonth() == zdt_currentDay.getDayOfMonth() && zdt_currentTermin.getMonth() == zdt_currentDay.getMonth()) {
                    day.add_termin(prepareTerminToDisplay(termin, day));
                }
                break;
        }
    }

    private TerminDecrypted_mav prepareTerminToDisplay(Termin_FirebaseCrypt termin, DayMitTerminen_mav currentDay){
        long currentDayInMillis = currentDay.getTime_utc();

        return new TerminDecrypted_mav(
                getUhrzeitString(termin),
                getNameString(termin, currentDayInMillis),
                termin.gibDescription(),
                termin.gibType(),
                termin.gibErledigungsTime()
        );
    }

    private String getNameString(Termin_FirebaseCrypt termin, long currentDayInMillis) {
        String name;
        if (termin.gibType() == Termin_FirebaseCrypt.TYPE_GEBURTSTAG){
            LocalDate currentGeburtstag = Instant.ofEpochMilli(currentDayInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate geburtstag        = LocalDate.of((int) termin.gibGeburtsjahr(), currentGeburtstag.getMonth(), currentGeburtstag.getDayOfMonth());
            Period period = Period.between(geburtstag, currentGeburtstag);
            name = termin.gibName() + " " + period.getYears() + "ter";
        } else
            name = termin.gibName();
        return name;
    }

    private String getUhrzeitString(Termin_FirebaseCrypt termin) {
        long terminStartTime                = termin.gibStartTimeInMillis();
        long terminEndTimeOnDay             = termin.gibEndTimeInMillisOnDay();

        ZonedDateTime cStart = ZonedDateTime.ofInstant(Instant.ofEpochMilli(terminStartTime), ZoneId.systemDefault());
        ZonedDateTime cEnd = ZonedDateTime.ofInstant(Instant.ofEpochMilli(terminEndTimeOnDay), ZoneId.systemDefault());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        String uhrzeit;
        if (cStart.getHour() == 0 && cStart.getMinute() == 0 && cEnd.getHour() == 0 && cEnd.getMinute() == 0) //Falls Start und Endzeit = Default (O Uhr) dann nicht anzeigen
            uhrzeit = "";
        else if(cEnd.getHour() == 0 && cEnd.getMinute() == 0) // Startzeit am Tag gesetzt aber Endzeit ist Default => Nur Startzeit zeigen
            uhrzeit = timeFormatter.format(termin.gibStartTimeInMillis());
        else
            uhrzeit = timeFormatter.format(termin.gibStartTimeInMillis()) + "-" + timeFormatter.format(termin.gibEndTimeInMillisOnDay());
        return uhrzeit;
    }
}
