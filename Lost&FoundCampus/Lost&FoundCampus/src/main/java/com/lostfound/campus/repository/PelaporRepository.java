package com.lostfound.campus.repository;

import com.lostfound.campus.model.Pelapor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PelaporRepository extends JpaRepository<Pelapor, Long> {
    boolean existsByNomorInduk(String nomorInduk);
}