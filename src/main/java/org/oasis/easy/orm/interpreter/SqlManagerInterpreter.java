package org.oasis.easy.orm.interpreter;

import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.annotations.Sql;
import org.oasis.easy.orm.dao.BasicDao;
import org.oasis.easy.orm.dialect.IDialect;
import org.oasis.easy.orm.dialect.mysql.MySqlDialect;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.mapper.sql.ConditionOperationMapper;
import org.oasis.easy.orm.mapper.sql.IOperationMapper;
import org.oasis.easy.orm.statement.StatementMetadata;
import org.oasis.easy.orm.statement.StatementRuntime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;

@Order(-1)
public class SqlManagerInterpreter implements Interpreter, InitializingBean {

    /**
     * 透传SQL
     */
    private static final Interpreter PassThroughInterpreter = new Interpreter() {
        @Override
        public void interpret(StatementRuntime runtime) {
        }
    };

    private IDialect dialect;

    public void setDialect(IDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void interpret(StatementRuntime runtime) {
        Interpreter interpreter = PassThroughInterpreter;
        StatementMetadata metadata = runtime.getMetadata();
        if (BasicDao.class.isAssignableFrom(metadata.getDaoMetadata().getDaoClass())) {
            // dao若要得到自动生成sql的能力,需要继承BasicDao
            Sql sql = metadata.getMethod().getAnnotation(Sql.class);
            if (sql == null || StringUtils.isEmpty(StringUtils.trim(sql.value()))) {
                /**
                 * 以下两种情况,系统自动生成sql
                 * 第一种情况:方法上没有标记@Sql注解(实际此种情况居多)
                 * 第二种情况:方法标记了@Sql注解,但没有写具体的sql语句
                 */
                IOperationMapper operationMapper = new ConditionOperationMapper();
                interpreter = new SqlGeneratorInterpreter(operationMapper);
            }
        }
        interpreter.interpret(runtime);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dialect == null) {
            dialect = new MySqlDialect();
        }
    }

    /**
     * SQL构造
     */
    private class SqlGeneratorInterpreter implements Interpreter {
        private final IOperationMapper operationMapper;

        public SqlGeneratorInterpreter(IOperationMapper operationMapper) {
            this.operationMapper = operationMapper;
        }

        @Override
        public void interpret(StatementRuntime runtime) {
            String generatedSql = dialect.translate(operationMapper, runtime);
            if (StringUtils.isEmpty(generatedSql)) {
                throw new EasyOrmException(ErrorCode.SERVICE_ERROR, "generate sql failed");
            }
            runtime.setSql(generatedSql);
        }
    }
}
