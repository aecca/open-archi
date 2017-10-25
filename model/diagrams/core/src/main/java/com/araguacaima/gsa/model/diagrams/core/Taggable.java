package com.araguacaima.gsa.model.diagrams.core;

import com.araguacaima.gsa.model.meta.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Taggable extends BaseEntity {

    private Set<String> tags = new LinkedHashSet<>();

    public static Set<String> build(String... tags) {
        Set<String> tags_ = new LinkedHashSet<>();
        for (String tag_ : tags) {
            tags_.add(tag_);
        }
        return tags_;
    }

    protected abstract Set<String> getRequiredTags();

    /**
     * Gets the comma separated list of tags.
     *
     * @return a comma separated list of tags,
     * or an empty string if there are no tags
     */
    public String getTags() {
        Set<String> setOfTags = new LinkedHashSet<>(getRequiredTags());
        setOfTags.addAll(tags);

        if (setOfTags.isEmpty()) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (String tag : setOfTags) {
            buf.append(tag);
            buf.append(",");
        }

        String tagsAsString = buf.toString();
        return tagsAsString.substring(0, tagsAsString.length() - 1);
    }

    void setTags(String tags) {
        if (tags == null) {
            return;
        }

        this.tags.clear();
        String[] split = tags.split(",");

        Collection<String> tags_ = new ArrayList<>(build(split));
        CollectionUtils.addAll(this.tags, tags_);
    }

    public void addTags(String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag_ : tags) {
            if (tag_ != null) {
                this.tags.add(tag_);
            }
        }
    }

    public void removeTag(String tag) {
        if (tag != null) {
            CollectionUtils.filter(tags, tag_ -> tag_.equals(tag));
        }
    }

    public boolean hasTag(String tag) {
        return CollectionUtils.find(this.tags, tag_ -> tag_.equals(tag)) != null;
    }
}
