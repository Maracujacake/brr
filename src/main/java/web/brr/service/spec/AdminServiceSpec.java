package web.brr.service.spec;

import java.util.List;
import java.util.Optional;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;
import web.brr.domains.Admin;
import web.brr.domains.Locadora;

public interface AdminServiceSpec {
    // cliente
    Cliente save(Cliente user, Boolean update);

    void deleteClienteById(Long id);

    List<Cliente> findAllClients();

    List<Locacao> findRegistrations(String id);

    Optional<Cliente> findClienteById(Long id);

    Optional<Cliente> findClienteByEmail(String email);

    Optional<Cliente> findClienteByCpf(String cpf);

    Optional<Cliente> findClienteByTelefone(String telefone);

    // locadora
    void deleteLocadoraById(Long id);

    Optional<Locadora> findLocadoraById(Long id);

    Optional<Locadora> findLocadoraByEmail(String cnpj);

    List<Locacao> All_Locacoes_Locadora(String id);

    Locadora save(Locadora locadora, Boolean update);


    // admin

    Optional<Admin> findByEmail(String email);

    Admin save(Admin admin, Boolean update);

    Optional<Admin> findById(Long id);
}
