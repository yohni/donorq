package com.example.yohni.donorq;

class EventQ {
    private String nama;
    private String lokasi;
    private String tanggal;
    private String imageName;

    public EventQ(){}

    public EventQ(String nama, String lokasi, String tanggal, String imageName){
        this.nama = nama;
        this.lokasi = lokasi;
        this.tanggal = tanggal;
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getNama() {
        return nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
