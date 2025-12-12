-- Création de la table (au cas où elle n'existe pas encore)
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nom VARCHAR(255),
    email VARCHAR(255)
);

-- Insertion de l'utilisateur par défaut (Admin)
-- ON CONFLICT (username) DO NOTHING permet d'éviter les erreurs si on relance le script
INSERT INTO users (username, password, nom, email)
VALUES ('testUser', 'testPass', 'Utilisateur Test', 'test@movieplanet.fr')
ON CONFLICT (username) DO NOTHING;