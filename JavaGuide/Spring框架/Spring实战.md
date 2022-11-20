https://www.bilibili.com/video/BV1rt4y1u7q5?p=9&spm_id_from=pageDriver&vd_source=7116afddac4acaa2875e31b432e7f0ed
黑马

# BeanFactory与ApplicationContext
## 他们之间的关系
![](images/2022-11-16-15-58-08.png)
![](images/2022-11-16-16-03-55.png)
![](images/2022-11-16-16-03-11.png)
![](images/2022-11-16-16-05-27.png)

## ApplicationContext常用
![](images/2022-11-16-16-11-27.png)
![](images/2022-11-16-16-12-49.png)
![](images/2022-11-16-16-13-34.png)

# 基于xml的Spring应用
## SpringBean的配置详解
![](images/2022-11-16-16-24-41.png)
## beanName的设置    name
1.name="aaa,bbb,ccc" 设置多个别名，容器可通过多个别名获，存在beanfactory的aliasmap里
2.id="userService" 设置id，容器可以通过id名获得，如单例下存在beanfactory的singletonObjects对象里的table（是一个ConcurrentHashMap）可以找到
3.当没有设置id的时候，table里会存了全限定名的key，容器可以通过getBean该key得到bean
总结：配了id的时候，id就是beanName，没有配id配了别名的时候，beanName就是所有别名中的第一个，如果都没有配的话，beanName就是全限定名
## Bean的作用域配置   scope
![](images/2022-11-16-16-46-32.png)
配置了spring-webmvc后，作用域还有request和session

## Bean的延迟加载    lazy-init
![](images/2022-11-16-16-56-44.png)
对ApplicationContext有效，对BeanFactory无效

## Bean的初始化和销毁方法配置  init-method/destroy-method
![](images/2022-11-16-17-09-23.png)
bean对象实例化后会执行init-method方法
ApplicationContext接口没有close方法，ClassPathXmlApplicationContext对象有，执行close方法后会有调用destroy-method方法
### 通过实现InitializingBean接口，完成Bean的初始化操作
执行时机早于init-method方法晚于属性设置
![](images/2022-11-16-17-22-48.png)
![](images/2022-11-16-17-33-31.png)

## Bean的实例化配置
![](images/2022-11-16-17-36-17.png)
![](images/2022-11-16-17-37-06.png)
![](images/2022-11-16-17-46-54.png)
使用工厂方法实例化时，容器会将配置的bean工厂方法的返回值作为beanid指向的value。
![](images/2022-11-16-19-07-58.png)
### 实现FactoryBean规范延迟实例化Bean
![](images/2022-11-16-19-18-43.png)
![](images/2022-11-16-19-18-31.png)
![](images/2022-11-16-19-19-12.png)
注意：
![](images/2022-11-16-19-19-44.png)
beanfactory的sigletonObject的table里存的是FactoryBean对象，而不是我们想要的DaoImpl
在哪里呢？执行getBean前后对比
![](images/2022-11-16-19-21-27.png)
![](images/2022-11-16-19-21-43.png)
getBean前容器里只有FactoryBean对象，要用的时候再去创建对象并缓存在factoryBeanObjectCache里（所以说有延迟功能）

## Bean的依赖注入配置
![](images/2022-11-16-19-27-04.png)
![](images/2022-11-16-19-44-11.png)
### 自动装配方式 autowire=byName/byType
![](images/2022-11-16-20-08-44.png)
注意：byType是和setXXX(UserDao userdao)中的UserDao参数去匹配的
![](images/2022-11-16-20-23-27.png)
这样写是可以成功注入的

## 命名空间的种类
![](images/2022-11-16-20-28-01.png)
![](images/2022-11-16-20-28-18.png)

## beans的profile属性切换环境
![](images/2022-11-16-20-34-05.png)


## import标签
![](images/2022-11-16-20-54-58.png)

## alias标签
![](images/2022-11-16-20-57-11.png)

## 自定义命名空间标签的使用步骤

# Spring的get方法
![](images/2022-11-16-21-09-52.png)

