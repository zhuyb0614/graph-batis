package org.zhuyb.graphbatis.interceptor;

import org.jooq.QueryPart;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zyb
 */
public class JooqCleanSqlVisitListener implements VisitListener, VisitListenerProvider {

    public static final Logger logger = LoggerFactory.getLogger(JooqCleanSqlVisitListener.class);

    @Override
    public void clauseStart(VisitContext context) {
        logger.info("clauseStart context {}", context);
    }

    @Override
    public void clauseEnd(VisitContext context) {
        logger.info("clauseEnd context {}", context);
    }

    @Override
    public void visitStart(VisitContext context) {
        logger.info("visitStart context {}", context);
    }

    @Override
    public void visitEnd(VisitContext context) {
        QueryPart[] queryParts = context.queryParts();
        context.renderContext();
        logger.info("visitEnd context {}", context);
    }

    @Override
    public VisitListener provide() {
        return this;
    }
}
