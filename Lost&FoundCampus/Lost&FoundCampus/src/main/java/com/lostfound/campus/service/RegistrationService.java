package com.lostfound.campus.service;

import com.lostfound.campus.model.Pelapor;
import com.lostfound.campus.model.Pengguna;
import com.lostfound.campus.repository.PelaporRepository;
import com.lostfound.campus.repository.PenggunaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final PenggunaRepository penggunaRepository;
    private final PelaporRepository pelaporRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(PenggunaRepository penggunaRepository, PelaporRepository pelaporRepository,
                               PasswordEncoder passwordEncoder) {
        this.penggunaRepository = penggunaRepository;
        this.pelaporRepository = pelaporRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(String nama, String email, String noWhatsapp, String tipePelapor,
                         String nomorInduk, String unitAsal, String password) {
        String normalizedEmail = email.trim().toLowerCase();
        String normalizedNomorInduk = nomorInduk.trim();
        if (penggunaRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("Email sudah terdaftar.");
        }
        if (pelaporRepository.existsByNomorInduk(normalizedNomorInduk)) {
            throw new IllegalArgumentException("Nomor induk sudah terdaftar.");
        }

        Pengguna pengguna = penggunaRepository.save(new Pengguna(nama.trim(), normalizedEmail,
                passwordEncoder.encode(password), noWhatsapp));
        pelaporRepository.save(new Pelapor(pengguna, tipePelapor, normalizedNomorInduk, unitAsal.trim()));
    }
}