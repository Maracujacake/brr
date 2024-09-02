package web.brr.controllers.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;
import web.brr.domains.Locadora;
import web.brr.service.impl.LocacaoService;
import web.brr.service.impl.AdminService;


@CrossOrigin
@RestController
public class LocacaoRestController {
    
    @Autowired
    private LocacaoService service;
    private AdminService adminService;

    // verificacao da requisicao JSON
    private boolean isJSONValid(String jsonInString) {
            try {
                return new ObjectMapper().readTree(jsonInString) != null;
            } catch (IOException e) {
                return false;
            }
    }


    // Lista todas as locações do sistema
    @GetMapping(path = "/locacoes")
    @ResponseBody
    public ResponseEntity<Iterable<Locacao>> listaTodas(){
        List<Locacao> locacoes = service.findAll(); // todas as locações em uma lista
        if(locacoes.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locacoes);
    }


    // Locações com determinado ID 
    @GetMapping(path = "/locacoes/{id}")
    @ResponseBody
    public ResponseEntity<Optional<Locacao>> listaPorId(@PathVariable("id") long id){  
        Optional<Locacao> loc = service.findById(id);
        if(loc == null){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(loc);
        }
    }


    // Locações de determinado USUARIO
    @GetMapping(path = "/locacoes/clientes/{id}")
    @ResponseBody
    public ResponseEntity<List<Locacao>> listaPorIdCliente(@PathVariable("id") String id){  
        List<Locacao> locacoes = service.findClienteRegistrations(id);
        if(locacoes.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(locacoes);
        }
    }

    // Locações de determinada LOCADORA
    @GetMapping(path = "/locacoes/locadoras/{id}")
    @ResponseBody
    public ResponseEntity<List<Locacao>> listaPorIdLocadora(@PathVariable("id") String id){  
        List<Locacao> locacoes = service.findLocadoraRegistrations(id);
        if(locacoes.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(locacoes);
        }
    }



}
