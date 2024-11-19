CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    /* BCrypt has a 60 char length */
    password CHAR(60) NOT NULL
);
