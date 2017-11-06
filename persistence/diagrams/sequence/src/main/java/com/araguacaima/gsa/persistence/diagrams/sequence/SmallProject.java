package com.araguacaima.gsa.persistence.diagrams.sequence;

import com.araguacaima.gsa.persistence.diagrams.core.Project;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("S")
public class SmallProject extends Project {
}