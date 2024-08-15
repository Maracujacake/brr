package web.brr.repositories;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import web.brr.domains.Admin;
import web.brr.domains.User;

@Repository
public interface AdminRep extends JpaRepository<User, Long> {

    @SuppressWarnings("unchecked")
    Admin save(Admin admin);

    void deleteById(Long id);

    Optional<Admin> findByEmail(String email);

}
