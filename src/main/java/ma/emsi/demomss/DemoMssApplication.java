package ma.emsi.demomss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@SpringBootApplication
public class DemoMssApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoMssApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProduitRepository produitRepository) {
        return args -> {
            produitRepository.save(new Produit(null, "Ord HP 54", 60000, 3));
            produitRepository.save(new Produit(null, "Imprimante Epson", 2000, 13));
            produitRepository.save(new Produit(null, "Smart Phone", 300, 23));
            produitRepository.findAll().forEach(produit -> {
                System.out.println(produit.getName());
            });
        };
    }
}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private double quantity;

}
@RepositoryRestResource
interface ProduitRepository extends JpaRepository<Produit, Long> {
    @RestResource(path = "/byName")
    Page<Produit> findByNameContains(@Param("kw") String name, Pageable pageable);
}

@Projection(name = "mobile", types = Produit.class)
interface ProduitProjection{
    String getName();
}

@Projection(name = "web", types = Produit.class)
interface ProduitProjection2{
    String getName();
    double getprice();
}