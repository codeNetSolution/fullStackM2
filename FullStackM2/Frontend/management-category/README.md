
# README

## Introduction
Cette application est constituée de deux parties :
- Une application frontend développée avec Angular.
- Une application backend développée avec Spring Boot 3.3.4 et utilisant MariaDB comme base de données.

Ce README explique les choix technologiques, comment lancer les deux parties de l'application, avec et sans Docker, ainsi que l'accès à la documentation Swagger.

---

## Frontend

### Technologies utilisées
- **Angular** : Choisi pour sa structure robuste, son support pour les applications modulaires à grande échelle, choisi également pour m'améliorer dans cette technologie et c'est la première fois que j'utilise cette technologie.
- **TailwindCSS** : Permet un design rapide et cohérent grâce à ses classes utilitaires. Il réduit le temps de développement et simple à utiliser.

### Prérequis
- Node.js (version 16 ou supérieure)
- npm

### Installation et lancement (sans Docker)
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/codeNetSolution/fullStackM2.git
   cd FullStackM2/frontend/management-category/
   ```
2. Installez les dépendances :
   ```bash
   npm install
   ```
3. Lancez le serveur de développement :
   ```bash
   ng serve
   ```
4. Accédez à l'application dans votre navigateur :
   [http://localhost:4200]

---

## Backend

### Technologies utilisées
- **Spring Boot 3.3.4** : Offrant un démarrage rapide et une configuration simplifiée pour les applications Java, Spring Boot est idéal pour développer des API REST robustes et performantes.
- **MariaDB** : Choisi pour sa compatibilité avec MySQL, son utilisation simple.

### Prérequis
- Java 17 ou supérieur
- Maven
- MariaDB (version 10.6 ou supérieure)

### Configuration de la base de données
1. Installez MariaDB sur votre machine.
2. Créez une base de données pour l'application :
   ```sql
   CREATE DATABASE categories_db;
   ```
3. Configurez les paramètres de connexion dans le fichier `application.properties` :
   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/categories_db
   spring.datasource.username=admin
   spring.datasource.password=admin
   ```

### Installation et lancement (sans Docker)
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/codeNetSolution/fullStackM2.git
   cd FullStackM2/
   ```
2. Compilez et lancez l'application :
   ```bash
   mvn spring-boot:run
   ```
3. L'application sera accessible sur : [http://localhost:8080](http://localhost:8080)

---

## Lancement avec Docker Compose

### Prérequis
- Docker
- Docker Compose

### Étapes
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/codeNetSolution/fullStackM2.git
   cd FullStackM2/
   ```
2. Lancez les conteneurs avec Docker Compose :
   ```bash
   docker-compose up --build
   ```
3. Accédez à l'application :
   - Frontend : [http://localhost:4200](http://localhost:4200)
   - Backend : [http://localhost:8080](http://localhost:8080)
   - Documentation Swagger : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Documentation Swagger
La documentation complète des API est disponible une fois le backend lancé, via ce lien :
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Conclusion
Cette application est conçue pour être facile à lancer et maintenir, que ce soit en mode local ou avec Docker. Les choix d'Angular, TailwindCSS, Spring Boot et MariaDB assurent performance, rapidité de développement et scalabilité.
