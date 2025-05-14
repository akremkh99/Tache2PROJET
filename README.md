

Padel Maa Chabeb est une application JavaFX conçue pour gérer les tournois de padel.
Elle offre des fonctionnalités avancées telles que la gestion des tournois, des événements, des participants, ainsi qu'une intégration avec des API pour améliorer l'expérience utilisateur.

Fonctionnalités Principales

CRUD Complet :
Ajout, modification, suppression et affichage des tournois, événements et participants.
Contrôle de saisie robuste pour garantir la validité des données entrées.
Barre de Recherche Dynamique :
Une barre de recherche permettant de filtrer les participants en temps réel en fonction du premier caractère saisi.
Exemple : Saisir "T" affichera tous les participants dont le nom commence par "T".

API Intégrées :
API Mapbox : Visualisation des emplacements des tournois sur une carte grâce aux coordonnées (latitude et longitude).
API Twilio : Envoi automatique d'un SMS de confirmation lorsqu'un participant s'inscrit à un tournoi.
Design Professionnel :

Interfaces utilisateur modernes et ergonomiques avec une expérience utilisateur fluide.
Styles personnalisés pour les boutons, tableaux et autres composants.

Commits Réalisés
Commit 1 : Fonctionnalités CRUD et Barre de Recherche
Implémentation complète des opérations CRUD (Create, Read, Update, Delete) pour les tournois, événements et participants.
Ajout d'une barre de recherche dynamique pour filtrer les tournois en fonction du premier caractère saisi.
Validation des champs de saisie pour éviter les erreurs utilisateur.
Commit 2 : Intégration des API

API Mapbox : Ajout d'une carte interactive pour afficher les emplacements des tournois en utilisant les coordonnées (latitude et longitude).
API Twilio : Envoi d'un SMS de confirmation à chaque inscription réussie à un tournoi.
Stockage des coordonnées géographiques dans la base de données.
Commit 3 : Design des Interfaces

Refonte complète du design des interfaces pour une meilleure expérience utilisateur.
Utilisation de styles CSS personnalisés pour styliser les boutons, tableaux et autres éléments.
Amélioration de la lisibilité et de l'ergonomie des interfaces.
Technologies Utilisées

JavaFX : Framework utilisé pour développer l'interface graphique.
API Mapbox : Pour la visualisation des emplacements sur une carte.
API Twilio : Pour l'envoi de SMS de confirmation.
Git/GitHub : Pour la gestion des versions et la collaboration.
CSS : Pour le style des interfaces utilisateur.
Instructions d'Installation et d'Exécution

Prérequis
JDK 17 ou version ultérieure.
IDE compatible Java (par exemple, IntelliJ IDEA).
Clé API Mapbox et Twilio (à configurer dans le fichier .env ou directement dans le code).

Étapes pour Exécuter le Projet

Clonez le dépôt GitHub :
" git clone https://github.com/votre-nom-utilisateur/padel-maa-chabeb.git" 

Ouvrez le projet dans votre IDE préféré.

Configurez les clés API nécessaires :
Ajoutez votre clé API Mapbox dans le fichier de configuration.
Ajoutez vos identifiants Twilio (SID, Token, Numéro de téléphone).

Exécutez l'application depuis votre IDE ou via la ligne de commande :
" mvn clean javafx:run"
