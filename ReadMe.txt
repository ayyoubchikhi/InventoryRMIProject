- Voici la commande complète de compilation (si nécessaire) :

javac -cp ".;lib/mysql-connector-java-9.1.0.jar" -d bin server/InventoryInterface.java server/InventoryImplementation.java server/ServerMain.java client/ClientMain.java


- dans une fenêtre on lance le serveur :

java -cp ".;bin;lib/mysql-connector-java-9.1.0.jar" -Djava.security.policy=policy/server.policy server.ServerMain

- dans une deuxième fenêtre on lance le client :

java -cp ".;bin;lib/mysql-connector-java-9.1.0.jar" -Djava.security.policy=policy/server.policy client.ClientMain


