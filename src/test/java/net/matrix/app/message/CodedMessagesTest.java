/*
 * Copyright(C) 2009 matrix
 * All right reserved.
 */
package net.matrix.app.message;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CodedMessagesTest {
    @Test
    public void testSaveAndLoad()
        throws IOException, CodedMessageException {
        List<CodedMessage> messageList = new ArrayList<>();
        messageList.add(CodedMessages.information("System.Error", "test1", "test2"));
        messageList.add(CodedMessages.information("100000000", "test3"));
        messageList.get(1).getUnformattedArguments().add("unformatted");
        messageList.get(1).getMessages().add(CodedMessages.debug("12345", "moo..."));
        String messageString;
        try (StringWriter os = new StringWriter()) {
            CodedMessages.save(messageList, os);
            messageString = os.toString();
        }
        // 读取
        List<CodedMessage> messageList2;
        try (Reader is = new StringReader(messageString)) {
            messageList2 = CodedMessages.load(is);
        }
        Assertions.assertThat(messageList2).hasSameSizeAs(messageList);
        for (int index = 0; index < messageList.size(); index++) {
            CodedMessage message1 = messageList.get(index);
            CodedMessage message2 = messageList2.get(index);
            Assertions.assertThat(message2.getCode()).isEqualTo(message1.getCode());
            Assertions.assertThat(message2.getTime()).isEqualTo(message1.getTime());
            Assertions.assertThat(message2.getArguments()).isEqualTo(message1.getArguments());
            Assertions.assertThat(message2.getUnformattedArguments()).isEqualTo(message1.getUnformattedArguments());
            Assertions.assertThat(message2.getMessages()).hasSameSizeAs(message1.getMessages());
        }
    }
}
