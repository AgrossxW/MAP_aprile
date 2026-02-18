@echo off
cd /d "%~dp0"
setlocal

REM --------------------------------------------------------
REM Modificare qui se il percorso di mysql.exe è diverso sul tuo PC!
set MYSQL_BIN="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
REM --------------------------------------------------------

set LIBDIR=libs
set JARNAME=mysql-connector-java-8.0.17.jar
set JARURL=https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.17/mysql-connector-java-8.0.17.jar

if not exist "%LIBDIR%" (
    mkdir "%LIBDIR%"
)

echo Scaricamento del file %JARNAME% nella cartella %LIBDIR%...
powershell -Command "Invoke-WebRequest -Uri '%JARURL%' -OutFile '%LIBDIR%%JARNAME%'"

if exist "%LIBDIR%%JARNAME%" (
    echo Download completato con successo.
    echo La libreria %JARNAME% e' stata salvata in %LIBDIR%.
) else (
    echo ERRORE: download fallito. Controlla la connessione internet o i permessi.
)

REM --- Esegui script setup database ---
echo -----------------------------------------
echo ESECUZIONE SCRIPT: setup_mapdb.sql
echo (verra' chiesta la password di root MySQL)
echo -----------------------------------------
%MYSQL_BIN% -u root -p < setup_mapdb.sql

pause
endlocal