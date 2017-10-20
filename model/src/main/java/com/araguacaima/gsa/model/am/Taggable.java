package com.araguacaima.gsa.model.am;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

abstract class Taggable {

    private Set<Tag> tags = new LinkedHashSet<>();

    protected abstract Set<Tag> getRequiredTags();

    /**
     * Gets the comma separated list of tags.
     *
     * @return a comma separated list of tags,
     * or an empty string if there are no tags
     */
    public String getTags() {
        Set<Tag> setOfTags = new LinkedHashSet<>(getRequiredTags());
        setOfTags.addAll(tags);

        if (setOfTags.isEmpty()) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (Tag tag : setOfTags) {
            buf.append(tag.getValue());
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

        Collection<Tag> tags_ = new ArrayList<>(build(split));
        CollectionUtils.addAll(this.tags, tags_);
    }

    public void addTags(String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag_ : tags) {
            if (tag_ != null) {
                Tag tag = new Tag();
                tag.setValue(tag_);
                this.tags.add(tag);
            }
        }
    }

    public void removeTag(String tag) {
        if (tag != null) {
            CollectionUtils.filter(tags, tag_ -> tag_.getValue().equals(tag));
        }
    }

    public boolean hasTag(String tag) {
        return CollectionUtils.find(this.tags, tag_ -> tag_.getValue().equals(tag)) != null;
    }

    public static Set<Tag> build(String... tags) {
        Set<Tag> tags_ = new LinkedHashSet<>();
        for (String tag_ : tags) {
            Tag tag = new Tag();
            tag.setValue(tag_);
            tags_.add(tag);
        }
        return tags_;
    }
}
