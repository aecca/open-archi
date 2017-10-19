package com.araguacaima.gsa.model.msa;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "Msa",
        name = "DisasterRecoveryReliability")
public class DisasterRecoveryReliability extends AbstractReliability {

}
