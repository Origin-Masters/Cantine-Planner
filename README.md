# Cantine-Planner


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

## 3. Vorraussetzung
Um das Programm nutzen zu können, wird die Long-Term-Support Java Version 21 benötigt.

# 4. Installation und Setup
Das Projekt wird als JAR Datei unter 1.0-SNAPSHOT mittels GitHub Actions in
	`cantine-planner-1.0-SNAPSHOT-shaded.jar ` mitgeliefert.

Hierbei beinhaltet die shaded-Version alle Essentiellen Dependencies.
Die Jar Datei kann in einem gewählten Verzeichnis mittles :
`java -jar cantine-planner-1.0-SNAPSHOT-shaded.jar `ausgeführt werden.

Durch unseren ***Copy-On-First-Run*** Ansatz wird ein Datenbank-Verzeichnis erstellt, dort findet man anschließend die zu nutzende Datenbank.


## 5. Tests
Wir haben mittels JUNIT 5.11.4 Tests implementiert,