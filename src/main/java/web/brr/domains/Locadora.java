package web.brr.domains;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Locadora")
@DynamicUpdate
public class Locadora extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, unique = true, length = 100)
    private String cnpj;

    @OneToMany(mappedBy = "locadora",cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Locacao> registrations = new HashSet<>();

    public Set<Locacao> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<Locacao> registrations) {
        this.registrations.clear();
        if (registrations != null) {
            this.registrations.addAll(registrations);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
