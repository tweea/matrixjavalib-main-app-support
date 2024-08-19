/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import net.matrix.app.message.CodedMessage;
import net.matrix.app.message.CodedMessageMx;

/**
 * 系统非检查异常，包含编码消息。
 */
@Immutable
public class SystemRuntimeException
    extends RuntimeException
    implements CodedException {
    private static final long serialVersionUID = 1L;

    /**
     * 消息。
     */
    @Nonnull
    private final CodedMessage message;

    /**
     * 使用默认消息构造异常。原因异常没有初始化，可以随后调用 {@link #initCause} 进行初始化。
     */
    public SystemRuntimeException() {
        this.message = CodedMessageMx.error(getDefaultMessageCode());
    }

    /**
     * 使用指定消息编码构造异常。原因异常没有初始化，可以随后调用 {@link #initCause} 进行初始化。
     *
     * @param messageCode
     *     消息编码。
     */
    public SystemRuntimeException(@Nonnull String messageCode) {
        this.message = CodedMessageMx.error(messageCode);
    }

    /**
     * 使用指定消息构造异常。原因异常没有初始化，可以随后调用 {@link #initCause} 进行初始化。
     *
     * @param message
     *     消息。
     */
    public SystemRuntimeException(@Nonnull CodedMessage message) {
        this.message = message;
    }

    /**
     * 使用指定原因异常构造异常，详细信息指定为 <code>(cause==null ? null : cause.toString())</code> （特别地指定原因异常的类和详细信息）。
     *
     * @param cause
     *     原因异常（使用 {@link #getCause()} 方法获取）。可以使用 <code>null</code> 值，指原因异常不存在或未知。
     */
    public SystemRuntimeException(@Nullable Throwable cause) {
        super(cause);
        this.message = CodedMessageMx.error(getDefaultMessageCode());
        initCauseMessage(cause);
    }

    /**
     * 使用指定消息编码和原因异常构造异常。<br>
     * 注意与 <code>cause</code> 关联的详细信息<i>不会</i>自动出现在本异常的详细信息中。
     *
     * @param cause
     *     原因异常（使用 {@link #getCause()} 方法获取）。可以使用 <code>null</code> 值，指原因异常不存在或未知。
     * @param messageCode
     *     消息编码。
     */
    public SystemRuntimeException(@Nullable Throwable cause, @Nonnull String messageCode) {
        super(cause);
        this.message = CodedMessageMx.error(messageCode);
        initCauseMessage(cause);
    }

    /**
     * 使用指定消息和原因异常构造异常。<br>
     * 注意与 <code>cause</code> 关联的详细信息<i>不会</i>自动出现在本异常的详细信息中。
     *
     * @param cause
     *     原因异常（使用 {@link #getCause()} 方法获取）。可以使用 <code>null</code> 值，指原因异常不存在或未知。
     * @param message
     *     消息。
     */
    public SystemRuntimeException(@Nullable Throwable cause, @Nonnull CodedMessage message) {
        super(cause);
        this.message = message;
        initCauseMessage(cause);
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        super.initCause(cause);
        initCauseMessage(cause);
        return this;
    }

    /**
     * 初始化原因异常的消息。
     *
     * @param cause
     *     原因异常。
     */
    private void initCauseMessage(Throwable cause) {
        if (cause instanceof CodedException) {
            CodedException ce = (CodedException) cause;
            this.message.addMessage(ce.getCodedMessage());
        } else if (cause != null) {
            this.message.addUnformattedArgument(cause.getMessage());
        }
    }

    @Override
    public String getDefaultMessageCode() {
        return "System.Error";
    }

    @Override
    public CodedMessage getCodedMessage() {
        return message;
    }

    @Override
    public String getMessage() {
        return message.formatAll();
    }
}
