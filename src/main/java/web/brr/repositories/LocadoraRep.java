package web.brr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import web.brr.domains.Cliente;
import web.brr.domains.Locadora;

@Repository
public interface LocadoraRep extends JpaRepository<Locadora, Long> {
    @SuppressWarnings("unchecked")
    Locadora save(Locadora locadora);

    void deleteById(Long id);

    List<Locadora> findAll();

    Optional<Locadora> findById(Long id);

    Optional<Locadora> findByEmail(String email);

    Optional<Locadora> findByCnpj(String cnpj);

    Optional<Locadora> findByCidade(String cidade);

    @Query("SELECT loc FROM Locadora loc WHERE loc.email = :email OR loc.cnpj = :cnpj")
    Optional<Cliente> findByEmailOrCnpj( @Param("email") String email, @Param("cnpj") String cnpj );
}
