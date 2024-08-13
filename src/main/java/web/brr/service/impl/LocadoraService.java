package web.brr.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.brr.domains.Locacao;
import web.brr.domains.Locadora;
import web.brr.encrypt.EncryptPassword;
import web.brr.repositories.LocadoraRep;
import web.brr.service.spec.LocadoraServiceSpec;

@Service
public class LocadoraService implements LocadoraServiceSpec {
    @Autowired
    private LocadoraRep locadoraRep;

    public Locadora save(Locadora locadora, Boolean update) {
        if(update) return locadoraRep.save(locadora);
        Optional<web.brr.domains.Locadora> nome = locadoraRep.findByEmailOrCnpj(locadora.getEmail(),
                locadora.getCnpj());
        if (nome.isPresent()) {
            return null;
        }

        locadora.setSenha(EncryptPassword.encrypt(locadora.getSenha()));
        locadora.setRole("ROLE_LOCADORA");
        return locadoraRep.save(locadora);
    }

    public void deleteById(Long id) {
        locadoraRep.deleteById(id);
    }

    public List<Locadora> findByNomeContaining(String nome){
        return locadoraRep.findByNomeContaining(nome);
    }


    public List<Locadora> findAll() {
        return locadoraRep.findAll();
    }

    public Optional<Locadora> findById(Long id) {
        return locadoraRep.findById(id);
    }

    public Optional<Locadora> findByEmail(String email) {
        return locadoraRep.findByEmail(email);
    }

    public Optional<Locadora> findByCnpj(String cnpj) {
        return locadoraRep.findByCnpj(cnpj);
    }

    public List<Locadora> findByCidade(String cidade) {
        return locadoraRep.findByCidade(cidade);
    }

    public Optional<Locadora> findByEmailOrCnpj(String email, String cnpj) {
        return locadoraRep.findByEmailOrCnpj(email, cnpj);
    }

    public List<Locacao> findRegistrations(String id){
        return locadoraRep.findRegistrations(id);
    }
}
