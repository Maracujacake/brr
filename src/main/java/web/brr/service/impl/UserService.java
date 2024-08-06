package web.brr.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.brr.domains.User;
import web.brr.repositories.UserRep;
import web.brr.service.spec.UserServiceSpec;

@Service
public class UserService implements UserServiceSpec {
    @Autowired
    UserRep dao;

    public User save(User user) {
        return dao.save(user);
    }

    public void deleteById(Long id) {
        dao.deleteById(id);
    }

    public List<User> findAll() {
        return dao.findAll();
    }

    public Optional<User> findById(Long id) {
        return dao.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return dao.findByEmail(email);
    }

    public Optional<User> findByNome(String nome) {
        return dao.findByNome(nome);
    }
}
