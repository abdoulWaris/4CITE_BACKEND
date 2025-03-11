# 4CITE_BACKEND

Repo pour le projet de test et d'intégration d'un projet de gestion d'hôtels (avec pipeline CI/CD). Ce dépôt contient uniquement le code du backend.

## Technologies Utilisées

- **Langage**: Java

## Fonctionnalités

- Gestion des réservations d'hôtel
- Gestion des clients(users)
- Gestion des chambres
- Intégration continue et déploiement continu (CI/CD)
- Test Unitaire et fonctionnels

## Installation

1. Clonez le dépôt :
    ```sh
    git clone https://github.com/abdoulWaris/4CITE_BACKEND.git
    ```
2. Naviguez dans le répertoire du projet :
    ```sh
    cd 4CITE_BACKEND
    ```
3.Selon votre IDE configurer votre .env comme ceci:
```sh
SPRING_APPLICATION_NAME= AkkordHotel
SPRING_DATASOURCE_URL=url_base_de_donnée
SPRING_DATASOURCE_USERNAME= username
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect
SPRING_SWAGGERSOURCE_PATH=/swagger-ui.html
SPRING_JWT_SECRET_TOKEN= un secret toke de votre choix(ex:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970)
```
4. Compilez et exécutez le projet Java avec votre IDE préféré ou en utilisant Maven/Gradle.
5. lancer la dans votre de donné créez un schéma AkkordHotel et lancer l'application

## Utilisation

1. Démarrez le serveur backend.
2. utilise ##mvn clean install pour lancer tout les tests
3. Utilisez des outils comme Postman pour interagir avec les API disponibles.
4. Consultez la documentation des API pour plus de détails sur les endpoints disponibles.

## Contribuer

1. Fork le projet.
2. Créez votre branche feature (`git checkout -b feature/AmazingFeature`).
3. Commitez vos changements (`git commit -m 'Add some AmazingFeature'`).
4. Pushez sur la branche (`git push origin feature/AmazingFeature`).
5. Ouvrez une Pull Request.

## Licence

Distribué sous la licence MIT. Voir `LICENSE` pour plus d'informations.
