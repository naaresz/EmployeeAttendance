package com.rfid.employeeattendance.service;


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
import org.bson.types.ObjectId;
import com.mongodb.client.model.Filters;
import com.rfid.employeeattendance.dao.GenericDAO;
import com.rfid.employeeattendance.gui.DataAbsensiPage;
import com.rfid.employeeattendance.objects.LogAbsensi;

public class AbsensiService {

    private final GenericDAO<LogAbsensi> DAO;

    public AbsensiService() {
        this.DAO = new GenericDAO<>("absensi", LogAbsensi.class);
    }

    // =========================
    // TAMBAH ABSENSI
    // =========================
    public void tambahAbsensi(LogAbsensi logBaru) {
        DAO.save(logBaru);
    }

    // =========================
    // TAMPILKAN SEMUA
    // =========================
    public void tampilkanDaftarAbsensi() {

        List<LogAbsensi> daftar = DAO.findAll();

        System.out.println("=== DATA ABSENSI ===");

        for (LogAbsensi l : daftar) {
            System.out.println(l.toString());
        }
    }

    // =========================
    // TAMPILKAN KE PANEL
    // =========================
    public void tampilAbsensi(JPanel panelTarget, String key) {

        List<LogAbsensi> daftarAbsensi;

        if (key.isEmpty()) {
            daftarAbsensi = DAO.findAll();
        } else {
            daftarAbsensi = cariAbsensi(key);
        }

        panelTarget.removeAll();

        panelTarget.setLayout(new BorderLayout());
        panelTarget.setBackground(new Color(235, 227, 213));

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false);

        gridPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        for (LogAbsensi l : daftarAbsensi) {

            JPanel cardPanel = new JPanel(new GridLayout(8, 1, 0, 5));

            cardPanel.setBackground(new Color(243, 238, 234));

            cardPanel.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    new Color(119, 107, 93),
                                    1,
                                    true
                            ),
                            BorderFactory.createEmptyBorder(
                                    15,
                                    15,
                                    15,
                                    15
                            )
                    )
            );

            JLabel lblNama =
                    new JLabel("Nama : " + l.getNama());

            lblNama.setForeground(Color.BLACK);

            JLabel lblUid =
                    new JLabel("UID RFID : " + l.getUidRfid());

            lblUid.setForeground(Color.BLACK);

            JLabel lblTanggal =
                    new JLabel("Tanggal : " + l.getTanggal());

            lblTanggal.setForeground(Color.BLACK);

            JLabel lblWaktuMasuk =
                    new JLabel("Jam Masuk : " +
                            l.getWaktuMasuk().toLocalTime());
            
            JLabel lblWaktuKeluar =
                    new JLabel("Jam Keluar : " +
                            l.getWaktuKeluar().toLocalTime());

            JLabel lblStatus =
                    new JLabel("Status : " + l.getStatus());

            lblStatus.setForeground(Color.BLACK);

            JPanel controlPanel =
                    new JPanel(new GridLayout(1, 2, 10, 5));

            controlPanel.setBackground(
                    new Color(243, 238, 234)
            );

            // ================= EDIT =================
            JButton tombolEdit = new JButton("Edit");

            tombolEdit.setBackground(Color.ORANGE);
            tombolEdit.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

            tombolEdit.addActionListener((ActionEvent e) -> {

                DataAbsensiPage.selectedId = l.getIdLog();

                DataAbsensiPage.txtUid.setText(
                        l.getUidRfid()
                );

                DataAbsensiPage.txtNama.setText(
                        l.getNama()
                );

                DataAbsensiPage.txtTanggal.setText(
                        l.getTanggal()
                );

                DataAbsensiPage.txtJamMasuk.setText(
                        l.getWaktuMasuk().toLocalTime().toString()
                );

                DataAbsensiPage.txtJamKeluar.setText(
                        l.getWaktuKeluar().toLocalTime().toString()
                );

                DataAbsensiPage.txtStatus.setText(
                        l.getStatus()
                );

                DataAbsensiPage.btnSave.setEnabled(false);
                DataAbsensiPage.btnUpdate.setEnabled(true);

            });

            // ================= DELETE =================
            JButton tombolDelete = new JButton("Delete");

            tombolDelete.setBackground(Color.RED);
            tombolDelete.setForeground(Color.WHITE);

            tombolDelete.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

            tombolDelete.addActionListener((ActionEvent e) -> {

                int confirm =
                        JOptionPane.showConfirmDialog(
                                null,
                                "Yakin ingin menghapus data absensi "
                                + l.getNama() + " ?",
                                "Konfirmasi",
                                JOptionPane.YES_NO_OPTION
                        );

                if (confirm == JOptionPane.YES_OPTION) {

                    hapusAbsensi(
                            l.getIdLog().toHexString()
                    );
                }

            });

            controlPanel.add(tombolEdit);
            controlPanel.add(tombolDelete);

            cardPanel.add(lblNama);
            cardPanel.add(lblUid);
            cardPanel.add(lblTanggal);
            cardPanel.add(lblWaktuMasuk);
            cardPanel.add(lblWaktuKeluar);
            cardPanel.add(lblStatus);
            cardPanel.add(controlPanel);

            gridPanel.add(cardPanel);
        }

        panelTarget.add(gridPanel, BorderLayout.NORTH);

        panelTarget.revalidate();
        panelTarget.repaint();
    }

    // =========================
    // CARI ABSENSI
    // =========================
    public List<LogAbsensi> cariAbsensi(String key) {

        List<Bson> filters = new ArrayList<>();

        for (Field field : LogAbsensi.class.getDeclaredFields()) {

            filters.add(
                    Filters.regex(
                            field.getName(),
                            key,
                            "i"
                    )
            );
        }

        return DAO.findMany(Filters.or(filters));
    }

    // =========================
    // UPDATE
    // =========================
    public void updateAbsensi(LogAbsensi newLog) {

        Bson filter =
                Filters.eq("_id", newLog.getIdLog());

        LogAbsensi log = DAO.findOne(filter);

        if (log != null) {

            DAO.update(filter, newLog);

            DataAbsensiPage.showData("");

            JOptionPane.showMessageDialog(
                    null,
                    "Data absensi berhasil diperbarui!"
            );
        }
    }

    // =========================
    // DELETE
    // =========================
    public void hapusAbsensi(String idLog) {

        Bson filter =
                Filters.eq(
                        "_id",
                        new ObjectId(idLog)
                );

        DAO.delete(filter);

        DataAbsensiPage.showData("");

        JOptionPane.showMessageDialog(
                null,
                "Data absensi berhasil dihapus."
        );
    }
    
    
}