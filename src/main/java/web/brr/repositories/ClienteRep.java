package web.brr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;

@Repository
public interface ClienteRep extends JpaRepository<Cliente, Long> {
    @SuppressWarnings("unchecked")
    Cliente save(Cliente cliente);

    void deleteById(Long Id);

    List<Cliente> findAll();

    Optional<Cliente> findById(Long Id);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);

    Optional<Cliente> findByTelefone(String telefone);

    List<Locacao> findRegistrations();

    @Query("SELECT c FROM Cliente c WHERE c.email = :email OR c.cpf = :cpf OR c.telefone = :telefone")
    Optional<Cliente> findByEmailOrCpfOrTelefone(@Param("email") String email, @Param("cpf") String cpf,
            @Param("telefone") String telefone);
}
