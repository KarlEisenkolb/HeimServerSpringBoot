# Privater Spring Boot Server
### Aufgaben:
Folgende Aufgaben sollen vom Server ausgeführt werden:
- regelmäßige Api-Abfrage der Wettervorhersage von openweathermap.org, Zwischenspeicherung der Daten in MariaDB, Anzeige der Wettervorhersage auf einer lokalen Website
- Anzeige und Echtzeitaktualisierung von Terminen, Aufgaben, Geburtstagen, Urlaub etc., sowie Zwischenspeicherung der Daten in MariaDB für jeden Tag (Die Termine stammen aus einer selbstprogrammierten nativen Android App mit der alle Termine organisiert werden.)
- Verschicken von Notifications an alle Smartphones der beteiligten Personen bei Änderung der Datenlage bestimmter Apps: Terminänderungen, neue Aufgaben, Aufgabe erledigt etc.
- Verschlüsselung der Daten für Firebase Firestore (Daten liegen im Cloud-Speicher verschlüsselt vor.)
- Anzeige, Echtzeitaktualiserung und tabellarische sowie visuelle Aufbereitung von Sensordaten der Wohnumgebung mittels chart.js
- Beurteilung der Sensordaten und visuelle Warnung bei Überschreitung von Grenzwerten
- Server läuft auf einem Raspberry Pi

![grab-landing-page](https://github.com/KarlEisenkolb/HeimServerSpringBoot/blob/master/images/smartMonitor.jpg)
[Klick Website Detailansicht](https://github.com/KarlEisenkolb/HeimServerSpringBoot/blob/master/images/website.PNG)

### Sensoranbindung:
- Sensoren werden an in der Wohnumgebung verteilten Raspberry Pi's über den I2C-Bus angeschlossen. 
- Erhaltene Sensordaten umfassen: die Feinstaubbelastung (pm2.5, pm10), die absolute Feuchtigkeit, die relative Feuchtigkeit, die Temperatur in °C und einen Luftqualitätsindex (IAQ), der die Anwesenheit flüchtiger organischer Verbindungen (VOC's) beurteilt.

![grab-landing-page](https://github.com/KarlEisenkolb/HeimServerSpringBoot/blob/master/images/raspberry.jpg)
