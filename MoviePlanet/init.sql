-- 1. Nettoyage initial
DROP TABLE IF EXISTS commentaires CASCADE;
DROP TABLE IF EXISTS articles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 2. Création de la table 'users'
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
VALUES ('admin', '1234', 'Administrateur', 'admin@movieplanet.fr', 'admin');

-- Insertion d'un utilisateur Visiteur générique
INSERT INTO users (username, password, nom, email, role)
VALUES ('visitor', '1234', 'Visiteur Test', 'visitor@movieplanet.fr', 'visitor');

-- Insertion des utilisateurs fictifs pour les commentaires
INSERT INTO users (username, password, nom, email, role) VALUES 
('yassin', '1234', 'DAANOUN Yassin', 'yassin@test.fr', 'visitor'),
('justin', '1234', 'CHAPON Justin', 'justin@test.fr', 'visitor'),
('carolina', '1234', 'BRAVO APAZA Carolina', 'carolina@test.fr', 'visitor'),
('nabil', '1234', 'BENNACER Nabil', 'nabil@test.fr', 'visitor');


-- 3. Création de la table 'articles'
CREATE TABLE IF NOT EXISTS articles (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prix DOUBLE PRECISION,
    description TEXT,
    image_url VARCHAR(255),
    stock INT,
    film_lie VARCHAR(255)
);

-- Article 1 : Star Wars
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Sabre Laser Skywalker', 129.99, 'Réplique officielle FX du sabre laser bleu.', 'img/sabre.png', 15, 'Star Wars');

-- Article 2 : Batman
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('T-Shirt Logo Batman', 24.50, 'T-shirt noir 100% coton, taille L.', 'img/tshirt_batman.png', 50, 'The Dark Knight');

-- Article 3 : Iron Man (Rupture de stock pour test)
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Figurine Iron Man Collector', 250.00, 'Édition limitée Mark IV, 30cm.', 'img/ironman_fig.png', 0, 'Avengers');

-- Article 4 : Harry Potter
INSERT INTO articles (nom, prix, description, image_url, stock, film_lie)
VALUES ('Baguette de Sureau', 39.99, 'Réplique en résine, boîte Ollivander incluse.', 'img/baguette.png', 10, 'Harry Potter');


-- 4. Création de la table 'commentaires'
CREATE TABLE IF NOT EXISTS commentaires (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    article_id INT NOT NULL,
    texte TEXT NOT NULL,
    note INT CHECK (note >= 0 AND note <= 5),
    date_publication TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

-- NOTE: Les IDs supposés ici sont :
-- Users: 1(admin), 2(visitor), 3(yassin), 4(justin), 5(carolina), 6(nabil)
-- Articles: 1(Sabre), 2(T-Shirt), 3(Iron Man), 4(Baguette)

-- Commentaires pour Article 1 (Sabre Laser)
INSERT INTO commentaires (user_id, article_id, texte, note) VALUES
(4, 1, 'Incroyable ! La qualité est top, on s''y croirait vraiment.', 5), -- Justin
(6, 1, 'Un peu cher pour ce que c''est, mais bel objet de collection.', 4); -- Nabil

-- Commentaires pour Article 2 (T-Shirt Batman)
INSERT INTO commentaires (user_id, article_id, texte, note) VALUES
(3, 2, 'Taille parfaitement, le coton est agréable.', 5), -- Yassin
(5, 2, 'Le logo a un peu bougé au lavage, attention.', 3), -- Carolina
(2, 2, 'Classique et efficace.', 4); -- Visitor

-- Commentaires pour Article 3 (Figurine Iron Man - Rupture)
INSERT INTO commentaires (user_id, article_id, texte, note) VALUES
(4, 3, 'La pièce maîtresse de ma collection !', 5), -- Justin
(5, 3, 'Dommage qu''elle soit si souvent en rupture, j''ai eu de la chance.', 5); -- Carolina

-- Commentaires pour Article 4 (Baguette Sureau)
INSERT INTO commentaires (user_id, article_id, texte, note) VALUES
(6, 4, 'Très belle boîte, idéale pour un cadeau.', 5), -- Nabil
(3, 4, 'Un peu légère en main, je m''attendais à plus lourd.', 3); -- Yassin