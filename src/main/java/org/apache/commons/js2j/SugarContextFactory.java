package org.apache.commons.js2j;

import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Context;

/**
 *
 */
public class SugarContextFactory extends ContextFactory
{
    private final SugarWrapFactory wrapFactory;

    public SugarContextFactory(SugarWrapFactory wrapFactory)
    {
        this.wrapFactory = wrapFactory;
    }

    @Override
    protected Context makeContext()
    {
        Context context = super.makeContext();
        context.setWrapFactory(wrapFactory);
        return context;
    }
}
