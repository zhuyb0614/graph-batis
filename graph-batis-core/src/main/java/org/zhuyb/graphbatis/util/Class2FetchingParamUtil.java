package org.zhuyb.graphbatis.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Primitives;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.zhuyb.graphbatis.anno.Ignore;
import org.zhuyb.graphbatis.anno.QueryName;
import org.zhuyb.graphbatis.entity.FetchingField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yunbo.zhu
 * @version 1.0
 * @date 2021/7/9 6:07 下午
 */
@UtilityClass
public class Class2FetchingParamUtil {

    private static final String GET = "get";


    private static List<FetchingField> transfer(Class clazz) {
        return transfer(clazz, 2);
    }

    private static List<FetchingField> transfer(Class clazz, int maxDeep) {
        return transfer(clazz, new Deep().setMax(maxDeep));
    }

    private static List<FetchingField> transfer(Class clazz, Deep deep) {
        List<FetchingField> fetchingFields = null;
        if (deep.getCurrent() < deep.getMax()) {
            fetchingFields = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();
            Set<String> getMethodFieldNames = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.getName().startsWith(GET))
                    .map(method -> CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL).convert(method.getName().replaceFirst(GET, ""))).collect(Collectors.toSet());
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                if (!field.isAnnotationPresent(Ignore.class) && getMethodFieldNames.contains(field.getName())) {
                    FetchingField fetchingField = new FetchingField();
                    fetchingField.setFiledName(field.getName());
                    if (String.class.equals(fieldType) || Primitives.isWrapperType(fieldType) || fieldType.isPrimitive()) {
                        fetchingField.setPrimitive(true);
                    } else if (Collection.class.isAssignableFrom(fieldType)) {
                        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                        fetchingField.setSubParams(transfer((Class) parameterizedType.getActualTypeArguments()[0], new Deep().setMax(deep.getMax() - 1).setCurrent(0)));
                    } else {
                        fetchingField.setSubParams(transfer(fieldType, new Deep().setMax(deep.getMax() - 1).setCurrent(0)));
                    }
                    if (fetchingField != null) {
                        fetchingFields.add(fetchingField);
                    }
                }
            }
        }
        return fetchingFields;
    }

    public static String toQueryString(Class clazz, int maxDeep, String queryName) {
        Preconditions.checkArgument(maxDeep > 0, "最大递归深度不能小于1");
        if (queryName == null) {
            Annotation annotation = clazz.getDeclaredAnnotation(QueryName.class);
            if (annotation != null) {
                QueryName queryNameAnnotation = (QueryName) annotation;
                queryName = queryNameAnnotation.value();
            }
        }
        Preconditions.checkArgument(StringUtils.isNotBlank(queryName), "查询方法名为空");
        return String.format("{%s%s}", queryName, toQueryString(transfer(clazz, maxDeep)));
    }

    public static String toQueryString(Class clazz) {
        return toQueryString(clazz, 2, null);
    }

    private static String toQueryString(List<FetchingField> fetchingFields) {
        if (fetchingFields != null && !fetchingFields.isEmpty()) {
            StringBuilder queryStringBuilder = new StringBuilder();
            queryStringBuilder.append("{");
            for (FetchingField fetchingField : fetchingFields) {
                if (fetchingField.isPrimitive()) {
                    queryStringBuilder.append(fetchingField.getFiledName());
                    queryStringBuilder.append(",");
                } else if (fetchingField.getSubParams() != null && !fetchingField.getSubParams().isEmpty()) {
                    queryStringBuilder.append(fetchingField.getFiledName());
                    queryStringBuilder.append(toQueryString(fetchingField.getSubParams()));
                    queryStringBuilder.append(",");
                }
            }
            queryStringBuilder.append("}");
            return queryStringBuilder.toString().replaceAll(",}", "}");
        }
        return "";
    }

    @Data
    @Accessors(chain = true)
    public static class Deep {
        private int max;
        private int current;
    }
}
