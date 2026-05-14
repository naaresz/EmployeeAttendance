package com.rfid.employeeattendance.service;

import com.mongodb.client.model.Filters;
import com.rfid.employeeattendance.dao.GenericDAO;
import com.rfid.employeeattendance.objects.Karyawan;
import com.rfid.employeeattendance.objects.LogAbsensi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DashboardService {
    public long getJumlahKaryawan() {
        GenericDAO<Karyawan> dao =
                new GenericDAO<>("karyawan", Karyawan.class);

        return dao.findAll().size();
    }
    
    public long getJumlahHadir() {
        GenericDAO<LogAbsensi> dao =
                new GenericDAO<>("absensi", LogAbsensi.class);

        return dao.findMany(
                Filters.eq("status", "Hadir")
        ).size();
    }
    
    public long getJumlahTidakHadir() {

        GenericDAO<Karyawan> karyawanDAO =
                new GenericDAO<>("karyawan", Karyawan.class);

        GenericDAO<LogAbsensi> absensiDAO =
                new GenericDAO<>("absensi", LogAbsensi.class);

        long jumlahKaryawan =
                karyawanDAO.findAll().size();

        long jumlahHadir =
                absensiDAO.findMany(
                        Filters.eq("status", "Hadir")
                ).size();

        return jumlahKaryawan - jumlahHadir;
    }
    
    public void tampilAbsensiTerbaru(JPanel panelTarget) {

        GenericDAO<LogAbsensi> dao =
                new GenericDAO<>("absensi", LogAbsensi.class);

        List<LogAbsensi> daftar = dao.findAll();

        panelTarget.removeAll();

        panelTarget.setLayout(new BorderLayout());

        JPanel container =
                new JPanel(new GridLayout(0, 3, 10, 10));

        container.setOpaque(false);

        int jumlah = Math.min(5, daftar.size());

        for (int i = daftar.size() - 1;
             i >= daftar.size() - jumlah;
             i--) {

            LogAbsensi l = daftar.get(i);

            JPanel card = new JPanel(
                    new GridLayout(4, 1)
            );

            card.setBackground(
                    new Color(243, 238, 234)
            );

            card.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    new Color(119,107,93),
                                    1,
                                    true
                            ),
                            BorderFactory.createEmptyBorder(
                                    10,
                                    10,
                                    10,
                                    10
                            )
                    )
            );

            JLabel nama =
                    new JLabel("Nama : " + l.getNama());

            JLabel tanggal =
                    new JLabel("Tanggal : " + l.getTanggal());

            JLabel masuk =
                    new JLabel(
                            "Masuk : " +
                            l.getWaktuMasuk()
                             .toLocalTime()
                    );

            JLabel status =
                    new JLabel(
                            "Status : " +
                            l.getStatus()
                    );

            card.add(nama);
            card.add(tanggal);
            card.add(masuk);
            card.add(status);

            container.add(card);
        }

        panelTarget.add(container, BorderLayout.CENTER);

        panelTarget.revalidate();
        panelTarget.repaint();
    }
}
