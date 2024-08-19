-- insercao de admin
INSERT INTO user (id, nome, email, senha, role) VALUES 
(10, 'Adao', 'Adao@peganomeupao.com', '123456', 'ROLE_ADMIN');

INSERT INTO admin (id, privelegios) VALUES 
(10, 'alguns ai');

-- senha: pedro 
UPDATE user SET senha = '$2a$10$Mu1WbGZvpAsw.FRW4RLfcOsysF8PLNaO5vKDxoOadIiaFMtmADh4S' WHERE id = 10;