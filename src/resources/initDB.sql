CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50),
  email VARCHAR(50),
  password VARCHAR(50),
  isLogged BOOLEAN
);