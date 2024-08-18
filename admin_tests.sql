-- insercao de admin
INSERT INTO user (id, nome, email, senha, role) VALUES 
(10, 'Adao', 'Adao@peganomeupao.com', '123456', 'ROLE_ADMIN');

INSERT INTO admin (id, privelegios) VALUES 
(10, 'alguns ai');

-- senha: pedro 
UPDATE user SET senha = '$2a$10$KsdBeBDVYXaT4HZkvKjA9uly13r1.fxBwNJF7teGpkS2u/XkPKdwG' WHERE id = 10;