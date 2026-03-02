# Manuale Utente
## Sistema di Clustering QT su Database MySQL (Architettura Client–Server)
------------------------------------------------------------------------

# Indice

1. [Introduzione](#1-introduzione)  
2. [Requisiti di Sistema](#2-requisiti-di-sistema)  
3. [Struttura del Progetto](#3-struttura-del-progetto)  
4. [Modalità di Avvio](#4-modalità-di-avvio)  
   - [4.1 Avvio su Windows](#41-avvio-su-windows)  
   - [4.2 Avvio su macOS / Linux](#42-avvio-su-macos--linux)  
5. [Utilizzo del Client](#5-utilizzo-del-client)  
   - [5.1 Caricamento da File](#51-Caricamento-da-File-load-to-file)  
   - [5.2 Caricamento da Database](#52-Caricamento-da-Database-Load-to-DB) 
6. [Arresto del Server](#6-arresto-del-server)
7. [Interpretazione dei Risultati](#7-interpretazione-dei-risultati) 
8. [Risoluzione dei Problemi](#9-risoluzione-dei-problemi)  
9. [Architettura del Sistema](#10-architettura-del-sistema)

------------------------------------------------------------------------

# 1. Introduzione

Il sistema implementa un sistema **Client–Server** per l’esecuzione dell’algoritmo di clustering QT (Quality Threshold).

Il **Server**: 
- Gestisce le connessioni multiple dei client tramite multithreading
- Si connette a un database MySQL
- Esegue il clustering
- Salva e carica cluster da file

Il **Client**: 
  - Permette all’utente di:
    - Caricare dati dal database
    - Eseguire clustering con raggio variabile
    - Salvare cluster
    - Ricaricare cluster da file

------------------------------------------------------------------------

# 2. Requisiti di Sistema

Per eseguire il sistema è necessario:

-   Java JDK 17 o superiore
-   MySQL Server installato e attivo (per modalità database)
-   Driver MySQL Connector presente nella cartella `Server / libs /`

Sistemi operativi supportati:

-   Windows
-   macOS
-   Linux

------------------------------------------------------------------------

# 3. Struttura del Progetto

Il progetto è organizzato nei seguenti package:

-   `server` → Gestione connessioni client
-   `mining` → Algoritmo di clustering
-   `data` → Rappresentazione del dataset
-   `database` → Accesso al database

------------------------------------------------------------------------

# 4. Modalità di Avvio

⚠ Il server deve essere avviato prima del client.

------------------------------------------------------------------------

## 4.1 Avvio su Windows

1.  Aprire la cartella `start` da interfaccia
2.  Eseguire:
    start-server.bat

3.  Successivamente eseguire:
    start-client.bat

In alternativa da Prompt dei comandi:
    start-server.bat
    start-client.bat

------------------------------------------------------------------------

## 4.2 Avvio su macOS / Linux

Aprire il terminale nella cartella `start` ed eseguire:
    chmod +x start-server.sh
    chmod +x start-client.sh

Avvio server:
    ./start-server.sh

Avvio client:
    ./start-client.sh

![Esecuzione Server](../img/server.png)

------------------------------------------------------------------------

# 5. Utilizzo del Client

Una volta avviato il client, l'utente può selezionare tra 2 opzioni :
![Selezione Client](../img/client1.png)

## 5.1 Caricamento da File (Load to File)

Consente di caricare cluster già generati e salvati precedentemente.  
Procedura:

1. Selezionare l'opzione `1` (Load to File).  
2. Inserire:
   - Nome della tabella  
   - Raggio usato durante la generazione del cluster  

![Load to File](img/file.png)

Il caricamento da file funziona solo se esiste già un clustering salvato con quel raggio per la tabella indicata.

## 5.2 Caricamento da Database (Load to DB)

Consente di ottenere i dati direttamente da una tabella MySQL.  
Procedura:

1. Selezionare l'opzione `2` (Load to DB).  
2. Inserire:
   - Nome della tabella  
   - Raggio da utilizzare
  
![Load to DB](img/db1.png)

I cluster vengono calcolati e mostrati a video, ad esempio:

![cluster Load to DB](img/db2.png)

------------------------------------------------------------------------

# 6. Arresto del Server

Per chiudere correttamente il server:

-   Windows: premere CTRL + C
-   macOS/Linux: premere CTRL + C

Si consiglia di evitare la chiusura forzata della finestra.

------------------------------------------------------------------------

# 7. Interpretazione dei Risultati

- **Centroid=(...)** indica il punto centrale del cluster, un esempio reale del dataset che rappresenta il cluster.  
- **Examples** elenca tutti gli elementi appartenenti al cluster.  
- **dist=...** indica la distanza di ciascun elemento dal centroid; più è bassa, più l’elemento è simile al centro del cluster.  
- **AvgDistance** è la distanza media tra tutti gli elementi del cluster e il centroid

------------------------------------------------------------------------

# 8. Risoluzione dei Problemi

## Errore: DatabaseConnectionException

Verificare: 
- Che MySQL sia attivo
- Che le credenziali siano corrette
- Che il driver MySQL sia presente in `Server / libs/`

## Errore: ClusteringRadiusException

Segnala la generazione di un solo cluster da parte dell’algoritmo QT-Clustering !

------------------------------------------------------------------------

# 9. Architettura del Sistema

Il sistema segue un'architettura a livelli:

Server → Mining → Data → Database

Le dipendenze sono unidirezionali per garantire separazione delle responsabilità.
