package web.brr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import web.brr.domains.Locacao;

@Repository
public interface LocacaoRep extends JpaRepository<Locacao, Long> {
    @SuppressWarnings("unchecked")
    Locacao save(Locacao Locacao);

    void deleteById(Long id);

    List<Locacao> findAll();

    Optional<Locacao> findById(Long id);


    @Query("SELECT c FROM Locacao c WHERE c.cliente.id = :id")
    List<Locacao> findClienteRegistrations(@Param("id") String id);

    @Query("SELECT c FROM Locacao c WHERE c.locadora.id = :id")
    List<Locacao> findLocadoraRegistrations(@Param("id") String id);

    @Query("SELECT c FROM Locacao c WHERE c.cliente.id = :idCliente AND c.locadora.id = :idLocadora")
    List<Locacao> findClienteAndLocadoraRegistrations(@Param("idCliente") String idCliente, @Param("idLocadora") String idLocadora);

    @Query("SELECT c FROM Locacao c WHERE c.registeredAt = :registeredAt AND c.locadora.id = :idLocadora")
    List<Locacao> findLocacaoLocadoraByDate(@Param("registeredAt") String registeredAt, @Param("idLocadora") String idLocadora);

    @Query("SELECT c FROM Locacao c WHERE c.registeredAt = :registeredAt AND c.cliente.id = :idCliente")
    List<Locacao> findLocacaoClienteByDate(@Param("registeredAt") String registeredAt, @Param("idCliente") String idCliente);
}