# Spring配置非自定义Bean
![](images/2022-11-16-21-14-21.png)
## 配置DruidDataSource
![](images/2022-11-20-16-58-20.png)
找到DruidDataSource需要配置的属性
![](images/2022-11-20-16-59-27.png)
![](images/2022-11-20-17-17-08.png)

## 配置MyBatis的SqlSessionFactory交由Spring管理
![](images/2022-11-20-19-46-17.png)




# SpringBean的实例化流程
![](images/2022-11-16-21-18-59.png)
![](images/2022-11-16-21-30-49.png)
![](images/2022-11-16-21-31-44.png)


# Spring的后置处理器
BeanFactoryPostProcessor的postProcessBeanFactory方法
BeanDefinitionPostPorcessor的postProcessBeanDefinitionRegistry方法和postProcessBeanFactory方法
## BeanFactory后置处理器
![](images/2022-11-16-21-34-14.png)
![](images/2022-11-16-22-18-59.png)
![](images/2022-11-16-22-19-41.png)
![](images/2022-11-16-22-17-30.png)
![](images/2022-11-16-22-37-51.png)
## Spring实例化过程中的体现
![](images/2022-11-16-22-40-20.png)

## 使用Spring的BeanFactoryPostProcessor扩展点完成自定义注解扫描
![](images/2022-11-16-23-00-56.png)

## Bean后置处理器
![](images/2022-11-16-23-07-06.png)
![](images/2022-11-16-23-12-33.png)

## 案例：对Bean方法执行时间日志增强
![](images/2022-11-16-23-43-14.png)
![](images/2022-11-17-00-29-19.png)
### 报错出现
![](images/2022-11-17-00-30-33.png)
分析：
当使用动态代理后，返回的是代理对象，该对象无法调用init方法，因为JDK代理只能代理接口方法。而init-method的实现原理是容器在初始化阶段去调用init-method。

## 完善实例化过程
![](images/2022-11-17-10-18-44.png)
![](images/2022-11-17-10-21-14.png)

## Bean的生命周期
![](images/2022-11-17-10-20-19.png)
### SpringBean的初始化过程
![](images/2022-11-20-18-06-33.png)
### Bean实例属性填充
![](images/2022-11-20-18-18-28.png)
![](images/2022-11-20-18-21-38.png)
#### 循环依赖
![](images/2022-11-20-18-31-28.png)
![](images/2022-11-20-18-34-40.png)
![](images/2022-11-20-18-38-15.png)
初次实例化bean后，包一层变为ObjectFactory（通过getObject返回该实例化bean）放到容器中
![](images/2022-11-20-18-48-03.png)
![](images/2022-11-20-18-50-50.png)
三级缓存流程图
![](images/2022-11-20-18-51-21.png)

### Aware接口
![](images/2022-11-20-19-05-00.png)

# Spring整合第三方框架
![](images/2022-11-20-19-08-23.png)

### 单独MyBatis
1.配置porm文件导入mybatis jar包
![](images/2022-11-20-19-28-27.png)

查看mybatis官方文档
2.创建XML，构建SqlSessionFactory
![](images/2022-11-20-20-25-35.png)
配置datasource

3.创建xml关联mapper
![](images/2022-11-20-20-27-08.png)
xml所在包名和接口代码所在包名一致（注意用/才能创建多级文件夹，而不是.）

4.测试
![](images/2022-11-20-19-30-27.png)

## 整合MyBatis
![](images/2022-11-20-19-11-02.png)
![](images/2022-11-20-22-10-07.png)

# SpringAOP
![](images/2022-11-17-10-25-32.png)
## AOP相关概念
![](images/2022-11-17-14-04-57.png)
## 基于XML配置的AOP
![](images/2022-11-17-14-08-06.png)
![](images/2022-11-17-14-11-04.png)
![](images/2022-11-17-14-13-13.png)
![](images/2022-11-17-14-43-04.png)
### 切点表达式配置方式
![](images/2022-11-17-14-45-46.png)
### 切点表达式的配置语法
![](images/2022-11-17-14-46-26.png)
![](images/2022-11-17-14-47-27.png)