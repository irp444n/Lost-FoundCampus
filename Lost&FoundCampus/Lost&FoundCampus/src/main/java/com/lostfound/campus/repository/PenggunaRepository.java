package com.lostfound.campus.repository;

import com.lostfound.campus.model.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenggunaRepository extends JpaRepository<Pengguna, Long> {
    boolean existsByEmailIgnoreCase(String email);
}