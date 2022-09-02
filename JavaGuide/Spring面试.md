答题技巧：
总：当前问题回答的是哪些具体的点
分：以1，2，3，4，5的方式分细节去描述相关的知识点，如果有哪些点不清楚，直接忽略过去，突出一些二技术名词（核心概念，接口，类，关键方法）
    避重就轻

# 1.Spring IOC的理解，原理与实现？
## 总
控制反转：理论思想，原来的对象是由使用者来进行控制，有了spring后，可以把整个对象交给spring来帮我们进行管理
DI：依赖注入，把对应的属性的值注入到具体的对象中，@Autowired，populateBean完成属性值的注入


容器：存储对象，使用map结构来存储（），在spring中一般存在三级缓存（循环依赖问题），singletonObject存放完整的Bean对象，整个Bean的生命周期，从创建到使用到销毁的过程全部都是由容器管理（Bean的生命周期）

## 分
1.一般聊到ioc容器的时候要涉及到容器的创建过程（beanFactory,DefaultListableBeanFactory）
    ioc容器有一个最上层的根接口叫做beanFactory，它里面只是一个接口，没有提供对应的子类实现，我们在实际中最普遍调用的就是DefaultListableBeanFactory，我们在使用的时候也会优先创建bean工厂，向bean工厂中设置一些参数（BeanPostProcessor，Aware接口的子类)等等属性
2.加载解析bean对象，准备要创建的bean对象的定义对象beanDefinition(xml或者注解的解析过程)
3.beanFactoryPostProcessor的处理，此处是扩展点，PlaceHolderConfigureSupport,ConfigurationClassPostProcessor
4.BeanPostProcessor的注册功能，方便后续对bean对象完成具体的扩展功能
5.通过反射的方式将BeanDefinition对象实例化成具体的bean对象。
6.bean对象的初始化过程（填充属性，调用aware子类的方法，调用BeanPostProcessor前置处理方法，调用init-method方法，调用BeanPostProcessor的后置处理方法）
7.生成完整的bean对象，通过getBean方法可以直接获取
8.销毁过程


![](images/2022-09-02-12-03-41.png)


# 2.谈一下IOC的底层实现
底层实现：工作原理，过程，数据结构，流程，设计模式，设计思想
你对他的理解，和你了解过的实现过程
反射，工厂，设计模式 关键的几个方法
1.先通过createBeanFactory创建出一个Bean工厂（DefaultListableBeanFactory）
2.开始循环创建对象，因为容器中的bean默认是单例的，所有优先通过getBean,doGetBean从容器中找，找不到的话
3.通过createBean，doCreateBean方法，以反射的方式创建对象，一般情况下使用的是无参的构造（getDeclaredConstructor，newInstance）
4.进行对象的属性填充populateBean
5.进行其他的初始化操作（initializingBean）



# 3.描述一下bean的生命周期？
![](images/2022-09-02-13-35-34.png)

# 4.Spring是如何解决循环依赖的问题？
# 执行顺序
https://blog.csdn.net/sunforraining/article/details/79008171

[静态变量的循环依赖]https://www.cnblogs.com/jason1990/p/11856296.html
https://www.cnblogs.com/leiqiannian/p/7922824.html#:~:text=%E4%B8%80%E8%88%AC%E9%A1%BA%E5%BA%8F%EF%BC%9A%E9%9D%99%E6%80%81%E5%9D%97%EF%BC%88%E9%9D%99%E6%80%81%E5%8F%98%E9%87%8F%EF%BC%89%E2%80%94%E2%80%94%3E%E6%88%90%E5%91%98%E5%8F%98%E9%87%8F%E2%80%94%E2%80%94%3E%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95%E2%80%94%E2%80%94%3E%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95%201%E3%80%81%E9%9D%99%E6%80%81%E4%BB%A3%E7%A0%81%E5%9D%97%EF%BC%88%E5%8F%AA%E5%8A%A0%E8%BD%BD%E4%B8%80%E6%AC%A1%EF%BC%89%202%E3%80%81%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95%EF%BC%88%E5%88%9B%E5%BB%BA%E4%B8%80%E4%B8%AA%E5%AE%9E%E4%BE%8B%E5%B0%B1%E5%8A%A0%E8%BD%BD%E4%B8%80%E6%AC%A1%EF%BC%893%E3%80%81%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95%E9%9C%80%E8%A6%81%E8%B0%83%E7%94%A8%E6%89%8D%E4%BC%9A%E6%89%A7%E8%A1%8C%EF%BC%8C%E6%89%80%E4%BB%A5%E6%9C%80%E5%90%8E%E7%BB%93%E6%9E%9C%E6%B2%A1%E6%9C%89e%20public%20class%20Print%20%7B%20public,System.%20out.print%20%28s%20%2B%20%22%20%22%29%3B%20%7D%20%7D
如果类还没有被加载： 
1、先执行父类的静态代码块和静态变量初始化，并且静态代码块和静态变量的执行顺序只跟代码中出现的顺序有关。 
2、执行子类的静态代码块和静态变量初始化。 
3、执行父类的实例变量初始化 
4、执行父类的构造函数 
5、执行子类的实例变量初始化 
6、执行子类的构造函数 
如果类已经被加载： 
则静态代码块和静态变量就不用重复执行，再创建类对象时，只执行与实例相关的变量初始化和构造方法。



