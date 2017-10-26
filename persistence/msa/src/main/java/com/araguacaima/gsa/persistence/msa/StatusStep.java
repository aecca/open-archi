package com.araguacaima.gsa.persistence.msa;

public enum StatusStep {
    PLACED(1),
    COMPLETING_MISSING_INFORMATION(2),
    ACCEPTED(3),
    REJECTED(4),
    ANALYSIS(5),
    COMPLETING_ADDITIONAL_INFORMATION(6),
    IN_EXECUTION(7),
    DONE(8);

    private final int rank;

    StatusStep(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}

