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
    ClienteRep clienteDAO;

    public Cliente save(Cliente Cliente) {
        Optional<web.brr.domains.Cliente> nome = clienteDAO.findByEmailOrCpfOrTelefone(Cliente.getEmail(),
                Cliente.getCpf(), Cliente.getTelefone());
        if (nome.isPresent()) {
            return null;
        }
        Cliente.setSenha(EncryptPassword.encrypt(Cliente.getSenha()));
        Cliente.setRole("ROLE_CLIENTE");
        return clienteDAO.save(Cliente);
    }

    public void deleteById(Long id) {
        clienteDAO.deleteById(id);
    }

    public List<Cliente> findAll() {
        return clienteDAO.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteDAO.findById(id);
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteDAO.findByEmail(email);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return clienteDAO.findByCpf(cpf);
    }

    public Optional<Cliente> findByTelefone(String telefone) {
        return clienteDAO.findByTelefone(telefone);
    }
}
