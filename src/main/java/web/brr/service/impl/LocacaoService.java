package web.brr.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.brr.domains.Locacao;
import web.brr.repositories.LocacaoRep;
import web.brr.service.spec.LocacaoServiceSpec;

@Service
public class LocacaoService implements LocacaoServiceSpec{

    @Autowired
    private LocacaoRep locacaoRep;


    public Locacao save(Locacao Locacao){
       return  locacaoRep.save(Locacao);
    }

    public void deleteById(Long id){
        locacaoRep.deleteById(id);
    }

    public List<Locacao> findAll(){
        return locacaoRep.findAll();
    }

    public Optional<Locacao> findById(Long id){
        return locacaoRep.findById(id);
    }

    public List<Locacao> findClienteRegistrations(String id){
        return locacaoRep.findClienteRegistrations(id);
    }

    public List<Locacao> findLocadoraRegistrations(String id){
        return locacaoRep.findLocadoraRegistrations(id);
    }

    public List<Locacao> findClienteAndLocadoraRegistrations(String idCliente,
            String idLocadora){
                return locacaoRep.findClienteAndLocadoraRegistrations(idCliente, idLocadora);
            }

    public List<Locacao> findLocacaoLocadoraByDate(String registeredAt,
            String idLocadora){
                return locacaoRep.findLocacaoLocadoraByDate(registeredAt, idLocadora);
            }

    public List<Locacao> findLocacaoClienteByDate(String registeredAt,
            String idCliente){

                return locacaoRep.findLocacaoClienteByDate(registeredAt, idCliente);
    }
}