# 4.Spring是如何解决循环依赖的问题的？
三级缓存，提前暴露对象，aop
总：什么是循环依赖问题,A依赖B，B依赖A
分：先说明bean的创建过程：实例化、初始化（填充属性）
    1、先创建A对象，实例化A对象，此时A对象中的b属性为空，填充属性b
    2、从容器中查找B对象，如果找到了，直接赋值不存在循环依赖问题，找不到直接创建B对象
    3、实例化B对象，此时B对象中的a属性为空，填充属性a
    4、从容器中查找A对象，找不到，直接创建
    形成闭环的原因

    此时，如果仔细琢磨的化，会发现A对象是存在的，只不过此时的A对象不是一个完整的状态，只完成了实例化但是未完成初始化，如果在程序调用过程中，拥有了某个对象的引用，能否在后期给他完成复制操作，可以优先把非完整状态的对象优先赋值，等待后续操作来完成赋值，相当于提前暴露了某个不完整对象的引用，相当于提前暴露了某个不完整对象的引用，所以解决问题的核心在于实例化和初始化分开操作，这也是解决循环依赖问题的关键，
    当所有的对象都完成实例化和初始化操作之后，还要把完整对象放到容器中，此时在容器中存在对象的几个状态，完成实例化但未完成初始化，完整状态，因为都在容器中，所以要使用不同的map结构来进行存储，此时就有了一级缓存和二级缓存，如果一级缓存中有了，那么二级缓存中就不会存在同名的对象，因为它们的查找顺序是1、2、3这样的方式来查找的。一级缓存中放的是完整的对象，二级缓存中放的是非完整对象

       为什么需要三级缓存？三级缓存的value类型是ObjectFactory，是一个函数式接口，存在的意义是保证在整个容器的运行过程中同名的bean对象只能有1个
    
       如果一个对象需要被代理，或者说需要生成代理对象，那么要不要优先生成一个普通对象？ 要
       
       普通对象和代理对象是不能同时出现在容器中的，因此当一个对象需要被代理的时候，就要使用代理对象覆盖掉之前的普通对象，在实际的调用过程中，是没有办法确定什么时候对象被使用，所以就要求当某个对象被调用的时候，优先判断此对象是否需要被代理，类似一种回调机制的实现，因此传入lambda表达式的时候，可以通过lambda表达式来执行对象的覆盖过程，getEarlyBeanReference()

       因此，所有的bean对象在创建的时候都要优先放到三级缓存中，在后续的使用过程中，如果需要被代理则返回代理对象，如果不需要被代理，则直接返回普通对象。

## 4.1 缓存的放置时间和删除时间
    三级缓存：createBeanInstance之后，addSingletonFactory
    二级缓存：第一次从三级缓存确定对象是代理对象还是普通对象，同时删除三级缓存getSingleton
    一级缓存：生成完整对象之后放到一级缓存，删除二三级缓存：addSingleton



# 5.BeanFactory与FactoryBean有什么区别？
 ==相同点==：都是用来创建bean对象的
 ==不同点==：使用BeanFactory创建对象的时候，必须要遵循严格的生命周期流程，太复杂了；如果想要简单的定义某个对象的创建，同时创建完成的对象想交给spring来管理，那么就需要实现FactoryBean接口了
    isSingleton:是否是单例对象
    getObjectType:获取返回对象的类型
    getObject：自定义创建的过程(new,反射，动态代理)
不管最终用哪种方式创建，最后创建的对象都会由spring管理
 

# 6. Spring中用到的设计模式？
单例模式：bean默认都是单例的
原型模式：指定作用域为prototype
工厂模式：BeanFactory
模板方法：postProcessBeanFactory,onFresh,initPropertyValue
策略模式：XmlBeanDefinitionReader,PropertiesBeanDefinitionReader
观察者模式：listener,event,multicast
适配器模式：Adapter
装饰者模式：BeanWrapper
责任链模式：使用AOP的时候会先生成一个拦截器
代理模式：动态代理
委托者模式：delegate


# 7. Spring的AOP的底层实现原理？
aop是ioc的一个扩展功能， 先有的ioc，再有的aop，只是在ioc的整个流程中新增的一个扩展点：BeanPostProcessor
总：aop概念，应用场景（事务，日志），实现用的动态代理
分：bean的创建过程中有一个步骤可以对bean进行扩展实现，aop本身就是一个扩展功能，所以在BeanPostProcessor的后置方法中来进行实现
    1.代理对象的创建过程（advice，切面，切点）
    2.通过jdk或cglib的方式来生成代理对象