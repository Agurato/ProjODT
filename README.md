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
Pour la partie graphique, c'est l'unique commande à rentrer pour lancer le programme.

Une fois la fenêtre ouverte, 

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
* __search__ _termesARechercher_ : permet de recherche un terme dans la base de données actuelle. Il existe la possibilité d'effectuer la recherche à l'aide d'opérateurs
ET et OU. Pour cela, il suffit de séparer les termes à rechercher par " ET " et/ou " OU " (le ET est prioritaire et sera traité en premier)
  * [__-d__ _repertoireATraiter_], [__--database__ _repertoireATraiter_]  recherche à partir du répertoire passé en paramètre
  * [__-h__], [__--help__] ouvre l'aide associée à l'action __search__
* __open__ _termesARechercher_ : ouvre le fichier odt contenant les termes recherchés
  * [__-d__ _repertoireATraiter_], [__--database__ _repertoireATraiter_]  recherche à partir du répertoire passé en paramètre
  * [__-h__], [__--help__] ouvre l'aide associée à l'action __search__
