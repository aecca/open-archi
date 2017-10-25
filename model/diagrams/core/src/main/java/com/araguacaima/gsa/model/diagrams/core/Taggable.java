package com.araguacaima.gsa.model.diagrams.core;

import com.araguacaima.gsa.model.meta.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Taggable extends BaseEntity {

    private Set<String> tags = new LinkedHashSet<>();

    protected abstract Set<String> getRequiredTags();

    protected Set<String> build(String... tags) {
        Set<String> tags_ = new LinkedHashSet<>();
        for (String tag_ : tags) {
            tags_.add(tag_);
        }
        return tags_;
    }
    /**
     * Gets the comma separated list of tags.
     *
     * @return a comma separated list of tags,
     * or an empty string if there are no tags
     */
    @Override
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

        Collection<String> tags_ = new ArrayList<String>(build(split));
        CollectionUtils.addAll(this.tags, tags_);
    }

    @Override
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

    @Override
    public void removeTag(String tag) {
        if (tag != null) {
            CollectionUtils.filter(tags, tag_ -> tag_.equals(tag));
        }
    }

    @Override
    public boolean hasTag(String tag) {
        return CollectionUtils.find(this.tags, tag_ -> tag_.equals(tag)) != null;
    }
}
