package web.brr.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.brr.domains.Cliente;
import web.brr.encrypt.EncryptPassword;
import web.brr.repositories.ClienteRep;
import web.brr.service.spec.ClienteServiceSpec;

@Service
public class ClienteService implements ClienteServiceSpec {
    @Autowired
    ClienteRep dao;

    public Cliente save(Cliente Cliente) {
        Optional<web.brr.domains.Cliente> nome = dao.findByEmailOrCpfOrTelefone(Cliente.getEmail(), Cliente.getCpf(), Cliente.getTelefone());
        if(nome.isPresent()){
            return null;
        }
        Cliente.setSenha(EncryptPassword.encrypt(Cliente.getSenha()));
        Cliente.setRole("ROLE_CLIENTE");
        return dao.save(Cliente);
    }

    public void deleteById(Long id) {
        dao.deleteById(id);
    }

    public List<Cliente> findAll() {
        return dao.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return dao.findById(id);
    }

    public Optional<Cliente> findByEmail(String email) {
        return dao.findByEmail(email);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return dao.findByCpf(cpf);
    }
    public Optional<Cliente> findByTelefone(String telefone) {
        return dao.findByTelefone(telefone);
    }
}
