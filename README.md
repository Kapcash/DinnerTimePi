# Le projet

DinnerTimePi est un projet basé sur un constat personnel. A l'heure du dîner, il faut dire à tout le monde
que c'est l'heure de se mettre à table !
Et bien souvent, ça se transforme en calvaire !

Cette application permet en un clic d'avertir tout le foyer que c'est l'heure de manger ! ;-)

# Le principe

Cette application toute simple fonctionne de la façon suivante :
 - A l'heure de manger, un personne presse le bouton
 - Tous les ordinateurs ayant installé l'application reçoivent une notification
 - Ces utilisateurs peuvent répondre "Ok !" ou "non merci !"
 - En fonction de la réponse, une LED s'allume si la personne arrive.
 
Seuls 4 clients peuvent être connectés à la fois.
Précaution à prendre : par défaut, l'adresse IP du serveur est 192.168.1.35. Il faut veiller à attribuer cette adresse IP fixe
au serveur, via les paramètres DHCP de la box par exemple. Le port utilisé est 6543.

# Comment ça marche ?

L'application utilise un serveur, installé sur la RaspberryPi, et des clients, installés sur les machines des utilisateurs.

Tout est codé en JAVA, avec quelques lignes de BASH. Le système est basé sur les Sockets.
Le serveur, lancé au démarrage de la RaspberryPi, tourne en continue dans l'attente de clients.
L'application cliente, une fois lancée, se connecte au serveur et attent une requête de celui-ci.

A chaque fois que le bouton sur la Raspberry est pressé, une socket est envoyée aux clients. Une notification apparait alors à l'écran
et demande à l'utilisateur sa réponse. Celle-ci est alors renvoyée au serveur, qui agit en fonction. Si la réponse est "Ok", la LED
correspondant à l'usager s'allume, sinon elle reste inactive.

Nous utilisons la librairie BASH gpio pour allumer les LEDs. Tous les éléments extérieurs sont directement connectés à la carte via les
pins GPIO. 

# Matériel

- Une RaspberryPi 2
- Une carte wifi USB
- 4 LED
- 4 résistances 1k ohms
- 1 bouton à impulsion
- Des cables femelles

# Evolutions prévues :

- Ajout d'un son à l'apparition de la notification
- Installation automatique du client
- Lancement au démarrage de la machine
- Nettoyage du code (...)
- Faire clignoter la LED si la réponse est "2 min !"
- Boitier imprimé en 3D
