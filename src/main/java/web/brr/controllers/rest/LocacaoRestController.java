package web.brr.controllers.rest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.brr.domains.Cliente;
import web.brr.domains.Locacao;
import web.brr.domains.Locadora;
import web.brr.service.impl.ClienteService;
import web.brr.service.impl.LocacaoService;
import web.brr.service.impl.LocadoraService;
import web.brr.service.impl.ObjectValidatorService;


@CrossOrigin
@RestController
public class LocacaoRestController {
    
    @Autowired
    private LocacaoService service;
    @Autowired
    private ClienteService cliService;
    @Autowired
    private LocadoraService locService;

    private ObjectValidatorService objectValidatorService = new ObjectValidatorService();


    // verificacao da requisicao JSON
    private boolean isJSONValid(String jsonInString) {
            try {
                return new ObjectMapper().readTree(jsonInString) != null;
            } catch (IOException e) {
                return false;
            }
    }


    private List<String> parse(Locacao locacao, JSONObject json) {

        Object idC = json.get("idCliente");
        Object idL = json.get("idLocadora");
        Object data = json.get("data");

        if (idC != null) {
            Long idCliente = (idC instanceof Long) ? (Long) idC : Long.parseLong(idC.toString());
            Optional<Cliente> cliente = cliService.findById(idCliente);
            if (cliente.isPresent()) {
                locacao.setCliente(cliente.get());
            }
            else{
                return List.of("Cliente não encontrado para o id " + idCliente);
            }
        }

        if (idL != null) {
            Long idLocadora = (idL instanceof Long) ? (Long) idL : Long.parseLong(idL.toString());
            Optional<Locadora> locadora = locService.findById(idLocadora);
            if (locadora.isPresent()) {
                locacao.setLocadora(locadora.get());
            } else {
                return List.of("Locadora não encontrada para o id " + idLocadora);
            }
        }

        if (data != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(data.toString(), formatter);
                locacao.setRegisteredAt(dateTime);
            } 
            catch (DateTimeParseException e) {
                return List.of("Formato de data inválido: " + data);
            }
        }


        List<String> errors = objectValidatorService.validate(locacao);
        return errors;
    }

    private List<String> parseUpdate(Locacao locacao, JSONObject json) {
        Object data = json.get("data");
        if (data != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(data.toString(), formatter);
                locacao.setRegisteredAt(dateTime);
            } 
            catch (DateTimeParseException e) {
                return List.of("Formato de data inválido: " + data);
            }
        }
        List<String> errors = objectValidatorService.validate(locacao);
        return errors;
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


    // cria locacao (corpo do JSON: idCliente, idLocadora, data)
    @PostMapping(path = "/locacoes")
    @ResponseBody
    public ResponseEntity<?> cria(@RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Locacao locacao = new Locacao();
                List<String> errors = parse(locacao, json);
                if(!errors.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors.toString());
                }
                List<Locacao> locacoes = service.findLocacaoClienteByDate(locacao.getRegisteredAt(),
                        locacao.getCliente().getId().toString());
                locacoes.addAll(service.findLocacaoLocadoraByDate(locacao.getRegisteredAt(),
                        locacao.getLocadora().getId().toString()));
                if (!locacoes.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe uma locação para essa data");
                }
                service.save(locacao);
                return ResponseEntity.ok(locacao);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }



    // atualiza locacao (corpo do JSON: data)
    @PutMapping(path = "/locacoes/{id}")
    @ResponseBody
    public ResponseEntity<?> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Locacao locacao = service.findById(id).orElse(null);
                if (locacao == null) {
                    return ResponseEntity.notFound().build();
                }
                List<String> errors = parseUpdate(locacao, json);
                if(!errors.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors.toString());
                }

                List<Locacao> locacoes = service.findLocacaoClienteByDate(locacao.getRegisteredAt(),
                        locacao.getCliente().getId().toString());
                locacoes.addAll(service.findLocacaoLocadoraByDate(locacao.getRegisteredAt(),
                        locacao.getLocadora().getId().toString()));
                if (!locacoes.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe uma locação para essa data");
                }
                service.save(locacao);
                return ResponseEntity.ok(locacao);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }



}
