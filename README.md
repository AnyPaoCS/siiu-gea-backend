# SIIU-GEA

Gestion de la Estructura Academica

## Correr el Proyecto

Para compilar el proyecto multimodulo en la ruta primcipal ejecutar mvn clean install

Para ejectar el projecto puedes correr la Clase

## Correr el Repositorio de archivos Jackrabbit modo StandAlone

Puedes ver un ejemplo de uso de Jackrabbit en el test

fileStorage/src/test/java/com/umss/siiu/filestorage/FileStorageApplicationTests.java

Para correr Jackrabbit primero debes copiar el ejecutable JAR en tu maquina local, por ejemplo copialo a tu carpeta
Downloads. Esta es la ruta del ejecutable a copiar en tu maquina local

* fileStorage/src/main/resources/jackrabbitJARStandAlone/jackrabbit-standalone-2.18.0.jar

Documentacion: http://jackrabbit.apache.org/jcr/standalone-server.html

Para correr Jackrabbit en la consola ejecuta el comando:

`java -jar jackrabbit-standalone-2.18.0.jar --port 8081`

Puedes ver los archivos en el navegador usando el usuario y password por defecto admin admin
http://localhost:8081/repository/default/
http://localhost:8081/remote.jsp

Cuando correr jackrabbit, podras ver que fisicamente en la misma ruta donde esta el archivo JAR que copiaste se crea una
carpeta "jackrabbit". Esta carpeta es la que contiene todos los archivos

## Generar backup de Jackrabbit

corre el comando cuando jackrabbit no este corriendo java -jar jackrabbit-standalone-2.18.0.jar --backup \
--repo jackrabbit \
--conf jackrabbit/repository.xml \
--backup-repo jackrabbit-backupN \
--backup-conf jackrabbit-backupN/repository.xml

## Restaurar un backup de Jackrabbit

Cuando jackrabbit no este corriendo

* Copia el backup renombrando el folder a jackrabbit
