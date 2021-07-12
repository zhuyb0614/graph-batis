package org.zhuyb.graphbatis.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yunbo.zhu
 * @version 1.0
 * @date 2021/7/12 4:45 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryName {
    String value() default "";
}
