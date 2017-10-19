package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import java.util.Set;

public class Volumetry extends BaseEntity {

    private Set<Activity> activities;

    private Set<BatchProcessing> batchProcessing;

    private Set<BulkProcessing> bulkProcessing;

    private Set<Concurrency> concurrencies;

    private Set<Database> dataBase;

    private Set<FileTransfer> fileTransfers;

    private Set<Rate> rates;

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Set<BatchProcessing> getBatchProcessing() {
        return batchProcessing;
    }

    public void setBatchProcessing(Set<BatchProcessing> batchProcessing) {
        this.batchProcessing = batchProcessing;
    }

    public Set<BulkProcessing> getBulkProcessing() {
        return bulkProcessing;
    }

    public void setBulkProcessing(Set<BulkProcessing> bulkProcessing) {
        this.bulkProcessing = bulkProcessing;
    }

    public Set<Concurrency> getConcurrencies() {
        return concurrencies;
    }

    public void setConcurrencies(Set<Concurrency> concurrencies) {
        this.concurrencies = concurrencies;
    }

    public Set<Database> getDataBase() {
        return dataBase;
    }

    public void setDataBase(Set<Database> dataBase) {
        this.dataBase = dataBase;
    }

    public Set<FileTransfer> getFileTransfers() {
        return fileTransfers;
    }

    public void setFileTransfers(Set<FileTransfer> fileTransfers) {
        this.fileTransfers = fileTransfers;
    }

    public Set<Rate> getRates() {
        return rates;
    }

    public void setRates(Set<Rate> rates) {
        this.rates = rates;
    }
}
