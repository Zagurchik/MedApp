package org.example.medapp.med;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedRepository extends JpaRepository<Med, String> {
    List<Med> findByNameContainingIgnoreCaseOrderByNameAsc(String q);
}