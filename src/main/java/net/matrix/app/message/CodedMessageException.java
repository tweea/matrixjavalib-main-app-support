/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import net.matrix.app.SystemException;

/**
 * 编码消息的系统异常。
 */
public class CodedMessageException
    extends SystemException {
    private static final long serialVersionUID = 1L;

    /**
     * 使用默认消息构造异常。原因异常没有初始化，可以随后调用 {@link #initCause} 进行初始化。
     */
    public CodedMessageException() {
        super();
    }

    /**
     * 使用指定消息编码构造异常。原因异常没有初始化，可以随后调用 {@link #initCause} 进行初始化。
     * 
     * @param messageCode
     *     消息编码。
     */
    public CodedMessageException(String messageCode) {
        super(messageCode);
    }

    /**
     * 使用指定消息构造异常。原因异常没有初始化，可以随后调用 {@link #initCause} 进行初始化。
     * 
     * @param message
     *     消息。
     */
    public CodedMessageException(CodedMessage message) {
        super(message);
    }

    /**
     * 使用指定原因异常构造异常，详细信息指定为 <code>(cause==null ? null : cause.toString())</code> （特别地指定原因异常的类和详细信息）。
     * 
     * @param cause
     *     原因异常（使用 {@link #getCause()} 方法获取）。可以使用 <code>null</code> 值，指原因异常不存在或未知。
     */
    public CodedMessageException(Throwable cause) {
        super(cause);
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
    public CodedMessageException(Throwable cause, String messageCode) {
        super(cause, messageCode);
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
    public CodedMessageException(Throwable cause, CodedMessage message) {
        super(cause, message);
    }

    @Override
    public String getDefaultMessageCode() {
        return "CodedMessage.Error";
    }
}
