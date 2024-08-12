package web.brr.service.spec;

import java.util.List;
import java.util.Optional;

import web.brr.domains.Locacao;

public interface LocacaoServiceSpec {

    Locacao save(Locacao Locacao);

    void deleteById(Long id);

    List<Locacao> findAll();

    Optional<Locacao> findById(Long id);

    List<Locacao> findClienteRegistrations(String id);

    List<Locacao> findLocadoraRegistrations(String id);

    List<Locacao> findClienteAndLocadoraRegistrations(String idCliente,
            String idLocadora);

    List<Locacao> findLocacaoLocadoraByDate(String registeredAt,
            String idLocadora);

    List<Locacao> findLocacaoClienteByDate(String registeredAt,
            String idCliente);
}
