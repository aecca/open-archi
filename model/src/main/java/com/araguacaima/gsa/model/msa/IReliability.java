package com.araguacaima.gsa.model.msa;

import java.util.Collection;

public interface IReliability {
    Boolean getContemplated();

    void setContemplated(Boolean contemplated);

    Markdown getDescription();

    void setDescription(Markdown description);

    Collection<String> getDocumentationList();

    void setDocumentationList(Collection<String> documentationList);

    Boolean getStandardSolution();

    void setStandardSolution(Boolean standardSolution);

}
