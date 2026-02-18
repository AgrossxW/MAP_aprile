#!/bin/bash
echo "==========================================="
echo "Setup librerie progetto Java - scarica jar"
echo "==========================================="

LIB_DIR="libs"
JAR_URL="https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.17/mysql-connector-java-8.0.17.jar"
JAR_NAME="mysql-connector-java-8.0.17.jar"
SQL_FILE="setup_mapdb.sql"

# Crea la cartella libs se non esiste
if [ ! -d "$LIB_DIR" ]; then
    echo "Creazione cartella $LIB_DIR..."
    mkdir "$LIB_DIR"
fi

echo "Scaricamento $JAR_NAME nella cartella $LIB_DIR..."
if command -v curl >/dev/null 2>&1; then
    curl -L -o "$LIB_DIR/$JAR_NAME" "$JAR_URL"
elif command -v wget >/dev/null 2>&1; then
    wget -O "$LIB_DIR/$JAR_NAME" "$JAR_URL"
else
    echo "Errore: né curl né wget sono installati."
    exit 1
fi

# Verifica se il file è stato scaricato
if [ -f "$LIB_DIR/$JAR_NAME" ]; then
    echo "Download completato con successo."
    echo "La libreria $JAR_NAME è stata salvata in $LIB_DIR."
else
    echo "ERRORE: download fallito. Controlla la connessione internet."
fi

echo "==========================================="
echo "Setup database MySQL (eseguo $SQL_FILE)..."
echo "Ti verra' chiesta la password di root MySQL, di solito vuota su Mac."
echo "==========================================="

if [ -f "$SQL_FILE" ]; then
    if command -v mysql >/dev/null 2>&1; then
        mysql -u root -p < "$SQL_FILE"
        echo "Setup database completato."
    else
        echo "Errore: il comando 'mysql' non è disponibile. Aggiungi MySQL al PATH o modifica lo script."
        exit 1
    fi
else
    echo "ERRORE: il file $SQL_FILE non esiste nella cartella corrente."
    exit 1
fi
