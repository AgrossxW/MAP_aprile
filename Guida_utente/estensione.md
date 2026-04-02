
## Guida Utente – Estensione progetto ClientTelegram

# Indice

1. [Introduzione](#1-introduzione)  
2. [Accesso al bot Telegram](#2-accesso-al-bot-telegram)  
3. [Nota progettuale](#3-nota-progettuale)  
4. [Requisiti di sistema](#4-requisiti-di-sistema)  
5. [Avvio del sistema](#5-avvio-del-sistema)  
6. [Comandi disponibili](#6-comandi-disponibili)  
   - [6.1 Menu principale](#61-menu-principale)  
   - [6.2 Modalità database](#62-modalità-database)  
   - [6.3 Modalità file](#63-modalità-file)  
7. [Utilizzo del bot](#7-utilizzo-del-bot)  
   - [7.1 Avvio e menu principale](#71-avvio-e-menu-principale)  
   - [7.2 Caricamento da database](#72-caricamento-da-database)  
   - [7.3 Caricamento da file](#73-caricamento-da-file)  
8. [Gestione della sessione](#8-gestione-della-sessione)  
9. [Interpretazione dei risultati](#9-interpretazione-dei-risultati)  
10. [Errori comuni](#10-errori-comuni)  
11. [Note finali](#11-note-finali)  
12. [Conclusione](#12-conclusione)  

---

# 1. Introduzione
Questa guida descrive l’utilizzo del **ClientTelegram**, estensione del progetto base di clustering QT.

A differenza del client testuale, l’interazione con il sistema avviene tramite un **bot Telegram**, che consente di:
- eseguire il clustering QT
- visualizzare lo stato della sessione
- salvare i risultati su file
- caricare cluster già salvati

Il bot comunica con il server QT utilizzando lo **stesso protocollo client-server** del progetto base, senza modificare il server.

Per iniziare, è sufficiente aprire la chat del bot e premere `/start`.

![Schermata iniziale del bot](../img/prechat.jpg)

---

# 2. Accesso al bot Telegram
Il bot può essere raggiunto in due modi:

- tramite link diretto: [QTClusteringMAP_bot](https://t.me/QTClusteringMAP_bot)
- tramite QR code

![QR code del bot Telegram](../img/QRcode.jpg)

---

# 3. Nota progettuale
Il ClientTelegram è stato progettato come **client separato**, mantenendo invariato il server QT.

Caratteristiche principali:
- Il bot è eseguito come **un unico processo client**
- Può gestire **più chat contemporaneamente**
- Ogni chat ha una propria **UserSession indipendente**
- La connessione al server viene aperta **solo quando necessario (on-demand)**

Questo approccio garantisce:
- separazione degli stati tra utenti
- efficienza nell’uso delle risorse
- riutilizzo completo del server esistente

Il bot rappresenta quindi un **nuovo client** del sistema, mentre il server continua a svolgere la logica di clustering senza dipendere dall’interfaccia utente.

---

# 4. Requisiti di sistema
Per utilizzare il ClientTelegram sono necessari:

- Java JDK 17 o superiore
- Server QT attivo (porta 8080 disponibile)
- MySQL attivo (solo per modalità database)
- Connessione Internet attiva
- Bot Telegram configurato

---

# 5. Avvio del sistema
Seguire i seguenti passi:

1. Avviare MySQL, se si desidera usare il caricamento da database
2. Avviare il server QT
3. Avviare il ClientTelegram
4. Aprire Telegram e cercare il bot
5. Premere `Avvia` oppure inviare il comando `/start`

⚠️ Il bot può essere avviato anche senza server attivo: la connessione verrà stabilita solo quando necessario.

---

# 6. Comandi disponibili

## 6.1 Menu principale
- `/start` → avvia il bot
- `/loadfromdb` → modalità database
- `/loadfromfile` → modalità file
- `/back` → ritorna al menu principale

## 6.2 Modalità database
- `/table` → inserimento nome tabella
- `/radius` → inserimento raggio
- `/compute` → esegue clustering
- `/status` → mostra parametri correnti
- `/saveonfile` → salva clustering su file

## 6.3 Modalità file
- selezione numerica del file mostrato dal bot
- `/back` → ritorna al menu principale

---

# 7. Utilizzo del bot

## 7.1 Avvio e menu principale
Dopo il comando `/start`, il bot mostra il **menu principale**, da cui è possibile scegliere se caricare i dati da database oppure da file salvati.

![Menu principale del bot](../img/start.jpg)

---

## 7.2 Caricamento da database
Per eseguire il clustering a partire da una tabella del database, seguire questa sequenza:

1. `/loadfromdb`
2. `/table`
3. inserire il nome della tabella
4. `/radius`
5. inserire un valore numerico maggiore di 0
6. `/compute`

Comandi opzionali:
- `/status` → visualizza lo stato corrente della sessione
- `/saveonfile` → salva il clustering su file
- `/back` → torna al menu principale

📌 La connessione al server viene aperta al momento di `/compute`.

Esempio di schermata del menu database:

![Menu load from db](../img/loadfromdb.jpg)

---

## 7.3 Caricamento da file
Per caricare un clustering già salvato:

1. `/loadfromfile`
2. il bot mostra la lista dei file disponibili sul server
3. inserire il **numero** corrispondente al file da caricare
4. il bot restituisce il clustering richiesto
5. usare `/back` per tornare al menu principale

📌 In questa modalità la connessione è temporanea, perché ogni caricamento è un’operazione indipendente.

Esempio di schermata del menu file:

![Menu load from file](../img/loadfromfile.jpg)

---

# 8. Gestione della sessione
Ogni chat Telegram ha una propria **sessione indipendente**.

La sessione contiene:
- nome tabella
- valore del raggio
- stato dell’input
- eventuale connessione al server
- lista dei file disponibili nel flusso di caricamento da file

Comportamenti importanti:
- `/back` resetta la sessione corrente e riporta al menu principale
- i parametri inseriti non vengono mantenuti dopo l’uscita dal menu database
- le sessioni di utenti diversi non interferiscono tra loro

Questo rende possibile gestire **più utenti contemporaneamente** pur avendo un solo processo del bot attivo.

---

# 9. Interpretazione dei risultati
Il clustering restituito contiene:

- **Number of Clusters** → numero di cluster trovati
- **Centroid** → centro del cluster
- **Examples** → elementi appartenenti al cluster
- **dist** → distanza di ciascun elemento dal centroide
- **AvgDistance** → distanza media del cluster

Queste informazioni permettono di valutare la struttura dei cluster generati e la loro compattezza.

---

# 10. Errori comuni
Possibili errori e cause:

- **Server non attivo** → errore di connessione
- **MySQL non attivo** → errore database durante `/compute`
- **Tabella inesistente o non valida** → errore SQL o rifiuto dell’input
- **Radius non valido** → il bot richiede un numero maggiore di 0
- **Selezione file non valida** → numero fuori intervallo o input non numerico
- **Comando non valido nel menu corrente** → il bot segnala che il comando non è ammesso in quel contesto
- **Nessuna connessione attiva** → uso scorretto di `/saveonfile` prima di `/compute`

---

# 11. Note finali
- Il salvataggio su file avviene sul server, nella directory prevista dal progetto base
- I file caricabili devono rispettare il formato previsto dal sistema
- La connessione al server è gestita automaticamente dal bot
- Il bot è progettato per un utilizzo semplice e guidato tramite menu
- La modalità database e la modalità file hanno comportamenti diversi nella gestione della connessione:
  - in `/loadfromdb` la connessione viene aperta a `/compute` e può restare attiva per permettere `/saveonfile`
  - in `/loadfromfile` ogni richiesta è autonoma e usa una connessione temporanea

---

# 12. Conclusione
Il ClientTelegram rappresenta un’interfaccia moderna e intuitiva per il sistema QT, permettendo di utilizzare le funzionalità principali del progetto tramite un semplice bot Telegram, senza necessità di usare il client testuale.

L’estensione mantiene l’architettura client-server del progetto base e rende il sistema più accessibile, pur lasciando invariata la logica del server.
