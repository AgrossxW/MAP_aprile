# Manuale Utente
## Sistema di Clustering QT su Database MySQL (Architettura Client–Server)
------------------------------------------------------------------------

# Indice

1. [Introduzione](#1-introduzione)  
2. [Requisiti di Sistema](#2-requisiti-di-sistema)
   - [2.1 Configurazione del Database](#21-Configurazione-del-Database)
4. [Struttura del Sistema](#3-struttura-del-sistema)  
5. [Modalità di Avvio](#4-modalità-di-avvio)  
   - [4.1 Avvio su Windows](#41-avvio-su-windows)  
   - [4.2 Avvio su macOS / Linux](#42-avvio-su-macos--linux)  
6. [Utilizzo del Client](#5-utilizzo-del-client)  
   - [5.1 Caricamento da File](#51-Caricamento-da-File-load-to-file)  
   - [5.2 Caricamento da Database](#52-Caricamento-da-Database-Load-to-DB) 
7. [Interpretazione dei Risultati](#6-interpretazione-dei-risultati) 
8. [Arresto del Server](#7-arresto-del-server)
9. [Risoluzione dei Problemi](#8-risoluzione-dei-problemi)  
10. [Architettura del Sistema](#9-architettura-del-sistema)

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
    - Salvare cluster su file
    - Ricaricare cluster da file

------------------------------------------------------------------------

# 2. Requisiti di Sistema

Per eseguire il sistema è necessario:

-   Java JDK 17 o superiore
-   MySQL Server installato e attivo (per modalità database)
-   Driver MySQL Connector presente nella cartella `Server/libs/`

Requisiti di Rete

- Il **Client** e il **Server** devono poter comunicare sulla rete.
- Se eseguiti sulla stessa macchina, è sufficiente utilizzare `localhost` (IP 127.0.0.1).
- La porta **8080** deve essere libera sul computer in cui viene avviato il Server, affinché possa mettersi in ascolto delle connessioni in ingresso.
     - ⚠ Se la porta 8080 è già occupata, il Server non potrà avviarsi correttamente.

Sistemi operativi supportati:

-   Windows
-   macOS
-   Linux



## 2.1 Configurazione del Database

Prima di utilizzare la modalità di apprendimento da database, è necessario configurare il database MySQL.

1. Avviare MySQL Server.
2. Aprire un client MySQL (MySQL Workbench o terminale).
3. Eseguire lo script `mapDB.sql` presente nel progetto.

Esempio da terminale:
`mysql -u root -p < mapDB.sql`

Lo script crea:
- Il database necessario
- Le tabelle richieste
- I dati di esempio utilizzati dal sistema

⚠ Senza l’esecuzione di `mapDB.sql`, la modalità database non funzionerà correttamente.

------------------------------------------------------------------------

# 3. Struttura del Sistema

Il sistema è organizzato nei seguenti package:

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
- per il server : `start-server.bat`
- per i client : `start-client.bat`

------------------------------------------------------------------------

## 4.2 Avvio su macOS / Linux

Aprire il terminale nella cartella `start` ed eseguire:
    `chmod +x start-server.sh`
    `chmod +x start-client.sh`

Avvio server:
    `./start-server.sh`

Avvio client:
    `./start-client.sh`

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

# 6. Interpretazione dei Risultati

Al termine dell’esecuzione del clustering, il sistema mostra le informazioni relative ai cluster generati.  
Ogni cluster è descritto attraverso i seguenti elementi:

- **Centroid = (...)**  
  Rappresenta il centro del cluster.  
  Nel sistema QT il centroide coincide con una **tupla reale del dataset**, scelta come punto di riferimento del cluster.

- **Examples**  
  Elenca tutti gli elementi (tuple) appartenenti al cluster.  
  Un elemento viene assegnato al cluster se la sua distanza dal centroide è minore o uguale al raggio specificato.

- **dist = ...**  
  Indica la distanza tra un elemento del cluster e il suo centroide.  
  Valori più bassi indicano una maggiore somiglianza rispetto al centro del cluster.

- **AvgDistance**  
  Rappresenta la distanza media tra tutti gli elementi del cluster e il centroide.  
  Fornisce una misura della compattezza del cluster:
  - valori bassi → cluster più compatto
  - valori più alti → cluster più disperso

------------------------------------------------------------------------
# 7. Arresto del Server

Per chiudere correttamente il server:

-   Windows: premere `CTRL + C`
-   macOS/Linux: premere `CTRL + C`

Si consiglia di evitare la chiusura forzata della finestra.

------------------------------------------------------------------------

# 8. Risoluzione dei Problemi

## Errore: DatabaseConnectionException

Verificare: 
- Che MySQL sia attivo
- Che il driver MySQL sia presente in `Server / libs/`

## Errore: ClusteringRadiusException

Segnala la generazione di un solo cluster da parte dell’algoritmo QT-Clustering !

------------------------------------------------------------------------

# 9. Architettura del Sistema

Il sistema segue un'architettura a livelli:

Server → Mining → Data → Database

Le dipendenze sono unidirezionali per garantire separazione delle responsabilità.
