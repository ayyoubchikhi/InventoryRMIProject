
# Système de Gestion d'Inventaire avec Accès Distants

## Contexte

Une petite entreprise souhaite mettre en place un système de gestion d'inventaire permettant de gérer les produits en stock, avec un accès distant via RMI. Ce projet consiste à développer une application Java avec deux parties principales : un serveur qui gère les données et un client permettant aux employés de se connecter à distance pour interagir avec l'inventaire.

## Objectifs

- Développer une application Java divisée en deux parties :
  - **Serveur** : Expose des services pour les clients distants via RMI, et gère la base de données.
  - **Client** : Permet aux utilisateurs d'effectuer des opérations sur l'inventaire (ajouter, supprimer, rechercher des produits).
  
- Implémenter l'accès distant avec **RMI**.
- Intégrer une base de données **MySQL** pour stocker les informations sur les produits.

## Fonctionnalités Principales

1. **Gestion des Produits** :
   - Ajouter un produit.
   - Mettre à jour un produit.
   - Supprimer un produit.
   - Rechercher des produits par nom, catégorie ou quantité.
   
2. **Accès Distant** :
   - Les employés peuvent se connecter au serveur via RMI et interagir avec l'inventaire à distance.

## Architecture Technique

### Base de données

- **MySQL** : Stockage des informations des produits.
- Interaction via **JDBC**.
- Scripts de base de données dans `db/schema.sql` et `db/data.sql`.

### Serveur

- **RMI** : Fournit des services d'inventaire aux clients distants.
- **DAO** : Gestion des accès à la base de données via `ProductDAO.java` et `UserDAO.java`.

### Client

- Interface utilisateur en **JavaFX** pour interagir avec le serveur via RMI.
- Composants de l'interface graphique dans `client/ClientGUI.java`.

### Sécurité

- **Authentification** : Le client se connecte via un formulaire de login (dans `LoginGUI.java`).

## Installation et Exécution

### Prérequis

- **Java** 8 ou supérieur.
- **MySQL** 5.7 ou supérieur.
- **JavaFX** (librairie incluse dans `javafx-sdk/`).

### Étapes d'Installation

1. **Configurer la Base de Données** :
   - Créez la base de données et les tables en utilisant le script `db/schema.sql`.
   - Insérez des données de test avec `db/data.sql`.

2. **Configurer le Serveur** :
   - Le serveur écoute les requêtes RMI.
   - Lancer le serveur avec `ServerMain.java`.

3. **Exécuter le Client** :
   - Le client se connecte au serveur via `ClientGUI.java`.

4. **Configurer les Politiques de Sécurité** :
   - Assurez-vous que le fichier de politique `policy/server.policy` est configuré correctement pour autoriser les connexions RMI.

### Compilation et Exécution avec Fichiers `.bat`

#### Compilation

- Pour compiler le client et le serveur, vous pouvez utiliser les fichiers `.bat` suivants :

    - **Compiler le serveur** : Double-cliquez sur le fichier `compileServer.bat`.
    - **Compiler le client** : Double-cliquez sur le fichier `compileClient.bat`.

#### Exécution

- Pour exécuter le serveur et le client, vous pouvez utiliser les fichiers `.bat` suivants :

    - **Lancer le serveur** : Double-cliquez sur le fichier `runServer.bat`.
    - **Lancer le client** : Double-cliquez sur le fichier `runClient.bat`.

### Commandes d'Exécution

```bash
# Pour lancer le serveur via fichier .bat
runServer.bat

# Pour lancer le client via fichier .bat
runClient.bat
```

## Structure du Projet

```plaintext
InventoryRMIProject/
├── server/                              # Code du serveur et logique de gestion des produits
│   ├── InventoryInterface.java           # Interface RMI pour l'accès aux services d'inventaire
│   ├── InventoryImplementation.java      # Implémentation de l'interface RMI
│   ├── DAO/                              # Classe de gestion des accès à la base de données
│   │   ├── ProductDAO.java               # DAO pour les produits
│   │   └── UserDAO.java                  # DAO pour la gestion des utilisateurs
│   └── ServerMain.java                   # Point d'entrée du serveur
├── client/                              # Code du client
│   ├── ClientGUI.java                    # Interface graphique du client pour gérer l'inventaire
│   ├── LoginGUI.java                     # Interface de login pour l'authentification des employés
├── resources/                           # Ressources de l'application
│   ├── styles.css                       # Styles CSS pour l'interface graphique
│   ├── logo.png                         # Logo de l'application
│   └── background.jpg                   # Image d'arrière-plan
├── policy/                              # Politique de sécurité pour RMI
│   └── server.policy                    # Fichier de configuration RMI
├── db/                                  # Scripts SQL pour la base de données
│   ├── schema.sql                       # Script SQL pour créer les tables
│   └── data.sql                         # Script pour insérer des données de test
├── javafx-sdk/                          # Dossiers JavaFX nécessaires
│   └── (les dossiers JavaFX)            # Dossiers nécessaires pour JavaFX
├── lib/                                 # Librairies externes
│   └── mysql-connector-java-9.1.0.jar    # Connexion MySQL JDBC
├── compileClient.bat                    # Fichier pour compiler le client
├── compileServer.bat                    # Fichier pour compiler le serveur
├── runServer.bat                        # Fichier pour exécuter le serveur
├── runClient.bat                        # Fichier pour exécuter le client
└── README.md                            # Ce fichier
```

## Justification des Choix Technologiques

- **RMI** : Utilisé pour la communication distante entre le client et le serveur, permettant une gestion centralisée de l'inventaire.
- **MySQL** : Choisi pour sa robustesse dans le stockage des données et sa compatibilité avec JDBC.
- **JavaFX** : Pour créer une interface graphique moderne et interactive pour le client.

## Rapport de Tests

Le rapport de tests inclut des tests pour :
- Ajout de produit.
- Connexion client-serveur via RMI.
- Authentification des employés.
- Requête de mise à jour et suppression de produits.

## Conclusion

Ce projet permet de gérer un inventaire à distance de manière sécurisée, avec une interface graphique moderne et un backend robuste utilisant RMI pour la communication entre le serveur et les clients. Il peut être facilement étendu avec des fonctionnalités supplémentaires telles que des notifications, des rapports, ou un système de gestion des utilisateurs plus avancé.
