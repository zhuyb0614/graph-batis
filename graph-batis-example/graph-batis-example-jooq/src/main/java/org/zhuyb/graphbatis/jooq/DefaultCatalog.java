/*
 * This file is generated by jOOQ.
 */
package org.zhuyb.graphbatis.jooq;


import org.jooq.Schema;
import org.jooq.impl.CatalogImpl;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.12.4"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class DefaultCatalog extends CatalogImpl {

    /**
     * The reference instance of <code></code>
     */
    public static final DefaultCatalog DEFAULT_CATALOG = new DefaultCatalog();
    private static final long serialVersionUID = -1717839405;
    /**
     * The schema <code></code>.
     */
    public final DefaultSchema DEFAULT_SCHEMA = org.zhuyb.graphbatis.jooq.DefaultSchema.DEFAULT_SCHEMA;

    /**
     * No further instances allowed
     */
    private DefaultCatalog() {
        super("");
    }

    @Override
    public final List<Schema> getSchemas() {
        List result = new ArrayList();
        result.addAll(getSchemas0());
        return result;
    }

    private final List<Schema> getSchemas0() {
        return Arrays.<Schema>asList(
                DefaultSchema.DEFAULT_SCHEMA);
    }
}