package com.example.projectgachihaja.zone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ZoneRepository extends JpaRepository<Zone,Long> {
    Zone findByCity(String city);
}
