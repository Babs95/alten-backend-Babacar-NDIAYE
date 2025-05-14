# alten-backend-Babacar-NDIAYE
> Spring Boot test_api_rest_modelsis

## Stack
*	Java 21
*	Spring Boot 3.4.5
*	Spring Rest.
*	Spring Data.
*   Spring Security
*	JPA.
*	Maven
*	Postgresql Database
*	Lombok
*	MapStruct
*   Swagger Api Doc

## HOW TO BUILD AND RUN
* Java 21
* IntelliJ IDEA
* Clone the repo
* Open with IntelliJ
* Use Maven to download all dependencies
* Build and run the project

Swagger a été intégré à l'application pour faciliter la documentation et le test de l'API. Cette interface interactive vous permet de visualiser et d'interagir avec les ressources de l'API sans avoir besoin d'implémenter de code client.

## Accès à Swagger UI

Une fois l'application démarrée, l'interface Swagger UI est accessible à l'adresse suivante :

```
http://localhost:8080/api/swagger-ui/index.html
```

## Fonctionnalités disponibles

### 1. Exploration de l'API

- Visualisation de tous les endpoints disponibles
- Description détaillée de chaque opération
- Documentation des paramètres, des corps de requête et des réponses

### 2. Authentification

Pour tester les endpoints sécurisés :

1. Connectez-vous d'abord via l'endpoint `/token` dans la section "Auth"
2. Copiez le token JWT retourné (sans le préfixe "Bearer")
3. Cliquez sur le bouton "Authorize" en haut de la page
4. Entrez votre token dans le format `Bearer [votre-token]` (exemple: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`)
5. Cliquez sur "Authorize" puis "Close"

Une fois authentifié, vous pourrez tester toutes les endpoints, y compris celles qui nécessitent des droits d'administrateur.

### 3. Test des endpoints

- Cliquez sur un endpoint pour le développer
- Remplissez les paramètres requis
- Cliquez sur "Try it out" pour exécuter la requête
- Visualisez la réponse complète, y compris les en-têtes et le corps

## Structure de l'API

L'API est organisée en plusieurs sections :

1. **Products** - Gestion des produits
    - `GET /v1/products` - Liste tous les produits
    - `GET /v1/products/{id}` - Récupère un produit par ID
    - `POST /v1/products` - Crée un nouveau produit (admin uniquement)
    - `PATCH /v1/products/{id}` - Met à jour un produit (admin uniquement)
    - `DELETE /v1/products/{id}` - Supprime un produit (admin uniquement)

2. **Auth** - Authentification
    - `POST /account` - Création de compte
    - `POST /token` - Connexion et obtention d'un token JWT

3. **Cart** - Gestion du panier
    - Différentes opérations pour gérer le panier d'achat de l'utilisateur

4. **Wishlist** - Gestion de la liste d'envies
    - Différentes opérations pour gérer la liste d'envies de l'utilisateur

## Notes importantes

1. Les endpoints nécessitant une authentification renverront une erreur 401 si aucun token n'est fourni
2. Les endpoints réservés aux administrateurs renverront une erreur 403 si vous n'êtes pas authentifié en tant qu'admin
3. Le token JWT expire après une période définie (généralement 1 heure)

## Schémas de données

Swagger UI affiche également les schémas de données utilisés par l'API, ce qui vous permet de comprendre la structure des objets manipulés.
