/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.matrix.java.text.MessageFormatMx;

/**
 * 编码消息。
 */
public class CodedMessage
    implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编码。
     */
    @Nonnull
    private final String code;

    /**
     * 时间。
     */
    private final long time;

    /**
     * 级别。
     */
    @Nonnull
    private final CodedMessageLevel level;

    /**
     * 参数列表。
     */
    @Nonnull
    private final List<String> arguments;

    /**
     * 不参与格式化的参数列表。
     */
    @Nonnull
    private final List<String> unformattedArguments;

    /**
     * 依附的消息列表。
     */
    @Nonnull
    private final List<CodedMessage> messages;

    /**
     * 构造器，设置时间为现在。
     *
     * @param code
     *     编码。
     * @param level
     *     级别。
     * @param arguments
     *     参数列表。
     */
    public CodedMessage(@Nonnull String code, @Nonnull CodedMessageLevel level, @Nonnull String... arguments) {
        this(code, System.currentTimeMillis(), level, arguments);
    }

    /**
     * 构造器。
     *
     * @param code
     *     编码。
     * @param time
     *     时间。
     * @param level
     *     级别。
     * @param arguments
     *     参数列表。
     */
    public CodedMessage(@Nonnull String code, long time, @Nonnull CodedMessageLevel level, @Nonnull String... arguments) {
        this.code = code;
        this.time = time;
        this.level = level;
        this.arguments = Lists.newArrayList(arguments);
        this.unformattedArguments = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    /**
     * 获取编码。
     */
    @Nonnull
    public String getCode() {
        return code;
    }

    /**
     * 获取时间。
     */
    public long getTime() {
        return time;
    }

    /**
     * 获取级别。
     */
    @Nonnull
    public CodedMessageLevel getLevel() {
        return level;
    }

    /**
     * 获取参数列表。
     */
    @Nonnull
    public List<String> getArguments() {
        return arguments;
    }

    /**
     * 获取不参与格式化的参数列表。
     */
    @Nonnull
    public List<String> getUnformattedArguments() {
        return unformattedArguments;
    }

    /**
     * 获取依附的消息列表。
     */
    @Nonnull
    public List<CodedMessage> getMessages() {
        return messages;
    }

    /**
     * 在参数列表中增加一个参数。
     *
     * @param argument
     *     参数。
     */
    public void addArgument(@Nullable String argument) {
        arguments.add(argument);
    }

    /**
     * 在不参与格式化的参数列表中增加一个参数。
     *
     * @param argument
     *     参数。
     */
    public void addUnformattedArgument(@Nullable String argument) {
        unformattedArguments.add(argument);
    }

    /**
     * 在依附的消息列表中增加一个消息。
     *
     * @param message
     *     消息。
     */
    public void addMessage(@Nonnull CodedMessage message) {
        messages.add(message);
    }

    /**
     * 判断消息或依附的消息列表中是否包含指定级别的消息。
     *
     * @param theLevel
     *     级别。
     * @return 是否包含指定级别的消息。
     */
    public boolean hasLevel(@Nonnull CodedMessageLevel theLevel) {
        if (level == theLevel) {
            return true;
        }
        for (CodedMessage message : messages) {
            if (message.hasLevel(theLevel)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将消息格式化为字符串形式。
     *
     * @return 消息的字符串形式。
     */
    @Nonnull
    public String format() {
        StringBuilder sb = new StringBuilder();
        format(sb);
        return sb.toString();
    }

    /**
     * 将消息格式化为字符串形式。
     */
    private void format(StringBuilder sb) {
        CodedMessageDefinition definition = CodedMessageDefinition.get(code);
        if (definition == null) {
            sb.append(MessageFormatMx.formatFallback(code, arguments.toArray()));
        } else {
            sb.append(MessageFormatMx.format(definition.getTemplate(), definition.getLocale(), arguments.toArray()));
        }
        for (String unformattedArgument : unformattedArguments) {
            sb.append('|');
            sb.append(unformattedArgument);
        }
    }

    /**
     * 将消息和依附的消息列表格式化为字符串形式。
     *
     * @return 消息的字符串形式。
     */
    @Nonnull
    public String formatAll() {
        StringBuilder sb = new StringBuilder();
        formatAll(sb, 0);
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 将消息和依附的消息列表格式化为字符串形式。
     */
    private void formatAll(StringBuilder sb, int depth) {
        for (int i = 0; i < depth; i++) {
            sb.append('\t');
        }
        format(sb);
        sb.append('\n');
        for (CodedMessage message : messages) {
            message.formatAll(sb, depth + 1);
        }
    }
}
