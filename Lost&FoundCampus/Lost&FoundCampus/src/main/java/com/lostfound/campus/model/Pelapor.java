package com.lostfound.campus.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pelapor")
public class Pelapor {

    @Id
    @Column(name = "id_pengguna")
    private Long idPengguna;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_pengguna")
    private Pengguna pengguna;

    @Column(name = "tipe_pelapor", nullable = false, length = 20)
    private String tipePelapor;

    @Column(name = "nomor_induk", nullable = false, unique = true, length = 30)
    private String nomorInduk;

    @Column(name = "unit_asal", nullable = false, length = 150)
    private String unitAsal;

    protected Pelapor() {
    }

    public Pelapor(Pengguna pengguna, String tipePelapor, String nomorInduk, String unitAsal) {
        this.pengguna = pengguna;
        this.tipePelapor = tipePelapor;
        this.nomorInduk = nomorInduk;
        this.unitAsal = unitAsal;
    }
}