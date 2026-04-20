package com.rfid.employeetracker.objects;

import java.time.LocalDateTime;
        
public class LogAbsensi {
    private String idLog;
    private String uidRfid;
    private LocalDateTime waktuMasuk;
    private LocalDateTime waktuKeluar;
    private String status;

    public LogAbsensi() {
        //
    }

    public LogAbsensi(String idLog, String uidRfid, LocalDateTime waktuMasuk, LocalDateTime waktuKeluar, String status) {
        this.idLog = idLog;
        this.uidRfid = uidRfid;
        this.waktuMasuk = waktuMasuk;
        this.waktuKeluar = waktuKeluar;
        this.status = status;
    }
    
    public String getTanggal(){
        if (waktuMasuk == null)return "-";
        return waktuMasuk.toLocalDate().toString();
    }
    
    public String getJamMasuk(){
        if (waktuMasuk == null)return "-";
        return waktuMasuk.toLocalTime().toString().substring(0,5);
    }
    
    public String getJamKeluar(){
        if (waktuKeluar == null)return "-";
        return waktuKeluar.toLocalTime().toString().substring(0,5);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdLog() {
        return idLog;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
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
