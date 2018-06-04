# Travail de session en équipe, dans le cadre du cours INF2015 Groupe 40 Hiver 2018.

Logiciel de manipulations de notes étudiantes pour l'UQBC.

## Notes décrivant le script de construction automatisée
* Le script de construction automatisé s'exécute à partir de l'IDE utilisé par tous les membres du groupe : NetBeans
* Le script est une suite d'instruction écrite en xml et s'exécutant en étape post-clean. Il se trouve entre les lignes 73 et 92 du fichier build.xml à la racine du projet.
* Pour exécuter le script, il faut right-click sur le projet dans Netbeans et sélectionner "Clean and Build"
* Le script va compiler les intrants (fichiers java) et y inclure les dépendances (contenus dans le répertoire/entrepôt lib). L'extrant du script est un seul fichier exécutable (jar) qui contient tout ce qui est nécessaire pour l'exécution du programme. L'extrant du script est déposé dans le répertoire "Store".
* J'ai vérifié auprès du professeur et il m'a indiqué que le script répondait aux exigences.

## Notes au correcteur
* Nous avons développer la solution en Java 1.8. 
* Veuillez vous assurer d'avoir le répertoire de Java 1.8 dans votre variable d'environnement "Path" avant tout autre répertoire de Java.
* La branche qui doit être corrigé est la branche origin/master.
* Veuillez tout d'abord cloner la branche origin/master.
* Le répertoire local (sur votre poste) du dépôt est référé en tant que [racine] dans les notes suivantes.

### Fichiers d'entrées
* Les fichiers ListeEtudiantsCours*.json doivent se trouver dans le répertoire [racine]\fileJson\listeDesCoursUQBC.
* Les fichiers evaluation*.json doivent se trouver dans le répertoire [racine]\fileJson\listeDesEvaluationsUQBC.

### Exécution du programme sans affichage des messages d'erreur
* La façon le plus simple d'exécuter le programme est de doublecliquer le fichier [racine]\run.bat. Notez que vous ne verrez aucun message d'erreur de cette façon.

### Exécution du programme avec messages d'erreur 
* Ouvrir une invite de commande à la [racine]
* Dans l'invite de commande, taper "run.bat" sans les guillemets puis appuyer sur la touche "Enter"
* Si le programme a détecté des erreurs lors de l'exécution, ces dernières seront affichées dans l'invite de commande

 
### Fichiers de sorties
* Aucun fichier de sortie n'est fourni dans le dépôt.
* Les fichiers de sorties seront créés lors de l'exécution du programme.
* Les fichiers de sorties du sprint #1 (un fichier texte par étudiant) se trouvent dans le répertoire [racine]\fileTxtByStudent.
* Les fichiers de sorties du sprint #2 (un fichier json  par groupe cours pour registraire) se trouvent dans le répertoire [racine]\fileJsonByGroupes-Cours
* Les fichiers de sorties du sprint #2 (un fichier texte par groupe cours pour professeur) se trouvent dans le répertoire [racine]\fileTxtBySummaryByEvaluation
* Les fichiers de sorties du sprint #3 (un fichier texte (echec) par groupe cours pour professeur) se trouvent dans le répertoire [racine]\fileTxtByGroupes-CoursWithFailure

## IDE pour la construction du logiciel

* [NetBeans IDE 8.2](https://netbeans.org/) - Notre équipe a utilisé cet IDE dans le cadre de la construction étape par étape du logiciel.

## Versionage

Nous utilisons [Git](https://git-scm.com/) pour le versionage du logiciel. Vous pouvez consulter notre répertoire (privé) via le lien suivant : [UQBC Hiver 2018](https://gitlab.com/mgiroux91/UQBC). 

## Auteurs

* **Benoît Mignault**
* **Marc Giroux**
* **Dominique Perron**
* **Alexis Cloutier**

Vous pouvez voir ces même contributeur sur notre dépôt [Contributeurs(e)](https://gitlab.com/mgiroux91/UQBC/project_members) qui ont participés au projet.
