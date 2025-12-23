-- Création de la table (au cas où elle n'existe pas encore)
-- Cette création se fait dans néon
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nom VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) DEFAULT 'User'
);

--Insertion de l'Admin
INSERT INTO users (username, password, nom, email, role)
VALUES ('admin', 'admin', 'Administrateur', 'admin@movieplanet.fr', 'Admin')
ON CONFLICT (username) DO NOTHING

-- Insertion de l'utilisateur de test
-- ON CONFLICT (username) DO NOTHING permet d'éviter les erreurs si on relance le script
INSERT INTO users (username, password, nom, email,role)
VALUES ('testUser', 'testPass', 'Utilisateur Test', 'test@movieplanet.fr','User')
ON CONFLICT (username) DO NOTHING;