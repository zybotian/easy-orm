### rose jade框架的使用方式

```java
@Dao
public interface UserDAO {
    // C
    @Sql("insert into " + TABLE_NAME + "(" + INSERT_COLUMNS + ")" + "values(" + INSERT_VALUES + ")")
    int insertOne(User user);
    // R
    @Sql("select " + SELECT_COLUMNS + " from " + TABLE_NAME + " where id=:1")
    User findByPrimaryKey(Long id);
    // U
    @Sql("update " + TABLE_NAME + " set " + UPDATE_COLUMNS + " where id=:1.id")
    int updateOne(User user);
    // D
    @Sql("delete from " + TABLE_NAME + " where id=:1")
    int deleteOne(Long id);
}
```

### rose使用不方便的地方

在设计好表结构后,还需要:

- 需要自己定义model类
- 需要自己写XxxDao接口类
- 由于自己写sql很可能出错，开发时需要花费大量精力去写dao方法的junit单元测试

在表结构复杂的时候,手写XxxDao很繁杂且容易出错. 

尝试之一:[Druid SqlParser对建表语句进行语法分析,得到表名,列名,列类型等信息,程序自动拼出model类和DAO类](https://github.com/zybotian/common-basic-service)

尝试之二:[EasyOrm框架,通过反射的方式拿到表名,列名,类型,程序自动生成sql语句](https://github.com/zybotian/easy-orm)

- 比较
   - Rose框架要求通过@Sql(sql语句)注解指定要执行的sql语句,需要程序员手写sql
   
   ```text
    @Sql("insert into " + TABLE_NAME + "(" + INSERT_COLUMNS + ")" + "values(" + INSERT_VALUES + ")")
    int insertOne(User user);
   ```
   
   - EasyOrm不要求Dao方法上添加注解@Sql,而是EasyOrm自己生成sql语句
   ```
   ID insert(ENTITY entity);
   ```
   
   - 当然，EasyOrm是对Rose的封装，也支持rose的@Sql注解直接手写Sql的使用方式
   
- 基于Spring、对用户自定义接口类做文章的通用套路

![通用套路](https://github.com/zybotian/easy-orm/blob/master/imgs/eo_framework.png)

- 使用上述通用套路的框架有：
   - Rose(公司内部使用的框架)
      - 扫有@DAO标记且以DAO结尾的class
      - 注册到spring容器
      - 注册时指定class为DAOFactoryBean
      - DAOFactoryBean接口的方法getObject方法中创建JDK动态代理
      - 程序调用接口方法实际是调用动态代理的invoke
      - 将DAO的方法上@Sql指定的sql语句拿到，invoke方法中拿到参数，经过各种处理，最后得到要执行的sql语句以及sql中占位符的值

   - DYCONF(公司内部使用的框架)
      - 扫描带有@DYCONF标记且以DONF结尾的class
      - 注册到spring容器
      - 注册时指定class为ConfFactoryBean
      - ConfFactoryBean接口的方法getObject方法中创建JDK动态代理
      - 程序调用接口方法实际时调用动态代理的invoke
      - 将CONF类上的@Rootkey以及方法上的@ConfName拿到，拼接成rootkey+name
      - 用上述组合key去zookeeper中拿到对应的值，解析，并返回给应用程序
      
### Easy Orm实现原理

1. EasyOrmBeanFactoryPostProcessor implements BeanFactoryPostProcessor
```text
    private String baseDir;
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 1. 查找指定目录下所有的带有自定义@Dao注解标记的.class
        
        //    1.1 spring的ResourcePatternResolver.getResources(baseDir)可以得到所有符合要求的.class对应的Resource对象数组
        //    1.2 遍历Resource数组, spring的MetadataReaderFactory.getMetadataReader(resource)得到MetadataReader
        //    1.3 new ScannedGenericBeanDefinition(metadataReader)得到ScannedGenericBeanDefinition对象
        
        // 2. 将所有符合预期的扫描到的.class构造成ScannedGenericBeanDefinition对象返回
        Set<BeanDefinition> candidates = findDaoCandidate();
        if (CollectionUtils.isEmpty(candidates)) {
            return;
        }

        // 3. 遍历ScannedGenericBeanDefinition列表,调用beanFactory.registerBeanDefinition()将bean定义注册到spring的bean factory
        //    3.1 在register之前, 先执行scannedBeanDefinition.setBeanClass(EasyOrmFactoryBean.class);
        //    3.2 EasyOrmFactoryBean是FactoryBean接口的实现类, 指定beanClass为FactoryBean后,spring创建bean对象时将调用FactoryBean的getObject方法来创建代理对象
        for (BeanDefinition beanDefinition : candidates) {
            registerDaoBeanDefinition(beanDefinition, beanFactory);
        }
    }
```

2. EasyOrmFactoryBean implements FactoryBean

- 如果bean的class是FactoryBean,spring创建bean时将调用getObject方法来创建

- 在getObject方法中通过JDK动态代理创建每一个DAO接口的代理类

```text
    @Override
    public Object getObject() throws Exception {
        if (targetProxy == null) {
            // 1. 通过JDK动态代理创建DAO接口的代理类
            // 2. 指定InvocationHandler为EasyOrmInvocationHandler
            targetProxy = Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class[]{objectType},
                    new EasyOrmInvocationHandler(daoMetaData));
        }
        return targetProxy;
    }
```

3. EasyOrmInvocationHandler implements InvocationHandler
- 最终所有对dao接口的调用都是调用invocation的invoke方法

```
@Override
public Object invoke(Object proxy, Method method, Object[] args) {
   // 间接调用jdbc操作数据库
}
```

4. SystemInterpreter和SqlManagerInterpreter

- SystemInterpreter是Rose框架提供,在执行真正的jdbc操作之前执行
   - 先通过method.getAnnotation(Sql.class)拿到@Sql注解对象
   - 通过@Sql注解可以拿到用户指定的sql语句
   - 使用apache的jexl表达式引擎对sql语句中的占位符替换成参数的实际值

- EasyOrm框架添加了SqlManagerInterpreter
```java
public class SqlManagerInterpreter implements Interpreter {
    // 什么也不做
    private static final Interpreter PassThroughInterpreter = new Interpreter() {
        @Override
        public void interpret(StatementRuntime runtime) {
        }
    };
    
    // 生成sql的入口
    private static final Interpreter SqlGeneratorInterpreter = new Interpreter() {
        @Override
        public void interpret(StatementRuntime runtime) {
           //  生成sql语句
        }
    };
    
    @Override
    void interpret(StatementRuntime runtime) {
        Sql sql = runtime.getMethod().getAnnotation(Sql.class);
        if (sql != null) {
            // 有注解的, SqlManagerInterpreter什么也不做 
            PassThroughInterpreter.interpret();
        } else {
            // 没有@Sql注解的,SqlManagerInterpreter调用EasyOrm的SqlGeneratorInterpreter来interpret
            SqlGeneratorInterpreter.interpret();
        }
    }
}
```

5. SqlGeneratorInterpreter负责sql语句的生成---以"ID insert(ENTITY entity)"为例说明
```
// 框架提供的基类接口
public interface BasicDao<ENTITY, ID> {
    ID insert(ENTITY entity);
}

// 用户自定义的接口类
@Dao
public interface UserDao extends BasicDao<User, Long> {}
```

- 5.1 通过Java提供的范型方法可以拿到ENTITY的实际类型为User,ID的实际类型为Long
   - UserDao.class.getGenericInterfaces()可以得到type = org.oasis.easy.orm.dao.BasicDao<org.oasis.easy.orm.model.User, java.lang.Long>
   - ((ParameterizedType)type).getActualTypeArguments()可以得到类型数组\[org.oasis.easy.orm.model.User, java.lang.Long\]
   - (Class) parameterizedType.getRawType().getTypeParameters()可以得到泛型类型数组\[ENTITY,ID\]
   - ENTITY的实际类型为org.oasis.easy.orm.model.User, ID的实际类型为java.lang.Long
   
- 5.2 根据约定,知道这条语句应当是insert
   ```
   String[] OPERATION_PREFIX_SELECT = {"get", "find", "query", "count", "select"};
   String[] OPERATION_PREFIX_INSERT = {"save", "insert"};
   String[] OPERATION_PREFIX_DELETE = {"delete", "remove"};
   String[] OPERATION_PREFIX_UPDATE = {"update", "modify"};
   ```
- 5.3 获取表的名字
   - 如果使用@Table注解指定了表名字, 则使用指定的表名字 Table table = clazz.getAnnotation(Table.class)
   - 否则, 表名字按照约定是名字转换为小写字母 + 下划线形式
   ```
   public class User {
     private Long id;

     private String name;

     private Integer age;

     private Integer groupId;
   }
   ```
- 5.4 获取列名字 
  - 通过反射,拿到User类的fields数组 Field[] fields = clazz.getDeclaredFields();
  - 对于使用@Column指定了列名字的字段 field.isAnnotationPresent(Column.class), 使用指定的列名字field.getAnnotation(Column.class) 
  - 对于没有使用@Column注解的field, 列名字是field转换成小写字母+下划线的形式
  
- 5.5 获取方法的参数值
   - invocation handler的invoke方法里面第三个参数就是方法的参数
   ```
   @Override
   public Object invoke(Object proxy, Method method, Object[] args) {
      // 间接调用jdbc操作数据库
   }
   ```