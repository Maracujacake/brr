package web.brr.service.spec;

import java.util.List;
import java.util.Optional;

import web.brr.domains.Locacao;
import web.brr.domains.Locadora;

public interface LocadoraServiceSpec {

    Locadora save(Locadora locadora, Boolean update);

    void deleteById(Long id);

    List<Locadora> findAll();

    Optional<Locadora> findById(Long id);

    Optional<Locadora> findByEmail(String email);

    Optional<Locadora> findByCnpj(String cnpj);

    List<Locadora> findByCidade(String cidade);

    List<Locadora> findByEmailOrCnpj(String email, String cnpj);

    List<Locadora> findByNomeContaining(String nome);

    List<Locacao> findRegistrations(String id);

}
