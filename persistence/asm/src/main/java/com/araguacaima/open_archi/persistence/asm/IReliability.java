package com.araguacaima.open_archi.persistence.asm;

import java.util.Set;

public interface IReliability {
    Boolean getContemplated();

    void setContemplated(Boolean contemplated);

    Markdown getDescription();

    void setDescription(Markdown description);

    Set<Documentation> getDocumentationList();

    void setDocumentationList(Set<Documentation> documentationList);

    Boolean getStandardSolution();

    void setStandardSolution(Boolean standardSolution);

    ReliabilitySolution getReliabilitySolution();

    void setReliabilitySolution(ReliabilitySolution reliabilitySolution);

}
