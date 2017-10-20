package com.araguacaima.gsa.persistence.am;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The word "component" is a hugely overloaded term in the software development
 * industry, but in this context a component is simply a grouping of related
 * functionality encapsulated behind a well-defined interface. If you're using a
 * language like Java or C#, the simplest way to think of a component is that
 * it's a collection of implementation classes behind an interface. Aspects such
 * as how those components are packaged (e.g. one component vs many components
 * per JAR file, DLL, shared library, etc) is a separate and orthogonal concern.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Component", schema = "AM")
public class Component extends Element {

    @OneToOne
    private Container parent;

    @Column
    private String technology;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "Component_CodeElements",
            joinColumns = {@JoinColumn(name = "CodeElement_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "CodeElement_Id",
                    referencedColumnName = "Id")})
    private Set<CodeElement> codeElements = new HashSet<>();

    @Column
    private long size;

    public Component() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return parent;
    }

    void setParent(Container parent) {
        this.parent = parent;
    }

    @JsonIgnore
    public Container getContainer() {
        return parent;
    }

    /**
     * Gets the technology associated with this component (e.g. "Spring Bean").
     *
     * @return the technology, as a String,
     * or null if no technology has been specified
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Sets the technology associated with this component (e.g. "Spring Bean").
     *
     * @param technology the technology, as a String
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Gets the set of CodeElement objects.
     *
     * @return a Set, which could be empty
     */
    public Set<CodeElement> getCode() {
        return new HashSet<>(codeElements);
    }

    void setCode(Set<CodeElement> codeElements) {
        this.codeElements = codeElements;
    }

    /**
     * Gets the size of this Component (e.g. number of lines).
     *
     * @return the size of this component, as a long
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of this component (e.g. number of lines).
     *
     * @param size the size
     */
    public void setSize(long size) {
        this.size = size;
    }

    public Set<CodeElement> getCodeElements() {
        return codeElements;
    }

    public void setCodeElements(Set<CodeElement> codeElements) {
        this.codeElements = codeElements;
    }
}