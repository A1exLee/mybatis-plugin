package com.alexlee.mybatis.inteceptor;

import com.google.common.base.Stopwatch;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
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

@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})})
public class RowBoundInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        BoundSql boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);
        System.out.println("原执行的sql为：" + boundSql.getSql());
        RowBounds rowBounds= (RowBounds) invocation.getArgs()[2];
        if(rowBounds==RowBounds.DEFAULT){
            return invocation.proceed();
        }
        String sql= "select * from (" +boundSql.getSql()+" ) t limit "+rowBounds.getOffset()+" , "+(rowBounds.getLimit()+rowBounds.getOffset());
        // 自定义sqlSource
        SqlSource sqlSource = new StaticSqlSource(mappedStatement.getConfiguration(), sql, boundSql.getParameterMappings());
        invocation.getArgs()[2]=RowBounds.DEFAULT;
        // 修改原来的sqlSource
        Field field = MappedStatement.class.getDeclaredField("sqlSource");
        field.setAccessible(true);
        field.set(mappedStatement, sqlSource);

        // 执行被拦截方法
        return invocation.proceed();
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
