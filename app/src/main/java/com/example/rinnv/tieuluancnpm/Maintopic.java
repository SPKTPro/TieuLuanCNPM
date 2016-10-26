package com.example.rinnv.tieuluancnpm;

import java.net.IDN;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Maintopic {
    private int Maintopic_ID;
    private String Maintopic_Tittle;
    private String Maintopic_Tittle_VN;
    private int Maintopic_Process;

    public Maintopic(int Maintopic_ID,
                     String Maintopic_Tittle,
                     String Maintopic_Tittle_VN,
                     int Maintopic_Process) {
        this.Maintopic_ID = Maintopic_ID;
        this.Maintopic_Tittle = Maintopic_Tittle;
        this.Maintopic_Tittle_VN = Maintopic_Tittle_VN;
        this.Maintopic_Process = Maintopic_Process;
    }

    public int getMaintopic_ID() {
        return Maintopic_ID;
    }

    public int getMaintopic_Process() {
        return Maintopic_Process;
    }

    public String getMaintopic_Tittle() {
        return Maintopic_Tittle;
    }

    public String getMaintopic_Tittle_VN() {
        return Maintopic_Tittle_VN;
    }
}
