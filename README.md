![Version](https://img.shields.io/badge/version-Abgabeversion-purple)
![Build Status](https://github.com/Origin-Masters/Cantine-Planner/actions/workflows/maven.yml/badge.svg?branch=main)

# Cantine Planner - Projekt in Programmierung 3

#### Xudong Zhang, Christian Stehle, Valentin Straßer, Michał Roziel

---

## 1. Projektübersicht
Cantine Planner ist ein Programm, welches zu dem Verwalten von einer Mensa / Kantine dient. Es ermöglicht dem Nutzer, einen Überblick über die aktuell auftretenden Gerichte zu behalten, sowie eine Personalisierung durchzuführen.
Eine Datenbank mit bereits existierenden Gerichten wird mitgeliefert.

---

## 2. Features
- Das Programm lässt sich mittels eines TUI Interfaces bedienen.
- Ein wöchentlicher Speiseplan kann angezeigt werden.
- User können erstellt werden, hierbei unterscheiden wir zwischen User und Admin.
	- Persönliche Allergene können ausgewählt werden.
	- Lieblingsgericht kann gesetzt werden.
- Gerichte können hinzugefügt und angezeigt werden.
	- Hierbei wird Preis, Kalorien, Allergene, Fleischinhalt sowie Wochentag gesetzt.
- Reviews bezüglich Gerichten können verfasst/gelöscht sowie angezeigt werden.
- Mitgelieferte, bereits gefüllte Datenbank - Man kann direkt anfagen.

---

## 3. Systemanforderungen
Java Version : LTS 21

Maven Version : 3.9.9

---
## 4. Installation
Das Maven Projekt kann miitels ` git@github.com:Origin-Masters/Cantine-Planner.git ` mittels eines Terminals in einen gewählten Ordner runtergeladen werden.
Nach dem öffnen des Projekts mit einer IDE Ihrer Wahl geben Sie bitte
`mvn clean install ` ein. Dies installiert alle benötigten Dependencies aus der Pom.xml



---
## 5. Schnellstart

Das Projekt wird als JAR Datei unter **_Abgabeversion_** mittels GitHub Actions  mitgeliefert.

Die JAR kann unter ` https://github.com/Origin-Masters/Cantine-Planner/releases/tag/submission ` heruntergeladen werden.

Bei Nutzung eines **_Windows_** Betriebssystems kann die JAR mittels eines Doppelklicks ausgeführt werden.

Bei Nutzung eines **_Mac OsX_** oder **_Linux_** Betriebssystems bitten wir Sie die JAR mittels eines Terminals und des Befehls `java -jar cantine-planner-Abgabeversion-shaded.jar ` auszuführen.

## 6. Anwendungsbeispiele
Um Alle Funktionen des Programms Nutzen zu können, kann der **_Admin-User_** verwendet werden.
Hierbei lauten die Login-Daten wie folgt :
```
Username : Admin
Password : Admin
```

![Meals By Calories](images/mealsByCalories.png)


---

Die Anwendung kann mittels den Pfeiltasten, dem Tabulator zum wechseln zwischen angezeigten Fenstern sowie der Enter-Taste zur Bestätigung bedient werden.
Die Eingabe von Text für die Anwendung erfolgt über die Buchstaben auf der Tastatur.
Das Benutzen einer Maus / eines Trackpads ist nicht vorgesehen.

---






## 4. CI / CD
Mittels GitHub workflows haben wir Continous Integration sowie Conitinous Deployment integriert.
Der Workflow `maven.yml` baut das Maven Projekt und führt Tests mit jedem *Push* aus.
Der Workflow `release.yml` baut das Maven Projekt und führt ebenfalls Tests mit jedem *Push* aus.
Nach dem die Tests ausgeführt wurden, wird das Tag `submission`sowie das dazugehöroge GitHub release erstellt.
## 5. Tests
Wir haben mittels JUNIT 5.11.4 Tests implementiert, diese können innerhalb src/test angeschaut und  mittels `mvn test`innerhalb des Terminals ausgeführt werden.
## 6. Projektstruktur
```
├── META-INF
├── database
├── src
│   ├── main
│   │   ├── java
│   │   │   └── de
│   │   │       └── htwsaar
│   │   │           └── cantineplanner
│   │   │               ├── businessLogic
│   │   │               ├── codegen
│   │   │               │   └── tables
│   │   │               │       └── records
│   │   │               ├── dataAccess
│   │   │               ├── dbUtils
│   │   │               ├── exceptions
│   │   │               ├── presentation
│   │   │               │   └── pages
│   │   │               └── security
│   │   └── resources
│   │       └── META-INF
│   └── test
│       └── java
│           └── de
│               └── htwsaar
│                   └── cantineplanner
│                       └── dataAccess
└── target
    ├── classes
    │   ├── META-INF
    │   └── de
    │       └── htwsaar
    │           └── cantineplanner
    │               ├── businessLogic
    │               ├── codegen
    │               │   └── tables
    │               │       └── records
    │               ├── dataAccess
    │               ├── dbUtils
    │               ├── exceptions
    │               ├── presentation
    │               │   └── pages
    │               └── security
    ├── generated-sources
    │   └── annotations
    ├── generated-test-sources
    │   └── test-annotations
    ├── maven-archiver
    ├── maven-status
    │   └── maven-compiler-plugin
    │       ├── compile
    │       │   └── default-compile
    │       └── testCompile
    │           └── default-testCompile
    ├── surefire-reports
    └── test-classes
        └── de
            └── htwsaar
                └── cantineplanner
                    └── dataAccess
```