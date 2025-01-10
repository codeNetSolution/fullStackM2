CREATE DATABASE IF NOT EXISTS categories_db;
USE categories_db;

CREATE TABLE IF NOT EXISTS category (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        parent_id INT,
                                        is_root BOOLEAN DEFAULT false,
                                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        nom VARCHAR(255) NOT NULL

    );

CREATE TABLE category_hierarchy (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    child_id INT NOT NULL,
                                    parent_id INT NOT NULL,
                                    FOREIGN KEY (child_id) REFERENCES category(id),
                                    FOREIGN KEY (parent_id) REFERENCES category(id)
);

ALTER TABLE category ADD CONSTRAINT chk_is_root_parent_id
    CHECK ((is_root = true AND parent_id IS NULL) OR (is_root = false));



INSERT INTO category (nom, is_root, parent_id) VALUES
                                                   ('Racine 1', true, NULL),
                                                   ('Racine 2', true, NULL),
                                                   ('Racine 3', true, NULL),
                                                   ('Racine 4', true, NULL),
                                                   ('Racine 5', true, NULL),
                                                   ('Racine 6', true, NULL);

INSERT INTO category (nom, is_root, parent_id) VALUES
                                                   ('Sous-catégorie 1', false, 1),
                                                   ('Sous-catégorie 2', false, 1),
                                                   ('Sous-catégorie 3', false, 2),
                                                   ('Sous-catégorie 4', false, 2),
                                                   ('Sous-catégorie 5', false, 3),
                                                   ('Sous-catégorie 6', false, 3),
                                                   ('Sous-catégorie 7', false, 4),
                                                   ('Sous-catégorie 8', false, 4),
                                                   ('Sous-catégorie 9', false, 5),
                                                   ('Sous-catégorie 10', false, 5),
                                                   ('Sous-catégorie 11', false, 6),
                                                   ('Sous-catégorie 12', false, 6),
                                                   ('Sous-catégorie 13', false, 1),
                                                   ('Sous-catégorie 14', false, 2);

INSERT INTO category_hierarchy (child_id, parent_id) VALUES
                                                         (7, 1), (8, 1),
                                                         (9, 2), (10, 2),
                                                         (11, 3), (12, 3),
                                                         (13, 4), (14, 4),
                                                         (15, 5), (16, 5),
                                                         (17, 6), (18, 6);