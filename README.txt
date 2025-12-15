=================================
FITNESS TRACKER - Guide d'Installation
=================================

PRÉREQUIS:
---------
1. Java 11 ou supérieur
2. MySQL (via XAMPP ou installation locale)
3. Maven (optionnel, pour la compilation avancée)

INSTALLATION DE LA BASE DE DONNÉES:
-----------------------------------
1. Ouvrir XAMPP et démarrer MySQL
2. Ouvrir phpMyAdmin (http://localhost/phpmyadmin)
3. Exécuter le script schema.sql:
   - Cliquer sur "Importer"
   - Sélectionner le fichier schema.sql
   - Cliquer sur "Exécuter"

Configuration de la connexion:
- L'application se connecte à: localhost:3306
- Utilisateur: root
- Mot de passe: (vide par défaut)
- Base de données: fitness_tracker

Si votre MySQL a un mot de passe différent, modifier:
src/database/DatabaseConnection.java ligne 7-8

COMPILATION:
-----------
cd project
javac -d bin -cp "lib/*" src/App.java src/controllers/*.java src/database/*.java src/models/*.java src/utils/*.java src/views/*.java

LANCEMENT:
---------
Depuis le répertoire project:
java -cp bin:lib/* App

Ou si vous utilisez un fichier .jar:
java -jar FitnessTracker.jar

LIBRAIRIES REQUISES (dans lib/):
-------------------------------
- mysql-connector-java-8.0.33.jar (ou plus récent)
- javafx-controls-20.jar
- javafx-fxml-20.jar
- javafx-graphics-20.jar
- javafx-base-20.jar

Télécharger JavaFX depuis: https://gluonhq.com/products/javafx/

UTILISATION:
-----------
1. Démarrer l'application
2. Créer un compte ou se connecter
3. Ajouter des exercices via l'onglet "Ajouter exercice"
4. Consulter vos statistiques dans le "Tableau de bord"
5. Voir l'historique des exercices dans "Historique"

DÉPANNAGE:
---------
- Si la connexion MySQL échoue: Vérifier que MySQL est démarré
- Si des classes ne sont pas trouvées: Vérifier que toutes les librairies sont dans lib/
- Si JavaFX ne s'affiche pas: Ajouter --module-path à la compilation avec le chemin JavaFX

SUPPORT:
--------
Pour toute question ou problème, vérifier:
1. Les fichiers sont dans les bons répertoires
2. MySQL est en cours d'exécution
3. La base de données est créée avec schema.sql
4. Tous les fichiers Java sont compilés dans bin/