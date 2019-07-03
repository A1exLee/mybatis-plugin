package com.alexlee.mybatis.inteceptor;

import com.google.common.base.Stopwatch;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 日志输出插件
 *
 * @author alexlee
 * @version 1.0
 * @date 2019/7/2 21:28
 */

@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class SqlStdInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        System.out.println("执行的sql为：" + boundSql.getSql());
        try {
            return invocation.proceed();
        } finally {
            System.out.println("耗时：" + stopwatch.stop());
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        Enumeration<?> enumeration = properties.propertyNames();
        System.out.println("properties:");
        while(enumeration.hasMoreElements()){
            System.out.println(enumeration.nextElement().toString());
        }
    }
}
