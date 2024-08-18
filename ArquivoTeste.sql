-- insercao de admin
INSERT INTO user (id, nome, email, senha, role) VALUES 
(10, 'Adm', 'admin1@example.com', 'password456', 'ROLE_ADMIN');

INSERT INTO admin (id, privelegios) VALUES 
(10, 'alguns ai');

INSERT INTO user (id, nome, email, senha, role) VALUES 
(11, 'John Doe', 'john@example.com', '123', 'ROLE_CLIENTE');

INSERT INTO admin (id, telefone, sexo, cpf, dataNascimento) VALUES 
(11, '19912345678', 'Male', '48310954733', '1990-01-01');

INSERT INTO user (id, nome, email, senha, role) VALUES 
(12, 'Jane Smith', 'jane@example.com', '456', 'ROLE_CLIENTE');

INSERT INTO admin (id, telefone, sexo, cpf, dataNascimento) VALUES 
(12, '16987654321', 'Female', '43937853922', '2000-10-12');

INSERT INTO user (id, nome, email, senha, role) VALUES 
(13, 'Locadora Web', 'webloc@example.com', '123456', 'ROLE_LOCADORA');

INSERT INTO locadora (id, cidade, cnpj) VALUES 
(13, 'São Carlos', '44999263000121');

INSERT INTO user (id, nome, email, senha, role) VALUES 
(13, 'Locadora Concorrente', 'concorrenteloc@example.com', '654321', 'ROLE_LOCADORA');

INSERT INTO locadora (id, cidade, cnpj) VALUES 
(13, 'São Carlos', '64837088000183');
