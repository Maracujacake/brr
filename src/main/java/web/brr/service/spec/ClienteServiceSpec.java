package web.brr.service.spec;


import java.util.List;
import java.util.Optional;

import web.brr.domains.Cliente;

public interface ClienteServiceSpec {
    Cliente save(Cliente user);

    void deleteById(Long id);

    List<Cliente> findAll();

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);

    Optional<Cliente> findByTelefone(String telefone);

}
