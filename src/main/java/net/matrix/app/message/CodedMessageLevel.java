/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.util.Map;

import net.matrix.java.lang.EnumMx;

/**
 * 编码消息级别。
 */
public enum CodedMessageLevel {
    /**
     * 跟踪。
     */
    TRACE(1),

    /**
     * 调试。
     */
    DEBUG(2),

    /**
     * 消息。
     */
    INFO(3),

    /**
     * 警告。
     */
    WARN(4),

    /**
     * 错误。
     */
    ERROR(5),

    /**
     * 致命错误。
     */
    FATAL(6);

    private static final Map<Integer, CodedMessageLevel> CODE_MAP = EnumMx.buildValueMap(CodedMessageLevel.class, v -> v.code);

    /**
     * 编码。
     */
    public final Integer code;

    CodedMessageLevel(Integer code) {
        this.code = code;
    }

    /**
     * 编码转换为枚举值。
     * 
     * @param code
     *     编码。
     * @return 枚举值。
     */
    public static CodedMessageLevel forCode(Integer code) {
        return CODE_MAP.get(code);
    }
}
