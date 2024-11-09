CREATE DATABASE IF NOT EXISTS categories_db;
USE categories_db;

CREATE TABLE IF NOT EXISTS category (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        parent_id INT,
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

