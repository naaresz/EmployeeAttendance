package com.rfid.employeetracker.objects;

public class Karyawan {
    private String uidRfid;
    private String namaLengkap;
    private String email;
    private String noHp;
    private String jabatan;
    private String alamat;

    public Karyawan() {
        
    }

    public Karyawan(String uidRfid, String namaLengkap, String email, String noHp, String jabatan, String alamat) {
        this.uidRfid = uidRfid;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.noHp = noHp;
        this.jabatan = jabatan;
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    @Override
    public String toString() {
        return "Karyawan{" + 
                "uidRfid=" + uidRfid + 
                ", namaLengkap=" + namaLengkap + 
                ", email=" + email + 
                ", noHp=" + noHp + 
                ", jabatan=" + jabatan + 
                ", alamat=" + alamat + '}';
    }
    
    
}
