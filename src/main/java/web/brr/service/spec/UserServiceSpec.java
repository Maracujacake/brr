package web.brr.service.spec;

import java.util.List;
import java.util.Optional;

import web.brr.domains.User;

public interface UserServiceSpec {
    User save(User user);

    void deleteById(Long id);

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByNome(String nome);
}
