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

- Java Development Kit
- SWI-Prolog
- JPL configured in the local environment

Detailed installation and execution instructions will be added as the project is organised for public use.

## Author

**Bruna Landgraff**

Informatics Engineering Student  
[LinkedIn](https://www.linkedin.com/in/brunalandgraff/)
