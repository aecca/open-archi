package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.Set;


@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "DIAGRAMS",
        name = "Constraint")
public class Constraint extends BaseEntity {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Constraint_Activities",
            joinColumns = {@JoinColumn(name = "Constraint_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Activities_Id",
                    referencedColumnName = "Id")})
    private Set<Activity> activities;
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Constraint_BatchProcessing",
            joinColumns = {@JoinColumn(name = "Constraint_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "BatchProcessing_Id",
                    referencedColumnName = "Id")})
    private Set<BatchProcessing> batchProcessing;
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Constraint_BulkProcessing",
            joinColumns = {@JoinColumn(name = "Constraint_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "BulkProcessing_Id",
                    referencedColumnName = "Id")})
    private Set<BulkProcessing> bulkProcessing;
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Constraint_Concurrency",
            joinColumns = {@JoinColumn(name = "Constraint_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Concurrency_Id",
                    referencedColumnName = "Id")})
    private Set<Concurrency> concurrencies;
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Constraint_Database",
            joinColumns = {@JoinColumn(name = "Constraint_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Database_Id",
                    referencedColumnName = "Id")})
    private Set<Database> dataBase;
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Constraint_FileTransfer",
            joinColumns = {@JoinColumn(name = "Constraint_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "FileTransfer_Id",
                    referencedColumnName = "Id")})
    private Set<FileTransfer> fileTransfers;
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Constraint_Rate",
            joinColumns = {@JoinColumn(name = "Constraint_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Rate_Id",
                    referencedColumnName = "Id")})
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

    @Override
    public void validateRequest() throws EntityError {
        //Do nothing. All request are valid on this entity
    }

    @Override
    public void validateCreation() {
        if (!(activities != null || batchProcessing != null || bulkProcessing != null || concurrencies != null || dataBase != null || fileTransfers != null || rates != null)) {
            throw new EntityError(resourceBundle.getString(getCreationErrorMessageKey()));
        }
    }

    @Override
    public void validateModification() throws EntityError {
        if (!(id == null && (
                activities != null || batchProcessing != null || bulkProcessing != null || concurrencies != null || dataBase != null || fileTransfers != null || rates != null))) {
            throw new EntityError(resourceBundle.getString(getModificationErrorMessageKey()));
        }
    }
}
