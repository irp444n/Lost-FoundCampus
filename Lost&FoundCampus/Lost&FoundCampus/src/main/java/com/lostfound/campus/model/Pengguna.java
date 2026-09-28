package com.lostfound.campus.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pengguna")
public class Pengguna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pengguna")
    private Long idPengguna;

    @Column(nullable = false, length = 100)
    private String nama;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "no_whatsapp", nullable = false, length = 20)
    private String noWhatsapp;

    @Column(nullable = false, length = 20)
    private String role = "pelapor";

    @Column(name = "status_akun", nullable = false, length = 20)
    private String statusAkun = "aktif";

    protected Pengguna() {
    }

    public Pengguna(String nama, String email, String passwordHash, String noWhatsapp) {
        this.nama = nama;
        this.email = email;
        this.passwordHash = passwordHash;
        this.noWhatsapp = noWhatsapp;
    }
}