package web.brr.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;
import web.brr.encrypt.EncryptPassword;
import web.brr.repositories.ClienteRep;
import web.brr.service.spec.ClienteServiceSpec;

@Service
public class ClienteService implements ClienteServiceSpec {
    @Autowired
    ClienteRep clienteDAO;

    public Cliente save(Cliente Cliente, Boolean update) {
        Cliente.setRole("ROLE_CLIENTE");
        if (update)
            return clienteDAO.save(Cliente);

        Cliente.setSenha(EncryptPassword.encrypt(Cliente.getSenha()));
        return clienteDAO.save(Cliente);
    }

    public void deleteById(Long id) {
        clienteDAO.deleteById(id);
    }

    public List<Cliente> findAll() {
        return clienteDAO.findAll();
    }

    public List<Locacao> findRegistrations(String id) {
        return clienteDAO.findRegistrations(id);
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

    @Deprecated
    public List<Cliente> findByEmailOrCpfOrTelefone(String email, String cpf, String telefone) {
        return clienteDAO.findByEmailOrCpfOrTelefone(email, cpf, telefone);
    }
}
