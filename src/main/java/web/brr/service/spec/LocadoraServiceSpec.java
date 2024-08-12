package web.brr.service.spec;

import java.util.List;
import java.util.Optional;

import web.brr.domains.Locadora;

public interface LocadoraServiceSpec {

    Locadora save(Locadora locadora);

    void deleteById(Long id);

    List<Locadora> findAll();

    Optional<Locadora> findById(Long id);

    Optional<Locadora> findByEmail(String email);

    Optional<Locadora> findByCnpj(String cnpj);

    Optional<Locadora> findByCidade(String cidade);

    Optional<Locadora> findByEmailOrCnpj(String email, String cnpj);
}
