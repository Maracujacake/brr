package web.brr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.brr.domains.Cliente;

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
}
