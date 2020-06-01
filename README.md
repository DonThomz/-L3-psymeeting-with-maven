# L3_PsyMeetingMaven
Projet Base de Données L3 2019-2020 

### Equipe

- **Thomas Guillaume**
- **Gabriel Dugny**
- **Yorane Doare**

### Frameworks

- JDK 11
- JavaFX 11
- Maven 3
- BDD : PostgreSQL, free hosting : ElephantSQL https://www.elephantsql.com/

### Executable

Prérequis : avoir Java >= 11 comme Java par défault sur le système

Dans le dossier shade lancer la commande :

```
java -jar L3-PsyMeeting.jar
```

Identifiant : 
user : admin
password : adminpwd

### Build le projet

1. Ouvrir le projet depuis votre IDE ( nous avons utilisé Intellij ) 

2. Spéficier le JDK à utiliser ( >= java 11 )

3. (si besoin faire charger le ***pom.xml***)

4. lancer les tasks : 	

   ```
   javafx:compile
   javafx:run
   ```
   
Sur certains systèmes, il peut être nécessaire de préciser 
au plugin javafx-maven-plugin l'emplacement de Java 11.

Il suffit de rajouter dans balise <executable> dans le pom.xml :

Autour de la ligne 60, rajouter cette balise dans la balise <configuration> du plugin.
```
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.1</version>
    <configuration>
        <mainClass>com.bdd.psymeeting.App</mainClass>
        <executable>C:/Program Files/Java/jdk-11.0.7/bin/java.exe</executable>
```
