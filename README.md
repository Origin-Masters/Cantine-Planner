![Version](https://img.shields.io/badge/version-Abgabeversion-purple)
![Build Status](https://github.com/Origin-Masters/Cantine-Planner/actions/workflows/maven.yml/badge.svg?branch=main)

# Cantine Planner - Projekt in Programmierung 3

#### Xudong Zhang, Christian Stehle, Valentin Straßer, Michał Roziel

## 1. Projektübersicht
Cantine Planner ist ein Programm, welches zu dem Verwalten von einer Mensa / Kantine dient. Es ermöglicht dem Nutzer, einen Überblick über die aktuell auftretenden Gerichte zu behalten, sowie eine Personalisierung durchzuführen.
Eine Datenbank mit bereits existierenden Gerichten wird mitgeliefert.

## 2. Benutzung
Durch unseren ***Copy-On-First-Run*** (Programmatischen) Ansatz wird ein Datenbank-Verzeichnis beim starten der JAR mittels


```
java -jar cantine-planner-Abgabeversion-shaded.jar
```
erstellt, dort findet man anschließend die zu nutzende Datenbank. Wir haben uns hierfür entschieden, da wir somit die Datenbank gefüllt mitliefern können.

---
Die Anwendung kann mittels den Pfeiltasten, dem Tabulator zum wechseln zwischen angezeigten Fenstern sowie der Enter-Taste zur Bestätigung bedient werden.
Die Eingabe von Text für die Anwendung erfolgt über die Buchstaben auf der Tastatur.
Das Benutzen einer Maus / eines Trackpads ist nicht vorgesehen.
---
Um Alle Funktionen des Programms Nutzen zu können, kann der **_Admin-User_** verwendet werden.
Hierbei lauten die Login-Daten wie folgt :
```
Username : Admin
Password : Admin
```