# http
应用层协议
客户端和服务端进行交互的一个规范
https://www.cnblogs.com/r1-12king/p/15878799.html

https://zhuanlan.zhihu.com/p/97682152

url与http协议
https://blog.csdn.net/qq_36722887/article/details/115617765
三大部分：
    起始行：
    头部字段集合:

    消息正文


[解决servlet中post请求和get请求中文乱码]https://blog.csdn.net/qq_53037676/article/details/125907391

https://blog.csdn.net/qq_31255557/article/details/119874196

cmd默认是GBK编码，所以fileencoding需要用GBK
TOMCAT


# 编码问题
测试代码
```java
import java.util.*;
public class demo {
    public static void main(String[] args) {
        System.out.println("啊实打实");
        System.out.println(System.getProperty("file.encoding"));
        Scanner sc=new Scanner(System.in);
        String s=sc.nextLine();
        System.out.println(s);
    }
}
```
编译器拿到Java文件的二进制流
javac -encoding xxx demo.java  告诉编译器java文件是什么方式进行编码的 
编译器对java文件进行解码后编译为class文件（UTF-8）
JVM虚拟机内是UTF-16

gbk和utf-8 对英文无影响都是ASCII码


file.encoding是JVM读取和写入文件的编码

IDEA控制台 encoding需要和vm的fileencoding保持一致

https://www.jianshu.com/p/da377fc821a8
https://blog.csdn.net/qq_51409098/article/details/126438148