package web.brr.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.brr.domains.Admin;
import web.brr.domains.Cliente;
import web.brr.domains.Locacao;
import web.brr.encrypt.EncryptPassword;
import web.brr.repositories.AdminRep;
import web.brr.repositories.ClienteRep;
import web.brr.service.spec.AdminServiceSpec;

@Service
public class AdminService implements AdminServiceSpec {

    @Autowired
    ClienteRep clienteRep;

    @Autowired
    AdminRep adminRep;

    // Métodos de cliente
    @Override
    public void deleteClienteById(Long id) {
        clienteRep.deleteById(id);
    }

    @Override
    public List<Cliente> findAllClients() {
        return clienteRep.findAll();
    }

    @Override
    public List<Locacao> findRegistrations(String id) {
        return clienteRep.findRegistrations(id);
    }

    @Override
    public Optional<Cliente> findClienteById(Long id) {
        return clienteRep.findById(id);
    }

    @Override
    public Optional<Cliente> findClienteByEmail(String email) {
        return clienteRep.findByEmail(email);
    }

    @Override
    public Optional<Cliente> findClienteByCpf(String cpf) {
        return clienteRep.findByCpf(cpf);
    }

    @Override
    public Optional<Cliente> findClienteByTelefone(String telefone) {
        return clienteRep.findByTelefone(telefone);
    }

    // Métodos de admin
    public Optional<Admin> findByEmail(String email) {
        return adminRep.findByEmail(email);
    }

    public Optional<Admin> findById(Long id) {
        return adminRep.findById(id);
    }

    public Admin save(Admin admin, Boolean update) {
        admin.setPrivelegios("0");
        if (update)
            return adminRep.save(admin);
        Optional<web.brr.domains.Admin> nome = adminRep.findByEmail(admin.getEmail());
        if (nome.isPresent()) {
            return null;
        }

        admin.setSenha(EncryptPassword.encrypt(admin.getSenha()));
        admin.setNome((admin.getNome()));
        admin.setRole("ROLE_ADMIN");
        admin.setEmail(admin.getEmail());
        admin.setPrivelegios(admin.getPrivelegios());
        return adminRep.save(admin);
    }

}
