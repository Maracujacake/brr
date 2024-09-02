package web.brr.controllers.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.brr.domains.Cliente;
import web.brr.service.impl.ClienteService;

@CrossOrigin
@RestController
public class ClientRestController {

    @Autowired
    private ClienteService service;

    private boolean isJSONValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    }

    private void parse(Cliente cli, JSONObject json) {

        Object id = json.get("id");
        if (id != null) {
            if (id instanceof Integer) {
                cli.setId(((Integer) id).longValue());
            } else {
                cli.setId((Long) id);
            }
        }

        cli.setNome((String) json.get("nome"));
        cli.setSenha((String) json.get("senha"));
        cli.setEmail((String) json.get("email"));
        cli.setRole((String) json.get("role"));

        cli.setTelefone((String) json.get("telefone"));
        cli.setSexo(((String) json.get("sexo")).charAt(0));
        cli.setCpf((String) json.get("cpf"));

        Object dataNascimento = json.get("dataNascimento");
        if (dataNascimento != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            cli.setDataNascimento(LocalDate.parse((String) dataNascimento, formatter));
        }
    }

    @PostMapping(path = "/clientes")
    @ResponseBody
    public ResponseEntity<Cliente> cria(@RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Cliente cliente = new Cliente();
                parse(cliente, json);
                service.save(cliente, false);
                return ResponseEntity.ok(cliente);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @GetMapping(path = "/clientes")
    @ResponseBody
    public ResponseEntity<Iterable<Cliente>> lista() {
        List<Cliente> clis = service.findAll();
        if (clis.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clis);
    }

    @GetMapping(path = "/clientes/{id}")
    @ResponseBody
    public ResponseEntity<Cliente> lista(@PathVariable("id") long id) {
        Cliente cli = service.findById(id).orElse(null);
        if (cli == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cli);
    }

    @PutMapping(path = "/clientes/{id}")
    @ResponseBody
    public ResponseEntity<Cliente> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Cliente cli = service.findById(id).orElse(null);
                if (cli == null) {
                    return ResponseEntity.notFound().build();
                }
                parse(cli, json);
                service.save(cli, true);
                return ResponseEntity.ok(cli);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @DeleteMapping(path = "/clientes/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

        Cliente cli = service.findById(id).orElse(null);
        if (cli == null) {
            return ResponseEntity.notFound().build();
        } else {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

}
