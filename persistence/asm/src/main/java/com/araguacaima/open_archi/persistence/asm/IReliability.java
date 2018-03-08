package com.araguacaima.open_archi.persistence.asm;

import java.util.Collection;

public interface IReliability {
    Boolean getContemplated();

    void setContemplated(Boolean contemplated);

    Markdown getDescription();

    void setDescription(Markdown description);

    Collection<Documentation> getDocumentationList();

    void setDocumentationList(Collection<Documentation> documentationList);

    Boolean getStandardSolution();

    void setStandardSolution(Boolean standardSolution);

    ReliabilitySolution getReliabilitySolution();

    void setReliabilitySolution(ReliabilitySolution reliabilitySolution);

}
