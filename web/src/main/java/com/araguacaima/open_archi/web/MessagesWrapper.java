package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.web.common.Message;
import com.araguacaima.open_archi.web.common.MessageSummary;
import com.araguacaima.open_archi.web.common.Messages;

import java.util.Map;

public class MessagesWrapper {

    public static Messages fromSpecificationMapToMessages(Map<String, Object> specificationMap) {
        Messages messages = new Messages();
        if (specificationMap == null) {
            return messages;
        }
        String code = Constants.SPECIFICATION_ERROR;
        Object value = specificationMap.get(code);
        fillMessage(messages, code, value);
        code = Constants.SPECIFICATION_ERROR_CREATION;
        value = specificationMap.get(code);
        fillMessage(messages, code, value);
        specificationMap.get(code);
        code = Constants.SPECIFICATION_ERROR_MODIFICATION;
        value = specificationMap.get(code);
        fillMessage(messages, code, value);
        specificationMap.get(code);
        code = Constants.SPECIFICATION_ERROR_DELETION;
        value = specificationMap.get(code);
        fillMessage(messages, code, value);
        specificationMap.get(code);
        code = Constants.SPECIFICATION_ERROR_REPLACEMENT;
        value = specificationMap.get(code);
        fillMessage(messages, code, value);
        specificationMap.get(code);
        code = Constants.SPECIFICATION_ERROR_ALREADY_EXISTS;
        value = specificationMap.get(code);
        fillMessage(messages, code, value);
        MessageSummary summary = new MessageSummary();
        summary.setTotalMessages(messages.getMessages().size());
        messages.setSummary(summary);
        return messages;
    }

    private static void fillMessage(Messages messages, String code, Object value) {
        if (value != null) {
            Message message = new Message();
            message.setCode(code);
            message.setMessage(value.toString());
            messages.addMessage(message);
        }
    }
}
