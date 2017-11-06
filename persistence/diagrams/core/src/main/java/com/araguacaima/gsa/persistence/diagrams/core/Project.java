package com.araguacaima.gsa.persistence.diagrams.core;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorColumn(name = "PROJ_TYPE")
@Table(name = "PROJECT")
public class Project extends BaseEntity{
@Column
    protected String name;
}