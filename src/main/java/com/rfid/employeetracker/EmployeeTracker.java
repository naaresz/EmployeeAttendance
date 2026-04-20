package com.rfid.employeetracker;

import com.rfid.employeetracker.ui.DashboardFrame;
import com.rfid.employeetracker.objects.Karyawan;
import com.rfid.employeetracker.objects.LogAbsensi;
import DataAccessObject.DataAccessObject;
import database.MongoConnection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.rfid.employeetracker.ui.DashboardFrame;

import java.time.LocalDateTime;
import java.util.HashMap;

public class EmployeeTracker {

    public static void main(String[] args) {
        
        MongoDatabase db = MongoConnection.getDatabase();
        MongoCollection<Document> karyawanCollection = db.getCollection("karyawan");
        
        long totalKaryawan = karyawanCollection.countDocuments();
        System.out.println("Total Karyawan:" + totalKaryawan);
        
        MongoCollection<Document> logCollection = db.getCollection("log_absensi");
        
        // logCollection.deleteMany(new Document());
        
        //DataAccessObject
        DataAccessObject<Karyawan> karyawanDAO = new DataAccessObject<>();
        DataAccessObject<LogAbsensi> logDAO = new DataAccessObject<>();
        
        // Data Karyawan
        Karyawan k = new Karyawan ();
        k.setUidRfid("12345");
        k.setNamaLengkap("Nareswari");
        k.setEmail("nareswari@gmail.com");
        k.setNoHp("08816776347");
        k.setJabatan("Waiters");
        k.setAlamat("Brebes");
        
        karyawanDAO.add(k);
        karyawanCollection.deleteMany(new Document());
        
        Document doc = new Document("uidRfid", k.getUidRfid())
                .append("nama", k.getNamaLengkap())
                .append("email", k.getEmail())
                .append("noHp", k.getNoHp())
                .append("jabatan", k.getJabatan())
                .append("alamat", k.getAlamat());
        
        karyawanCollection.insertOne(doc);
        
        System.out.println("Data masuk ke MongoDB");
        
        // Hashmap RFID
        HashMap<String, Karyawan> rfidMap = new HashMap<>();
        
        rfidMap.put(k.getUidRfid(), k);
        
        // Simulasi Scan RFID
        String scanRFID = "12345";
        
        Karyawan hasil = rfidMap.get(scanRFID);
        
        if (hasil != null){
            System.out.println(" HASIL SCAN ");
            System.out.println("Nama:" + hasil.getNamaLengkap());
            
            prosesScan(scanRFID);
            
            System.out.println("Log absensi masuk ke MongoDB");
            
        } else {
            System.out.println("RFID tidak ditemukan!");
            System.out.println("Data tidak ditemukan");
        }
        
        // Tampilkan semua log
        System.out.println(" DATA ABSENSI ");
        
        //for (LogAbsensi l : logDAO.getAll()){
            
            //Karyawan karyawan = rfidMap.get(l.getUidRfid());
            //System.out.println("Nama: " + karyawan.getNamaLengkap());
            //System.out.println("RFID: " + l.getUidRfid());
            //System.out.println("Waktu: " + l.getWaktuMasuk());
           // System.out.println("Status: " + l.getStatus());
        // }
        
        System.out.println(" Data Karyawan ");
        
        for (Karyawan karyawan : karyawanDAO.getAll()){
             System.out.println("Nama: " + karyawan.getNamaLengkap());
             System.out.println("RFID: " + karyawan.getUidRfid());
             System.out.println();
        }
        
        System.out.println(" DASHBOARD ");
        
        System.out.println("Total Karyawan: " + karyawanDAO.getAll().size());
        // System.out.println("Total Kehadiran: " + logDAO.getAll().size());
        
        System.out.println("AMBIL 1 DATA DARI MONGODB");
        
        for (Document docMongo : karyawanCollection.find()){
            System.out.println("Nama:" + docMongo.getString("nama"));
            System.out.println("RFID:" + docMongo.getString("uidRfid"));
            System.out.println("-------------");
        }
        
        System.out.println("Data LOG ABSENSI DARI MONGODB");
        
        for (Document logDoc : logCollection.find()){
            String tanggal = logDoc.getString("tanggal");
            
            String masukRaw = logDoc.getString("waktuMasuk");
            String keluarRaw = logDoc.getString("waktuKeluar");
            
            // format tanggal
            String tanggalFormat = tanggal != null
                    ? tanggal.substring(8,10) + "-" + tanggal.substring(5,7) + "-" + tanggal.substring(0,4)
                    : "-";
            
            // format jam
            String jamMasuk = masukRaw != null
                    ? masukRaw.split("T")[1].substring(0,5)
                    : "-";
            
            String jamKeluar = keluarRaw != null
                    ? keluarRaw.split("T")[1].substring(0,5)
                    :"-";
            
            System.out.println("RFID" + logDoc.getString("uidRfid"));
            System.out.println("Tanggal:" + tanggalFormat);
            System.out.println("Jam Masuk:" + jamMasuk);
            System.out.println("Jam Keluar:" + jamKeluar);
            System.out.println("Status:" + logDoc.getString("status"));
            System.out.println("--------------------");
        }
        
        // System.out.println("HAPUS DATA KARYAWAN");
        
        // karyawanCollection.deleteMany(new Document("uidRfid", "12345"));
        
        // System.out.println("Data Karyawan dengan RFID 12345 berhasil dihapus");
        
        System.out.println("UPDATE DATA KARYAWAN");
        
        Document filter = new Document("uidRfid", "12345");
        
        Document update = new Document("$set",
                new Document("nama", "Andira"));
        
        karyawanCollection.updateOne(filter, update);
        
        System.out.println("Data berhasil diupdate");
    }
    
    public static void prosesScan(String uid){
        MongoDatabase db = MongoConnection.getDatabase();
        MongoCollection<Document> logCollection = db.getCollection("log_absensi");
        
        String today = java.time.LocalDate.now().toString();
        
        // untuk mencari log hari ini yang belum keluar
        Document logHariIni = logCollection.find(
                new Document("uidRfid", uid)
                .append("tanggal", today)
                .append("waktuKeluar",new Document("$eq", null))
        ).first();
        
        if (logHariIni == null){
            // masuk
            Document newLog = new Document("uidRfid", uid)
                    .append("tanggal", today)
                    .append("waktuMasuk", java.time.LocalDateTime.now().toString())
                    .append("waktuKeluar", null)
                    .append("status", "Hadir");
            
            logCollection.insertOne(newLog);
            
            System.out.println("Scan Masuk");
        } else {
            // Keluar
            Document update = new Document("$set",
                    new Document("waktuKeluar", java.time.LocalDateTime.now().toString())
            );
            
            logCollection.updateOne(
                    new Document("_id", logHariIni.getObjectId("_id")),
                    update
            
            );
            
            System.out.println("Scan Keluar");
        }
        
        if (DashboardFrame.instance != null){
            DashboardFrame.instance.refreshDashboard();
        }
        
    }
}
