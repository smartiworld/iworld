package iworld.rpc.utils.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gq.cai
 * @version 1.0
 * @description
 * @date 2024/5/10 10:45
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface CellIndex {
    
    int index() default 0;
}
