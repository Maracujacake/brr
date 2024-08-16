package web.brr.repositories;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import web.brr.domains.Admin;

@Repository
public interface AdminRep extends JpaRepository<Admin, Long> {

    @SuppressWarnings("unchecked")
    Admin save(Admin admin);

    void deleteById(Long id);

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findById(Long id);

}
