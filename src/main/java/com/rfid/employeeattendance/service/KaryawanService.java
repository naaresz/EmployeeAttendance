package com.rfid.employeeattendance.service;

import com.rfid.employeeattendance.dao.GenericDAO;
import com.mongodb.client.model.Filters;
import com.rfid.employeeattendance.gui.DataKaryawanPage;
import com.rfid.employeeattendance.objects.Karyawan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.bson.conversions.Bson;

public class KaryawanService {

    private final GenericDAO<Karyawan> DAO;

    public KaryawanService() {
        this.DAO = new GenericDAO<>("karyawan", Karyawan.class);
    }

    // =========================
    // TAMBAH KARYAWAN
    // =========================
    public void tambahKaryawan(Karyawan karyawanBaru) {
        DAO.save(karyawanBaru);
    }

    public void tambahKaryawan(
            String uidRfid,
            String namaLengkap,
            String email,
            String noHp,
            String jabatan,
            String alamat
    ) {

        Karyawan karyawanBaru = new Karyawan(
                uidRfid,
                namaLengkap,
                email,
                noHp,
                jabatan,
                alamat
        );

        DAO.save(karyawanBaru);
    }

    // =========================
    // TAMPILKAN SEMUA
    // =========================
    public void tampilkanDaftarKaryawan() {

        List<Karyawan> daftar = DAO.findAll();

        System.out.println("=== DAFTAR KARYAWAN ===");

        for (Karyawan k : daftar) {
            System.out.println(k.toString());
        }
    }

    // =========================
    // TAMPILKAN KE PANEL
    // =========================
    public void tampilKaryawan(JPanel panelTarget, String key) {

        List<Karyawan> daftarKaryawan;

        if (key.isEmpty()) {
            daftarKaryawan = DAO.findAll();
        } else {
            daftarKaryawan = cariKaryawan(key);
        }

        panelTarget.removeAll();

        panelTarget.setLayout(new BorderLayout());
        panelTarget.setBackground(new Color(235, 227, 213));

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false);

        gridPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        for (Karyawan k : daftarKaryawan) {

            JPanel cardPanel = new JPanel(new GridLayout(5, 1, 0, 5));

            cardPanel.setBackground(new Color(243, 238, 234));

            cardPanel.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder((new Color(119, 107, 93)), 1, true),
                            BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    )
            );

            JLabel lblNama = new JLabel("Nama : " + k.getNamaLengkap());
            lblNama.setForeground(Color.BLACK);

            JLabel lblUid = new JLabel("UID RFID : " + k.getUidRfid());
            lblUid.setForeground(Color.BLACK);

            JLabel lblEmail = new JLabel("Email : " + k.getEmail());
            lblEmail.setForeground(Color.BLACK);

            JLabel lblJabatan = new JLabel("Jabatan : " + k.getJabatan());
            lblJabatan.setForeground(Color.BLACK);

            JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 5));
            controlPanel.setBackground(new Color(243, 238, 234));

            // ================= EDIT =================
            JButton tombolEdit = new JButton("Edit");

            tombolEdit.setBackground(Color.ORANGE);
            tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));

            tombolEdit.addActionListener((ActionEvent e) -> {

                DataKaryawanPage.txtUid.setText(k.getUidRfid());
                DataKaryawanPage.txtNama.setText(k.getNamaLengkap());
                DataKaryawanPage.txtEmail.setText(k.getEmail());
                DataKaryawanPage.txtNoHp.setText(k.getNoHp());
                DataKaryawanPage.txtJabatan.setText(k.getJabatan());
                DataKaryawanPage.txtAlamat.setText(k.getAlamat());
                DataKaryawanPage.btnSave.setEnabled(false);
                DataKaryawanPage.btnUpdate.setEnabled(true);

            });

            // ================= DELETE =================
            JButton tombolDelete = new JButton("Delete");

            tombolDelete.setBackground(Color.RED);
            tombolDelete.setForeground(Color.WHITE);
            tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));

            tombolDelete.addActionListener((ActionEvent e) -> {

                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Yakin ingin menghapus data "
                        + k.getNamaLengkap() + " ?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    hapusKaryawan(k.getUidRfid());
                }

            });

            controlPanel.add(tombolEdit);
            controlPanel.add(tombolDelete);

            cardPanel.add(lblNama);
            cardPanel.add(lblUid);
            cardPanel.add(lblEmail);
            cardPanel.add(lblJabatan);
            cardPanel.add(controlPanel);

            gridPanel.add(cardPanel);
        }

        panelTarget.add(gridPanel, BorderLayout.NORTH);

        panelTarget.revalidate();
        panelTarget.repaint();
    }

    // =========================
    // CARI KARYAWAN
    // =========================
    public List<Karyawan> cariKaryawan(String key) {
        List<Bson> filters = new ArrayList<>();
        for (Field field : Karyawan.class.getDeclaredFields()) {
            filters.add(
                    Filters.regex(field.getName(), key, "i")
            );
        }
        return DAO.findMany(Filters.or(filters));
    }

    // =========================
    // UPDATE
    // =========================
    public void updateKaryawan(Karyawan newK) {
        Bson filter = Filters.eq("uidRfid", newK.getUidRfid());
        Karyawan k = DAO.findOne(filter);
        if (k != null) {
            DAO.update(filter, newK);
            DataKaryawanPage.showData("");
            JOptionPane.showMessageDialog(
                    null,
                    "Data berhasil diperbarui!"
            );
        }
    }

    // =========================
    // DELETE
    // =========================
    public void hapusKaryawan(String uidRfid) {
        Bson filter = Filters.eq("uidRfid", uidRfid);
        DAO.delete(filter);
        DataKaryawanPage.showData("");
        JOptionPane.showMessageDialog(
                null,
                "Data karyawan berhasil dihapus."
        );
    }
}
