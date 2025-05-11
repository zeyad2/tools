package org.os.minisocial.config;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.transaction.Transactional;

@Interceptor
@Transactional
public class TransactionalInterceptor {
    @AroundInvoke
    public Object manageTransaction(InvocationContext ctx) throws Exception {
        return ctx.proceed();
    }
}