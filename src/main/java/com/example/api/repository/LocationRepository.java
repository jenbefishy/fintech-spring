package com.example.api.repository;
import com.example.api.model.LocationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<LocationModel, Long> {
    @Query("SELECT l FROM LocationModel l JOIN FETCH l.events WHERE l.id = :id")
    LocationModel findByIdWithEvents(@Param("id") Long id);
}