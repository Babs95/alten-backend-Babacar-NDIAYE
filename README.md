# Alten E-Commerce API
> Développé par Babacar NDIAYE

Ce projet est une API REST complète pour une plateforme e-commerce, développée avec Spring Boot. L'API permet la gestion des produits, utilisateurs, paniers d'achat et listes d'envies, avec une authentification sécurisée par JWT.

## Stack Technique

* **Backend** :
   * Java 21
   * Spring Boot 3.4.5
   * Spring REST
   * Spring Data JPA
   * Spring Security
   * JWT (JSON Web Token)
   * Hibernate
   * Maven
   * Flyway (migrations de base de données)

* **Base de données** :
   * PostgreSQL

* **Tests** :

  * JUnit 5
  * Mockito
  * TestContainers (tests d'intégration avec PostgreSQL)

* **Outils de développement** :
   * Lombok (réduction de code boilerplate)
   * MapStruct (mapping objet-objet)
   * SpringDoc OpenAPI (documentation de l'API)

## Fonctionnalités Implémentées

* **Gestion de produits** : CRUD complet avec des rôles d'accès différenciés
* **Authentification** : Inscription et connexion sécurisées avec JWT
* **Autorisation** : Rôles administrateur et utilisateur
* **Gestion du panier** : Ajout, suppression et modification des produits
* **Liste d'envies** : Possibilité de sauvegarder des produits favoris
* **Documentation interactive** : Interface Swagger pour tester l'API

## Prérequis

* Java 21 ou supérieur
* Maven 3.8 ou supérieur
* PostgreSQL 13 ou supérieur
* Navigateur web moderne (pour Swagger UI)

## Installation

1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/Babs95/alten-backend-Babacar-NDIAYE.git
   cd Alten-backend-Babacar-NDIAYE
   ```

2. **Configurer la base de données** :
   * Créez une base de données PostgreSQL nommée `alten_test_db`
   * Configurez les informations de connexion dans `src/main/resources/application.yml` si nécessaire

3. **Compiler et exécuter l'application** :
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Accéder à l'application** :
   * L'API est disponible à l'adresse : `http://localhost:8080/api`
   * La documentation Swagger est disponible à : `http://localhost:8080/api/swagger-ui/index.html`

## Structure du Projet

```
src/
├── main/java/sn/babacar/alten/test/
│   ├── config/          # Configuration Spring et Swagger
│   ├── controller/      # Contrôleurs REST (endpoints)
│   ├── dtos/            # Objets de transfert de données
│   ├── entities/        # Entités JPA
│   ├── exception/       # Gestion des exceptions
│   ├── mapper/          # Mappeurs MapStruct
│   ├── repositories/    # Repositories Spring Data
│   ├── request/         # Objets de requête standardisés
│   ├── response/        # Objets de réponse standardisés
│   ├── security/        # Configuration de sécurité et JWT
│   ├── services/        # Logique métier
│   └── util/            # Classes utilitaires
└── test/java/sn/babacar/alten/test/
    ├── integration/     # Tests d'intégration avec TestContainers
    ├── services/        # Tests unitaires des services
    └── util/            # Classes utilitaires pour les tests
```
## Tests
Le projet comprend quelques tests pour garantir la qualité et la fiabilité du code :

### Tests Unitaires

 * Tests de services : Couvrent la logique métier avec des mocks pour les dépendances

### Tests d'Intégration

 * Tests de contrôleurs : Vérifient le comportement des endpoints REST
 * Utilisent TestContainers pour créer une instance PostgreSQL isolée (postgres:16.3-alpine)
 * Testent l'interaction complète entre les composants de l'application
 * Vérifient le comportement des API dans un environnement proche de la production

Pour exécuter tous les tests :
```bash
mvn test
```

## Documentation de l'API (Swagger)

Swagger a été intégré pour faciliter la documentation et le test de l'API. Cette interface interactive vous permet de visualiser et d'interagir avec les ressources sans avoir besoin d'implémenter de code client.

### Accès à Swagger UI

Une fois l'application démarrée, l'interface Swagger UI est accessible à l'adresse suivante :
```
http://localhost:8080/api/swagger-ui/index.html
```

### Authentification dans Swagger

Pour tester les endpoints sécurisés :

1. Créez un compte via `/account` ou utilisez le compte admin par défaut (email: admin@admin.com, mot de passe: admin123)
2. Connectez-vous via l'endpoint `/token` dans la section "Auth"
3. Copiez le token JWT retourné
4. Cliquez sur le bouton "Authorize" en haut de la page
5. Entrez votre token dans le format `Bearer [votre-token]`
6. Cliquez sur "Authorize" puis "Close"

### Structure de l'API

L'API est organisée en plusieurs sections :

1. **Products** - Gestion des produits
   * `GET /v1/products` - Liste tous les produits
   * `GET /v1/products/{id}` - Récupère un produit par ID
   * `POST /v1/products` - Crée un nouveau produit (admin uniquement)
   * `PATCH /v1/products/{id}` - Met à jour un produit (admin uniquement)
   * `DELETE /v1/products/{id}` - Supprime un produit (admin uniquement)

2. **Auth** - Authentification
   * `POST /account` - Création de compte
   * `POST /token` - Connexion et obtention d'un token JWT

3. **Cart** - Gestion du panier
   * `GET /v1/cart` - Récupère le panier de l'utilisateur
   * `POST /v1/cart/products/{productId}` - Ajoute un produit au panier
   * `PATCH /v1/cart/items/{itemId}` - Met à jour la quantité d'un produit
   * `DELETE /v1/cart/products/{productId}` - Supprime un produit du panier
   * `DELETE /v1/cart` - Vide le panier

4. **Wishlist** - Gestion de la liste d'envies
   * `GET /v1/wishlist` - Récupère la liste d'envies de l'utilisateur
   * `POST /v1/wishlist/products/{productId}` - Ajoute un produit à la liste
   * `DELETE /v1/wishlist/products/{productId}` - Supprime un produit de la liste
   * `DELETE /v1/wishlist` - Vide la liste d'envies

## Notes de Sécurité

* L'authentification est gérée par token JWT
* Seul l'utilisateur avec l'email admin@admin.com peut ajouter, modifier ou supprimer des produits
* Les tokens JWT expirent après 1 heure
* Les mots de passe sont hachés en utilisant BCrypt

## Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.

## Contact

Babacar NDIAYE - [babacar.ndiayepro07@gmail.com] - [https://www.linkedin.com/in/babacar-ndiaye-8abb2a160/]