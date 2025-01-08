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