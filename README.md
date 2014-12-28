ProjODT
=======

Rechercher des fichiers .odt instantanément

Pour démarrer le programme, il y a 2 manières de faire :
* L'une dite graphique
* L'autre dans le terminal précédemment ouvert

Dans les deux cas, votre programme va s'éxecuter à l'aide de la commande (si vous êtes bien dans le dossier du fichier éxecutable) :

"java -jar projODT.jar"

Partie graphique
----------------
Vous pourrez trouver une version illustrée de ce manuel à l'adresse: http://akkes.fr/projets/projodt/manuel/
Pour la partie graphique, c'est l'unique commande à rentrer pour lancer le programme.

La barre de menu possède plusieurs onglets :
* Fichier :
  * Changer la racine (ctrl+R) : change le répertoire racine des fichiers .odt dans lesquels effectuer la recherche
  * Syncroniser (ctrl+S) : reparcourt tout depuis la racine pour actualiser les fichiers .odt
  * Fermer (ctrl+W) : quitte le programme
* Aide :
  * Manuel d'utilisation : procure une aide plus avancée
  * A propos : fournit les informations sur le logiciel

L'écran de la fenêtre est composé de 2 parties. Celle du haut correspond à la barre de recherche, celle du bas est l'écran des résultats.

La recherche peut s'effectuer à l'aide d'opérateurs : ET et OU. Le ET est prioritaire et sera traité en premier lors de la recherche. Celle-ci est automatique et lorsque la recherche est effectuée, les résultats sont affichés en-dessous.

Pour ouvrir le fichier résultant de la recherche, il suffit de cliquer dessus.

Un clic simple sur la citation trouvée ouvre l'image associée au fichier .odt et affiche les informations importantes correspondante au fichier.

Partie console
--------------
Pour la partie console, il faut rajouter à cette commande certaines actions/options :

* __display__ _fichierOdtATraiter_ : permet d'afficher tous les titres et les informations utiles du fichier passé en paramètre
  * [__-h__], [__--help__] ouvre l'aide associée à l'action __display__
* __list__  : permet de lister tous les fichiers .odt à partir du répertoire racine actuel dans la base de données
  * [__-d__ _repertoireATraiter_], [__--database__ _repertoireATraiter_] liste tous les fichiers .odt à partir du répertoire passé en paramètre
  * [__-h__], [__--help__] ouvre l'aide associée à l'action __list__
* __sync__ : permet de mettre à jour la base de données.
  * [__-d__ _repertoireATraiter_], [__--database__ _repertoireATraiter_] met à jour la base passée en paramètre
  * [__-h__], [__--help__] ouvre l'aide associée à l'action __sync__
* __search__ _termesARechercher_ : permet de recherche un terme dans la base de données actuelle. Il existe la possibilité d'effectuer la recherche à l'aide des opérateurs ET et OU. Pour cela, il suffit de séparer les termes à rechercher par " ET " et/ou " OU " (le ET est prioritaire et sera traité en premier)
  * [__-d__ _repertoireATraiter_], [__--database__ _repertoireATraiter_]  recherche à partir du répertoire passé en paramètre
  * [__-h__], [__--help__] ouvre l'aide associée à l'action __search__
* __open__ _termesARechercher_ : ouvre le fichier odt contenant les termes recherchés
  * [__-d__ _repertoireATraiter_], [__--database__ _repertoireATraiter_]  recherche à partir du répertoire passé en paramètre
  * [__-h__], [__--help__] ouvre l'aide associée à l'action __search__
