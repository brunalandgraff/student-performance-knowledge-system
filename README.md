# Student Performance Knowledge System

A knowledge-based application developed with Java and Prolog to analyse student performance, participation and academic risk indicators.

The system uses Prolog as its inference and data-management layer, while Java provides the command-line interface and controls the application flow.

## Features

- Identify students at academic risk
- Identify students with adequate forum participation
- Identify students with positive academic performance
- Calculate the class average
- List students performing above the class average
- Search students by ID
- Add new students
- Update student grades
- Update forum participation
- Remove students from the knowledge base
- Persist changes in the Prolog knowledge base

## Technologies

- Java
- SWI-Prolog
- JPL — Java–Prolog integration library

## Architecture

The application separates responsibilities between two programming paradigms:

- **Prolog:** knowledge representation, inference rules, data queries and persistence
- **Java:** command-line interface, input validation and application flow
- **JPL:** communication between Java and the Prolog inference engine

## Project Structure

```text
src/
├── java/
│   ├── Aplicacao.java
│   ├── IntegradorProlog.java
│   └── Menu.java
└── prolog/
    ├── base_conhecimento.pl
    └── sistema.pl
```

## Academic Context

This project was developed for the Programming Languages course of the Bachelor's Degree in Informatics Engineering at Universidade Aberta.

**Final grade: 4.0 / 4.0**

This repository contains a portfolio-oriented version of the original academic submission.

## What I Learned

- Declarative programming with Prolog
- Knowledge representation and inference rules
- Dynamic fact management and persistence
- Java–Prolog integration using JPL
- Separation between application logic and user interface
- Input validation and error handling
- Modular application design

## Requirements

- Java JDK 8 or later
- SWI-Prolog
- JPL — included with the SWI-Prolog installation

> The SWI-Prolog installation directory may vary depending on the operating system and installation settings.

## How to Run

### Test the Prolog Knowledge Base

Open a terminal in the `src/prolog` directory:

```bash
swipl
```

Load the system:

```prolog
?- consult('sistema.pl').
```

Example queries:

```prolog
?- listar_em_risco(L).
?- listar_participativos(L).
?- listar_bons(L).
?- media_turma(M).
?- listar_acima_media(L).
?- listar_consistentes(L).
```

Expected results using the initial knowledge base:

```text
listar_em_risco(L).        L = [2,5]
listar_participativos(L).  L = [1,3,4,6]
listar_bons(L).            L = [1,3,4,6]
media_turma(M).            M = 12.25
listar_acima_media(L).     L = [1,3,6]
listar_consistentes(L).    L = [1,3,6]
```

### Run the Java Interface on Windows

Open PowerShell in the `src/java` directory.

Add the SWI-Prolog native libraries to the current session:

```powershell
$env:PATH = "C:\Program Files\swipl\bin;" + $env:PATH
```

Compile the Java classes:

```powershell
javac -cp ".;C:\Program Files\swipl\lib\jpl.jar" *.java
```

Run the application:

```powershell
java "-Djava.library.path=C:\Program Files\swipl\bin" -cp ".;C:\Program Files\swipl\lib\jpl.jar" Aplicacao ../prolog/sistema.pl
```

The paths may need to be adjusted if SWI-Prolog is installed in a different directory.

## Author

**Bruna Landgraff**

Informatics Engineering Student  
[LinkedIn](https://www.linkedin.com/in/brunalandgraff/)
