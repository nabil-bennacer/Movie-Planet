DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nom VARCHAR(255),
    email VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'visitor' CHECK (role IN ('admin', 'visitor'))
);

-- Insertion de l'utilisateur Admin
INSERT INTO users (username, password, nom, email, role)
VALUES ('admin', '1234', 'Administrateur', 'admin@movieplanet.fr', 'admin')
ON CONFLICT (username) DO NOTHING;

-- Insertion d'un utilisateur Visiteur pour tester
INSERT INTO users (username, password, nom, email, role)
VALUES ('visitor', '1234', 'Visiteur Test', 'visitor@movieplanet.fr', 'visitor')
ON CONFLICT (username) DO NOTHING;

-- 1. Création de la table 'articles' (identique à celle définie dans Java)
CREATE TABLE IF NOT EXISTS articles (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prix DOUBLE PRECISION,
    description TEXT,
    image_url VARCHAR(255),
    stock INT,
    film_lie VARCHAR(255)
);

-- Article 1 : Dispo, Star Wars
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Sabre Laser Skywalker', 129.99, 'Réplique officielle FX du sabre laser bleu.', 'img/sabre_blue.png', 15, 'Star Wars');

-- Article 2 : Dispo, Batman
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('T-Shirt Logo Batman', 24.50, 'T-shirt noir 100% coton, taille L.', 'img/tshirt_batman.png', 50, 'The Dark Knight');

-- Article 3 : RUPTURE DE STOCK (pour tester le message d''erreur)
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Figurine Iron Man Collector', 250.00, 'Édition limitée Mark IV, 30cm.', 'img/ironman_fig.png', 0, 'Avengers');

-- Article 4 : Stock Faible (pour tester la décrémentation jusqu''à 0)
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Affiche Vintage Pulp Fiction', 15.00, 'Format A2, papier glacé.', 'img/pulp_poster.png', 2, 'Pulp Fiction');

-- Article 5 : Autre film (pour tester la recherche "Harry Potter")
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Baguette de Sureau', 39.99, 'Réplique en résine, boîte Ollivander incluse.', 'img/baguette.png', 10, 'Harry Potter');

-- Article 6 : Un item pas cher
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Porte-clés Groot', 5.50, 'Petit Groot dansant.', 'img/groot_key.png', 100, 'Guardians of the Galaxy');

-- Article 7 : Test recherche partielle (ex: chercher "Seigneur")
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Anneau Unique', 59.90, 'Plaqué or avec inscription elfique.', 'img/ring.png', 5, 'Le Seigneur des Anneaux');