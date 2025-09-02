package com.example.moodin.stress;

public enum StressLevel {
    LOW(0, "낮음"),
    HIGH(1, "높음"),
    RISK(2, "위험"),
    CRITICAL(3, "고위험");

    public final int severity;
    public final String label;
    StressLevel(int s, String label){ this.severity=s; this.label=label; }
    public static StressLevel max(StressLevel a, StressLevel b){
        return a.severity>=b.severity? a:b;
    }
}
