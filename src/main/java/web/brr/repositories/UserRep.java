package web.brr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.brr.domains.User;

@Repository
public interface UserRep extends JpaRepository<User, Long> {
    @SuppressWarnings("unchecked")
    User save(User user);

    void deleteById(Long id);

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByNome(String nome);

}
