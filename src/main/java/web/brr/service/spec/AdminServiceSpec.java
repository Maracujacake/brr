package web.brr.service.spec;

import java.util.List;
import java.util.Optional;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;
import web.brr.domains.Admin;

public interface AdminServiceSpec {
    void deleteClienteById(Long id);

    List<Cliente> findAllClients();

    List<Locacao> findRegistrations(String id);

    Optional<Cliente> findClienteById(Long id);

    Optional<Cliente> findClienteByEmail(String email);

    Optional<Cliente> findClienteByCpf(String cpf);

    Optional<Cliente> findClienteByTelefone(String telefone);

    Optional<Admin> findByEmail(String email);

    Admin save(Admin admin, Boolean update);

    Optional<Admin> findById(Long id);
}
