package com.araguacaima.open_archi.web.common;

import java.util.Set;
import java.util.TreeSet;

public class Messages {

    private MessageSummary summary;
    private Set<Message> messages = new TreeSet<>();

    public MessageSummary getSummary() {
        return summary;
    }

    public void setSummary(MessageSummary summary) {
        this.summary = summary;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
