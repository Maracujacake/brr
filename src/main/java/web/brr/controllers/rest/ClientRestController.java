package web.brr.controllers.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(path = "/api/cliente")
    @ResponseBody
    public ResponseEntity<Cliente> cria(@RequestBody JSONObject json) {
        System.out.println("AQUI");
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
}
