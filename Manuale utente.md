# Manuale Utente - Sistema di Clustering QT (Client/Server)

manuale_markdown = """
# Manuale Utente
## Sistema di Clustering QT (Architettura Client/Server)

---

# Indice

1. Introduzione  
2. Requisiti di Sistema  
3. Struttura del Progetto  
4. Modalità di Avvio  
   - 4.1 Avvio su Windows  
   - 4.2 Avvio su macOS / Linux  
5. Utilizzo del Client  
6. Modalità di Apprendimento  
   - 6.1 Apprendimento da Database  
   - 6.2 Apprendimento da File  
7. Salvataggio dei Risultati  
8. Arresto del Server  
9. Risoluzione dei Problemi  
10. Architettura del Sistema  

---

# 1. Introduzione

Il sistema implementa un algoritmo di clustering QT secondo un’architettura client-server.

Il **Server**:
- Gestisce le connessioni dei client
- Esegue l’algoritmo di clustering
- Interagisce con il database o con file locali

Il **Client**:
- Permette all’utente di selezionare la modalità di apprendimento
- Consente l’inserimento del raggio (radius)
- Visualizza i risultati del clustering

---

# 2. Requisiti di Sistema

Per eseguire il sistema è necessario:

- Java JDK (versione compatibile con il progetto)
- MySQL Server installato e attivo (per modalità database)
- Driver MySQL Connector presente nella cartella `libs`

Sistemi operativi supportati:

- Windows
- macOS
- Linux

---

# 3. Struttura del Progetto

Il progetto è organizzato nei seguenti package:

- `server` → Gestione connessioni client
- `mining` → Algoritmo di clustering
- `data` → Rappresentazione del dataset
- `database` → Accesso al database

Cartelle principali:

- `Server/`
- `Client/`
- `libs/`
- `start/`

---

# 4. Modalità di Avvio

⚠ Il server deve essere avviato prima del client.

---

## 4.1 Avvio su Windows

1. Aprire la cartella `start`
2. Eseguire:

    start-server.bat

3. Successivamente eseguire:

    start-client.bat

In alternativa da Prompt dei comandi:

    start-server.bat
    start-client.bat

---

## 4.2 Avvio su macOS / Linux

Aprire il terminale nella cartella `start` ed eseguire:

    chmod +x start-server.sh
    chmod +x start-client.sh

Avvio server:

    ./start-server.sh

Avvio client:

    ./start-client.sh

---

# 5. Utilizzo del Client

Una volta avviato il client, l’utente può:

1. Selezionare la modalità di apprendimento
2. Inserire il valore del raggio (radius)
3. Visualizzare il numero di cluster generati
4. Salvare il risultato su file

Il raggio determina la dimensione massima dei cluster.

---

# 6. Modalità di Apprendimento

## 6.1 Apprendimento da Database

L’utente inserisce:

- Nome della tabella
- Valore del raggio

Il sistema:
- Legge i dati dal database
- Costruisce il dataset
- Esegue il clustering

---

## 6.2 Apprendimento da File

Permette di caricare un clustering precedentemente salvato.

Il sistema:
- Deserializza il file
- Ripristina il ClusterSet

---

# 7. Salvataggio dei Risultati

Il sistema consente di salvare:

- Numero di cluster
- Contenuto dei cluster
- Raggio utilizzato

I file vengono salvati in formato serializzato.

---

# 8. Arresto del Server

Per chiudere correttamente il server:

- Windows: premere CTRL + C
- macOS/Linux: premere CTRL + C

Si consiglia di evitare la chiusura forzata della finestra.

---

# 9. Risoluzione dei Problemi

## Errore: DatabaseConnectionException

Verificare:
- Che MySQL sia attivo
- Che le credenziali siano corrette
- Che il driver MySQL sia presente in `libs/`

## Errore: ClusteringRadiusException

Il raggio inserito è troppo piccolo.

---

# 10. Architettura del Sistema

Il sistema segue un’architettura a livelli:

Server → Mining → Data → Database

Le dipendenze sono unidirezionali per garantire separazione delle responsabilità.
"""

import pypandoc

output_path = "/mnt/data/Manuale_Utente_Sistema_QT_Clustering.md"

pypandoc.convert_text(
    manuale_markdown,
    'md',
    format='md',
    outputfile=output_path,
    extra_args=['--standalone']
)

output_path
