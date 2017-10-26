package com.araguacaima.gsa.persistence.diagrams.core;


import com.araguacaima.gsa.util.Url;

import java.util.Set;

/**
 * Represents a feature, such as a Java class or interface,
 * that is part of the implementation of a component.
 */
public class Feature extends Item {

    /**
     * the role of the feature ... Primary or Supporting
     */
    private FeatureRole role = FeatureRole.Supporting;

    /**
     * the fully qualified type of the feature
     **/
    private String type;

    /**
     * a URL; e.g. a reference to the feature in source code control
     */
    private String url;

    /**
     * the programming language used to create the feature
     */
    private String language;

    /**
     * the category of feature; e.g. class, interface, etc
     */
    private String category;

    /**
     * the visibility of the feature; e.g. public, package, private
     */
    private String visibility;

    /**
     * the size of the feature; e.g. the number of lines
     */
    private long size;
    private ElementKind kind = ElementKind.FEATURE;

    Feature() {
    }

    Feature(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName == null || fullyQualifiedTypeName.trim().isEmpty()) {
            throw new IllegalArgumentException("A fully qualified name must be provided.");
        }

        int dot = fullyQualifiedTypeName.lastIndexOf('.');
        if (dot > -1) {
            setName(fullyQualifiedTypeName.substring(dot + 1, fullyQualifiedTypeName.length()));
            this.type = fullyQualifiedTypeName;
        } else {
            setName(fullyQualifiedTypeName);
            this.type = fullyQualifiedTypeName;
        }
    }

    /**
     * Gets the role of this feature; Primary or Supporting.
     *
     * @return a FeatureRole enum
     */
    public FeatureRole getRole() {
        return role;
    }

    void setRole(FeatureRole role) {
        this.role = role;
    }

    /**
     * Gets the type (fully qualified type name) of this feature.
     *
     * @return the type, as a String
     */
    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    @Override
    protected String getCanonicalNameSeparator() {
        return null;
    }

    @Override
    public ElementKind getKind() {
        return this.kind;
    }

    @Override
    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    /**
     * Gets the URL where more information about this feature can be found.
     *
     * @return the URL as a String, or null if not set
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where more information about this feature can be found.
     *
     * @param url the URL as a String
     * @throws IllegalArgumentException if the URL is not a well-formed URL
     */
    public void setUrl(String url) {
        if (url != null && url.trim().length() > 0) {
            if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    /**
     * Gets the programming language of this feature.
     *
     * @return the programming language, as a String
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the programming language of this feature.
     *
     * @param language the programming language, as a String
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Gets the category of this feature (interface, class, etc).
     *
     * @return the category, as a String
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of this feature.
     *
     * @param category the category, as a String
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the visibility of this feature (public, package, etc).
     *
     * @return the visibility, as a String
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility of this feature.
     *
     * @param visibility the visibility, as a String
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    /**
     * Gets the size of this feature (e.g. the number of lines of code).
     *
     * @return the size, as a long
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of this feature.
     *
     * @param size the size, as a long
     */
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature that = (Feature) o;

        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    protected Set<String> getRequiredTags() {
        return null;
    }
}