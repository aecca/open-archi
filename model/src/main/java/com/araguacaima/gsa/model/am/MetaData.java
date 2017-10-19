package com.araguacaima.gsa.model.am;

import com.araguacaima.gsa.model.persons.Responsible;
import com.araguacaima.gsa.model.common.Version;

import java.util.Collection;

public class MetaData {

    private Collection<Responsible> responsibles;
    private Collection<Responsible> collaborators;
    private Collection<Element> relatedWith;
    private Collection<Element> usedId;
    private Collection<Grouping> groupings;
    private DeploymentStatus deploymentStatus;
    private Version version;
    private Type type;
    private Collection<View> views;

}
