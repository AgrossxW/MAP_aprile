-- ***  Pulizia (Opzionale, ma raccomandato per la ri-eseguibilità) ***
-- Elimina il database se esiste già
DROP DATABASE IF EXISTS MapDB;
-- Elimina l'utente se esiste già
DROP USER IF EXISTS 'MapUser'@'localhost';

-- ***  Creazione del Database (MapDB) ***
CREATE DATABASE MapDB;

-- ***  Creazione dell'Utente e Assegnazione dei Permessi ***
-- La classe DbAccess si connette come 'MapUser' con password 'map'
CREATE USER 'MapUser'@'localhost' IDENTIFIED BY 'map';

-- Assegna i diritti di SELECT, INSERT, DELETE (Lettura/Scrittura/Modifica) e CREATE
-- su tutte le tabelle del database MapDB all'utente MapUser.
GRANT SELECT, INSERT, DELETE, CREATE ON MapDB.* TO 'MapUser'@'localhost';

-- l comando FLUSH PRIVILEGES in MySQL forza il server a ricaricare le tabelle dei privilegi, 
-- applicando immediatamente eventuali modifiche ai permessi degli utenti e svuotando la cache dei privilegi, 
-- permettendo di aggiornare la sicurezza senza riavviare il server
FLUSH PRIVILEGES;

-- ***  Creazione della Tabella (playtennis) ***
-- Seleziona il database per non doverlo specificare ogni volta (opzionale)
USE MapDB; 

-- Crea la tabella playtennis con la sua struttura
CREATE TABLE playtennis(
    outlook VARCHAR(10),
    temperature FLOAT(5,2),
    umidity VARCHAR(10),
    wind VARCHAR(10),
    play VARCHAR(10)
);

-- ***  Popolamento della Tabella ***
INSERT INTO playtennis VALUES('sunny',30.3,'high','weak','no');
INSERT INTO playtennis VALUES('sunny',30.3,'high','strong','no');
INSERT INTO playtennis VALUES('overcast',30.0,'high','weak','yes');
INSERT INTO playtennis VALUES('rain',13.0,'high','weak','yes');
INSERT INTO playtennis VALUES('rain',0.0,'normal','weak','yes');
INSERT INTO playtennis VALUES('rain',0.0,'normal','strong','no');
INSERT INTO playtennis VALUES('overcast',0.1,'normal','strong','yes');
INSERT INTO playtennis VALUES('sunny',13.0,'high','weak','no');
INSERT INTO playtennis VALUES('sunny',0.1,'normal','weak','yes');
INSERT INTO playtennis VALUES('rain',12.0,'normal','weak','yes');
INSERT INTO playtennis VALUES('sunny',12.5,'normal','strong','yes');
INSERT INTO playtennis VALUES('overcast',12.5,'high','strong','yes');
INSERT INTO playtennis VALUES('overcast',29.21,'normal','weak','yes');
INSERT INTO playtennis VALUES('rain',12.5,'high','strong','no');
