package com.pi.server.GuiServices_out;

import com.pi.server.DatabaseManagment.PersistingService_Organisationsapp;
import com.pi.server.DatabaseManagment.PersistingService_Weather;
import com.pi.server.Models.Organisationsapp.Mav_DayMitTerminen;
import com.pi.server.Models.Organisationsapp.Mav_TerminDecrypted;
import com.pi.server.Models.Organisationsapp.FirebaseCrypt_Termin_entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.pi.server.DatabaseManagment.PersistingService_Organisationsapp.NutzerOrganisationsapp_Termin;
import static com.pi.server.DatabaseManagment.PersistingService_Weather.*;

@Service
public class MainService {

    @Autowired
    private PersistingService_Weather persistingService_weather;
    @Autowired
    private PersistingService_Organisationsapp persistingService_orgaApp;

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

    public Object getLatestCurrentWeather() {
        return persistingService_weather.getLastItem(CurrentWeather);
    }

    public List getWeatherHourlyForecastContent(){
        return persistingService_weather.getAll(HourlyWeather);
    }

    public List getWeatherDailyForecastContent(){
        return persistingService_weather.getAll(DailyWeather);
    }

    public List getTermineForDaysToDisplay(int anzahl_Tageskacheln){
        ZonedDateTime todayDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        List<Mav_DayMitTerminen> daylist = new ArrayList<>();
        daylist.add(new Mav_DayMitTerminen(todayDate.toInstant().toEpochMilli()));
        for(int i = 1; i <= anzahl_Tageskacheln; i++)
            daylist.add(new Mav_DayMitTerminen(todayDate.plus(i, ChronoUnit.DAYS).toInstant().toEpochMilli()));

        setTermineInDayList(daylist);
        return daylist;
    }

    private void setTermineInDayList(List<Mav_DayMitTerminen> daylist){
        long lastDayInDayListPlusOneDayInMillis = ZonedDateTime.ofInstant(Instant.ofEpochMilli(daylist.get(daylist.size()-1).getTime_utc()), ZoneId.systemDefault()).plus(1, ChronoUnit.DAYS).toInstant().toEpochMilli(); // der Endzeitpunkt soll auch Termin beeinhalten die nach 0:00 des Endtages starten
        List<FirebaseCrypt_Termin_entity> completeTerminList = (List<FirebaseCrypt_Termin_entity>)(Object) persistingService_orgaApp.getAll_withStartAndEndTime(NutzerOrganisationsapp_Termin, daylist.get(0).getTime_utc(), lastDayInDayListPlusOneDayInMillis);

        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'Uhr |' EEE d MMM ");
        //for(Termin_FirebaseCrypt termin : completeTerminList) //debugging listoutput
            //System.out.println(termin.gibName() +"|"+ sdf.format(termin.gibStartTimeInMillis()));

        long todayInMillis = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(); // today in Millis UTC
        for (Mav_DayMitTerminen day : daylist){
            long currentDayInMillis = day.getTime_utc();
            long nextDayInMillis = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currentDayInMillis), ZoneId.systemDefault()).plus(1, ChronoUnit.DAYS).toInstant().toEpochMilli();

            for (FirebaseCrypt_Termin_entity termin :completeTerminList) {
                if (currentDayInMillis <= termin.gibStartTimeInMillis() && nextDayInMillis > termin.gibStartTimeInMillis()){
                    day.add_termin(prepareTerminToDisplay(termin, day));
                }else if(termin.gibWiederholungsIntervall() != FirebaseCrypt_Termin_entity.REPETITION_SINGLE && termin.gibStartTimeInMillis() == termin.gibEndTimeInMillis()){
                    addingRepetitionTerminHandling(day, termin);
                }else if(termin.gibType() == FirebaseCrypt_Termin_entity.TYPE_AUFGABE){
                    if (todayInMillis == currentDayInMillis && termin.gibStartTimeInMillis() <= currentDayInMillis && termin.gibErledigungsTime() == FirebaseCrypt_Termin_entity.TASK_NOT_DONE)
                        day.add_termin(prepareTerminToDisplay(termin, day));
                    else if(currentDayInMillis <= termin.gibErledigungsTime() && nextDayInMillis > termin.gibErledigungsTime())
                        day.add_termin(prepareTerminToDisplay(termin, day));
                }
            }
        }
    }

    private void addingRepetitionTerminHandling(Mav_DayMitTerminen day, FirebaseCrypt_Termin_entity termin) {
        ZonedDateTime zdt_currentDay = ZonedDateTime.ofInstant(Instant.ofEpochMilli(day.getTime_utc()), ZoneId.systemDefault());
        ZonedDateTime zdt_currentTermin = ZonedDateTime.ofInstant(Instant.ofEpochMilli(termin.gibStartTimeInMillis()), ZoneId.systemDefault());

        switch ((int) termin.gibWiederholungsIntervall()) {
            case (int) FirebaseCrypt_Termin_entity.REPETITION_DAY:
                day.add_termin(prepareTerminToDisplay(termin, day));
                break;
            case (int) FirebaseCrypt_Termin_entity.REPETITION_WEEK:
                if (zdt_currentTermin.getDayOfWeek() == zdt_currentDay.getDayOfWeek()) {
                    day.add_termin(prepareTerminToDisplay(termin, day));
                }
                break;
            case (int) FirebaseCrypt_Termin_entity.REPETITION_MONTH:
                if (zdt_currentTermin.getDayOfMonth() == zdt_currentDay.getDayOfMonth()) {
                    day.add_termin(prepareTerminToDisplay(termin, day));
                }
                break;
            case (int) FirebaseCrypt_Termin_entity.REPETITION_YEAR:
                if (zdt_currentTermin.getDayOfMonth() == zdt_currentDay.getDayOfMonth() && zdt_currentTermin.getMonth() == zdt_currentDay.getMonth()) {
                    day.add_termin(prepareTerminToDisplay(termin, day));
                }
                break;
        }
    }

    private Mav_TerminDecrypted prepareTerminToDisplay(FirebaseCrypt_Termin_entity termin, Mav_DayMitTerminen currentDay){
        long currentDayInMillis = currentDay.getTime_utc();

        return new Mav_TerminDecrypted(
                getUhrzeitString(termin),
                getNameString(termin, currentDayInMillis),
                termin.gibDescription(),
                termin.gibType(),
                termin.gibErledigungsTime()
        );
    }

    private String getNameString(FirebaseCrypt_Termin_entity termin, long currentDayInMillis) {
        String name;
        if (termin.gibType() == FirebaseCrypt_Termin_entity.TYPE_GEBURTSTAG){
            LocalDate currentGeburtstag = Instant.ofEpochMilli(currentDayInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate geburtstag        = LocalDate.of((int) termin.gibGeburtsjahr(), currentGeburtstag.getMonth(), currentGeburtstag.getDayOfMonth());
            Period period = Period.between(geburtstag, currentGeburtstag);
            name = termin.gibName() + " " + period.getYears() + "ter";
        } else
            name = termin.gibName();
        return name;
    }

    private String getUhrzeitString(FirebaseCrypt_Termin_entity termin) {
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
