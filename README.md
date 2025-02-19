![Version](https://img.shields.io/badge/version-1.0--SNAPSHOT-blue)
![Build Status](https://github.com/Origin-Masters/Cantine-Planner/actions/workflows/maven.yml/badge.svg?branch=development)

[Cantine Planner GitHub](https://github.com/Origin-Masters/Cantine-Planner)
# Cantine Planner - Projekt in Programmierung 3

## Xudong Zhang, Christian Stehle, Valentin Straßer, Michał Roziel

## 1. Projektübersicht
Cantine Planner ist ein Programm, welches zu dem Verwalten von einer Mensa | Kantine dient. Es ermöglicht dem Nutzer, einen Überblick über die aktuell auftretenden Gerichte zu behalten, sowie eine Personalisierung durchzuführen.
Eine Datenbank mit bereits existierenden Gerichten wird mitgeliefert.

## 2. Features
- Das Programm lässt sich mittels eines TUI Interfaces bedienen.
- Ein wöchentlicher Speiseplan kann angezeigt werden.
- User können erstellt werden, hierbei unterscheiden wir zwischen User und Admin.
	- Persönliche Allergene können ausgewählt werden.
	- Lieblingsgericht kann gesetzt werden.
- Gerichte können hinzugefügt und angezeigt werden.
	- Hierbei wird Preis, Kalorien, Allergene, Fleischinhalt sowie Wochentag gesetzt.
- Reviews bezüglich Gerichten können verfasst/gelöscht sowie angezeigt werden.
## 3. Vorraussetzungen
Um das Programm nutzen zu können, wird die Long-Term-Support *Java Version 21* benötigt.
## 4. Installation und Setup
Das Projekt wird als JAR Datei unter 1.0-SNAPSHOT mittels GitHub Actions in
	`cantine-planner-1.0-SNAPSHOT-shaded.jar ` mitgeliefert.

Hierbei beinhaltet die shaded-Version alle Essentiellen Dependencies.
Die Jar Datei kann in einem gewählten Verzeichnis mittles :
`java -jar cantine-planner-1.0-SNAPSHOT-shaded.jar `ausgeführt werden.
## 5. Benutzung
Durch unseren ***Copy-On-First-Run*** (Programmatischen) Ansatz wird ein Datenbank-Verzeichnis beim starten der JAR durch `java -jar [...] ` erstellt, dort findet man anschließend die zu nutzende Datenbank. Wir haben uns hierfür entschieden, da wir die Datenbank gefüllt mitliefern möchten.
## 4. CI / CD
Mittels GitHub workflows haben wir Continous Integration sowie Conitinous Deployment integriert.
Der Workflow `maven.yml` baut das Maven Projekt und führt Tests mit jedem *Push* aus.
Der Workflow `release.yml` baut das Maven Projekt, führt Tests aus.
Nach dem die Tests ausgeführt wurden, wird das Tag `submission`sowie das dazugehöroge GitHub release erstellt.
## 5. Tests
Wir haben mittels JUNIT 5.11.4 Tests implementiert, diese können innerhalb src/test angeschaut und  mittels `maven test `innerhalb des Terminals ausgeführt werden.
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




