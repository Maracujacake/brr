-- insercao de admin
INSERT INTO user (id, nome, email, senha, role) VALUES 
(10, 'Adao', 'Adao@peganomeupão.com', '123456', 'ROLE_ADMIN');

INSERT INTO admin (id, privelegios) VALUES 
(10, 'alguns ai');

UPDATE user SET senha = '$2a$10$i4HFzyCRjd5KfAaR2NVFqeG2fGsrQVf.KDOPHyiunLRCWm9UaC.kK' WHERE id = 10;