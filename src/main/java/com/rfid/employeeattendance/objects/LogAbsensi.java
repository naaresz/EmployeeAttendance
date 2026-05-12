package com.rfid.employeeattendance.objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class LogAbsensi {
    @BsonId
    private ObjectId idLog;
    private String uidRfid;
    private String nama;
    private LocalDateTime waktuMasuk;
    private LocalDateTime waktuKeluar;
    private String status;

    public LogAbsensi() {
        //
    }

    public LogAbsensi(ObjectId idLog, String uidRfid, String nama, LocalDateTime waktuMasuk, LocalDateTime waktuKeluar, String status) {
        this.idLog = idLog;
        this.uidRfid = uidRfid;
        this.nama = nama;
        this.waktuMasuk = waktuMasuk;
        this.waktuKeluar = waktuKeluar;
        this.status = status;
    }
    
    public String getTanggal(){
        if (waktuMasuk == null)return "-";
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("id", "ID"));
        
        return waktuMasuk.format(formatter);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ObjectId getIdLog() {
        return idLog;
    }

    public void setIdLog(ObjectId idLog) {
        this.idLog = idLog;
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }
    
    public String getNama(){
        return nama;
    }
    
    public void setNama(String nama){
        this.nama = nama;
    }

    public LocalDateTime getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(LocalDateTime waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public LocalDateTime getWaktuKeluar() {
        return waktuKeluar;
    }

    public void setWaktuKeluar(LocalDateTime waktuKeluar) {
        this.waktuKeluar = waktuKeluar;
    }
}
