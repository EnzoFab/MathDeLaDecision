# MathDeLaDecision
Projet mathématiques de la décision premier semestre de 4ème année réalisé par : Ehamelo, Rul, Fabre.

Le but : trouver une méthode permettant la répartition des étudiants 
dans différents groupes de projets en fonction des appréciations qu'ils se donnent entre eux et de celles qu'ils donnent aux projets.


Notre méthode, pour l’instant, ne concerne que la formation des groupes. Nous avons un nombre déjà défini de binômes et de trinômes à composer. Nous effectuons d’abord des binômes et à la fin les trinômes. 
Le critère que l’on cherche à maximiser est le taux de satisfaction. Le taux de satisfaction est le rapport entre le nombre de groupes satisfaits et le nombre total de groupes formés. Un groupe est satisfait si le poids dans ce groupe est inférieur à 5 pour les binômes et à 15 pour les trinômes (nous avons pris la moitié des poids maximaux, c’est-à-dire dans le pire des cas). 
Le poids est fonction d’une mesure que nous avons appelée “distance”. Pour cette mesure, nous nous basons en priorité sur la différence entre la meilleure mention donnée par un élève A (à tous les élèves confondus) et la mention donnée par l’élève A à un élève précis B. Ainsi le poids d’un binôme (A,B) est : distance(A,B)+distance(B,A) et le poids d’un trinôme(A,B,C) est quant à lui est poids(A,B)+poids(B,C)+poids(A,C). 
 Il existe des critères qui permettent de choisir lorsqu’il y a plusieurs possibilités d’affectation (nous les appellerons “candidats”). Le premier critère est le nombre de personnes à qui le candidat a donné une mention égale ou supérieure à la mention utilisée pour le calcul du poids. On cherche alors parmi ces candidats ceux qui en comptabilisent le moins. Si il y a encore égalité, à ce moment là nous utilisons un autre critère qui est le profil. Le profil d’un élève étant la médiane des mentions qui lui ont été attribués par tous les élèves plus ou moins la distance des autres mentions qu’il a reçues au dessus et en dessous de cette mention. Nous privilégions les élèves qui ont un moins bon profil qu’un autre en cas d’égalité car ce premier sera pour nous moins facile à mettre en groupe plus tard.
Le résultat est un fichier excel qui contient l’ensemble de binômes et trinômes formés avec le poids associé à chaque groupe.

