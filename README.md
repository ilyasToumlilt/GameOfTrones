# GameOfTrones
M2_PPM - Projet Android

== Participants : 2 ==

   Adrien Ferreira - 3405623
   Ilyas  Toumlilt - 3261538
   
== Options implémentée ==

--> Partie Obligatoire:
    Ecran d'accueil avec demande nom utilisateur - OK
    NavigationDrawer avec Profile/Carte/Armes - OK
    Affichage Position courante et sanisettes - OK
    Affichage Profil pour changer son status - OK
    Affichage de la liste des armes - OK
    Cercle d'action à partir de la position courante - OK
    Notification pour informer l'utilisateur de ce qu'on fait - OK

--> Partie Bonus :
    Gestion de la rotation - OK
    Persistence durable - OK
    Liste des armes personnalisée - OK
    Ajout durable des sanisettes - OK
    Musique de jeu + bruit lors d'une attaque - OK

== Architecture du projet ==

   Le projet se compose de 4 activitées, une première, SignUpActivity
   permettant d'enregistrer un nouvel utilisateur en renseignant son
   pseudo. Une deuxième la plus pertinante, GameActivity, qui va hériter
   de AppCompatActivity et qui contiendra le NavigationDrawer, le Fragment
   pour la carte googleMap, et qui s'occupera d'instancier les deux activités
   restantes, ProfileActivity, pour pouvoir modifier le pseudo du joueur et
   son status, et WeaponsActivity, qui permet de sélectionner une autre
   arme.
   Trois classes modèle sont également livrées, Weapon pour les armes,
   une arme est définie par son nom, le coût de ses dégats, et sa portée
   d'attaque; Sanitary pour une sanisette, définie par ses coordonnées et les
   points de vie qui lui restent; Player pour un joueur, défini par son
   pseudo et son statut.
   Ainsi, la GameActivity va se charger, d'un côté de la gestion de la Map,
   à savoir catcher les événements, la localisation ( et toutes les 
   permission qui vont avec ), l'affichage des Marker pour Sanisettes, et
   le cercle d'attaque pour les Weapons ainsi que la mise à jour des données
   de ces deux entitées. D'un autre côté, elle s'occupera du moteur de jeu
   à savoir l'intéraction entre les événements utilisateur, et les données
   du jeu.
   Les classes sont documentées ( en fran-glais :-x )
   //TODO persistence

== Difficultées rencontrées ==

   La réalisation du projet nous a pris deux jours de travail, dont une bonne
   partie a été consacrée à l'affichage de la map et la connexion aux Google
   Play Services, et aux fetching des resources depuis la base ( URL fourni )
   Le projet a été testé et fonctionne sur le Nexus 7.