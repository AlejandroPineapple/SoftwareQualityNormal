GRANT ALL PRIVILEGES ON calidadSoftware2024.* TO 'root'@'%' IDENTIFIED BY '123456';
FLUSH PRIVILEGES;

CREATE TABLE usuariosCool (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    isLogged BOOLEAN DEFAULT FALSE
);