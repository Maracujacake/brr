package web.brr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.brr.domains.Locadora;

@Repository
public interface LocadoraRep extends JpaRepository<Locadora, Long> {
    Locadora save(Locadora locadora);
    void deleteById(Long id);
    List<Locadora> findAll();
    Optional<Locadora> findById(Long id);
    Locadora findByEmail(String email); 
    Locadora findByCnpj(String cnpj);
    Locadora findByCidade(String cidade); 
}
