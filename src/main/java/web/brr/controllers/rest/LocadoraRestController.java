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

import web.brr.domains.Locadora;
import web.brr.service.impl.LocadoraService;

@CrossOrigin
@RestController
public class LocadoraRestController {

    @Autowired
    private LocadoraService service;

    private boolean isJSONValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    }

    private void parse(Locadora loc, JSONObject json) {

        Object id = json.get("id");
        if (id != null) {
            if (id instanceof Integer) {
                loc.setId(((Integer) id).longValue());
            } else {
                loc.setId((Long) id);
            }
        }

        loc.setNome((String) json.get("nome"));
        loc.setSenha((String) json.get("senha"));
        loc.setEmail((String) json.get("email"));
        loc.setRole((String) json.get("role"));

        loc.setCidade((String) json.get("cidade"));
        loc.setCnpj((String) json.get("cnpj"));
    }

    @PostMapping(path = "/locadoras")
    @ResponseBody
    public ResponseEntity<Locadora> cria(@RequestBody JSONObject json) {
        System.out.println("AQUI");
        try {
            if (isJSONValid(json.toString())) {
                Locadora locadora = new Locadora();
                parse(locadora, json);
                service.save(locadora, false);
                return ResponseEntity.ok(locadora);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }
    @GetMapping(path = "/locadoras")
    @ResponseBody
    public ResponseEntity<Iterable<Locadora>> lista() {
        List<Locadora> locs = service.findAll();
        if (locs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locs);
    }

    @GetMapping(path = "/locadoras/{id}")
    @ResponseBody
    public ResponseEntity<Locadora> lista(@PathVariable("id") long id) {
        Locadora loc = service.findById(id).orElse(null);
        if (loc == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loc);
    }

    @PutMapping(path = "/locadoras/{id}")
    @ResponseBody
    public ResponseEntity<Locadora> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Locadora loc = service.findById(id).orElse(null);
                if (loc == null) {
                    return ResponseEntity.notFound().build();
                }
                parse(loc, json);
                service.save(loc, true);
                return ResponseEntity.ok(loc);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @DeleteMapping(path = "/locadoras/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

        Locadora loc = service.findById(id).orElse(null);
        if (loc == null) {
            return ResponseEntity.notFound().build();
        } else {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

}

