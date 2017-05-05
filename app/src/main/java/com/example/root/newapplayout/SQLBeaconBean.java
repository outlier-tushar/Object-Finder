package com.example.root.newapplayout;

/**
 * Created by root on 22/4/17.
 */

public class SQLBeaconBean
{
    private String atname;
    private int uuid;
    private String mac;
    private int rssi;
    private String sname;

    public String getATName() {return atname;}
    public int getUuid() {return uuid;}
    public String getMac() {return mac;}
    public int getRssi() {return rssi;}
    public String getSName() {return sname;}

    public void setATName(String name){this.atname = name;}
    public void setUuid(int uuid){this.uuid = uuid;}
    public void setMac(String mac){this.mac = mac;}
    public void setRssi(int rssi){this.rssi = rssi;}
    public void setSName(String sname){this.sname = sname;}

}
