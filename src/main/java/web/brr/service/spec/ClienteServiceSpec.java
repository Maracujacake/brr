package web.brr.service.spec;

import java.util.List;
import java.util.Optional;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;

public interface ClienteServiceSpec {
    Cliente save(Cliente user, Boolean update);

    void deleteById(Long id);

    List<Cliente> findAll();

    List<Locacao> findRegistrations(String id);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);

    Optional<Cliente> findByTelefone(String telefone);

    List<Cliente> findByEmailOrCpfOrTelefone(String email, String cpf, String telefone);
}
