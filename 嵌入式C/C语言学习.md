# EOF怎么输入
在windows中是ctrl+z
在Linux中是ctrl+d

# getchar和putchar
getchar返回输入字符的ASCII码，中文会乱码
![](images/2022-11-28-15-28-16.png)

![](images/2022-11-28-15-40-44.png)

## 实验
[getchar函数()详解](https://blog.csdn.net/qq_15719613/article/details/120962669)
![](images/2022-11-28-16-23-14.png)
![](images/2022-11-28-16-22-45.png)


# 数组
[C语言中变量不初始化会怎么样？](https://blog.csdn.net/weixin_74195551/article/details/127328173)
[数组的使用](https://blog.csdn.net/hwx1546/article/details/123190222)
## 数组初始化设置
数字数组
声明同时赋值：没赋值的设0
只声明：设置随机数
声明后再赋值需要逐个赋值





## 字符数组
strlen()  计算字符串长度直到空结束符，但不包括空结束符
[C语言字符数组和字符串详解](http://c.biancheng.net/view/1832.html)
### 1.字符串结束标志：'\0'
#### 1.1 是什么？
ASCII码表中的第0个字符，nul，既不能显示，也没有控制功能，输出没有效果
#### 1.2 有什么用？
C语言处理字符串时，从前往后逐个扫描字符，一旦遇到'\0'就认为到达了字符串的末尾，结束处理。

#### 1.3 在字符串和字符数组中的应用
    1.""包围的字符串自动在末尾加'/0'
        如果char[n],n小于定义的字符串"xxx"长度，例，char[3]="ab",则内存长度sizeof为3，strlen为2,末尾会自动添加'\0'
        如果char[3]="abc",则内存长度sizeof为3,strlen也为3，末尾添加'\0';
    2.逐个字符给数组赋值不会自动添加'\0'
        例如 char str[]={'a','b','c'};
        但是 char str[4]={'a','b','c'}会自动添加'/0'
[如果数组末尾没有'\0'可能出现的问题](https://blog.csdn.net/m0_69951061/article/details/125406225)


### 2.字符数组的初始化
[windows和linux下置0](https://blog.csdn.net/tesla777/article/details/127689070)


### 3.创建数组的方式
#### 3.1声明时赋值
##### 3.1.1 声明长度
char str[30] = {"c.biancheng.net"};
char str[30] = "c.biancheng.net";  //这种形式更加简洁，实际开发中常用
##### 3.1.2 不声明长度
char str[] = {"c.biancheng.net"};
char str[] = "c.biancheng.net";  

#### 3.2 声明后赋值
```C
char str[7];
str = "abc123";  //错误
//正确
str[0] = 'a'; str[1] = 'b'; str[2] = 'c';
str[3] = '1'; str[4] = '2'; str[5] = '3';
```
必须逐个赋值


# 函数
![](images/2022-11-28-17-55-29.png)

# '/'反斜杠

# case
[switch case语句不加break的后果](https://blog.csdn.net/qq_39478139/article/details/108446550)


[C语言中void*详解及应用](https://www.runoob.com/w3cnote/c-void-intro.html)

[声明和定义](https://blog.csdn.net/weixin_28876083/article/details/117009456)
声明包含定义


# 4 函数与程序结构
## 4.6  静态变量
### static修饰外部变量后，该变量不能被其他源文件声明
![](images/2022-11-30-21-01-58.png)
![](images/2022-11-30-21-02-16.png)
![](images/2022-11-30-21-04-08.png)

### static用于修饰函数，该函数不能被其他源文件声明
![](images/2022-11-30-21-21-06.png)
![](images/2022-11-30-21-21-27.png)
![](images/2022-11-30-21-21-43.png)

### static修饰内部变量，可使其一直占据存储空间，不随函数的被调用和退出而存在和消失
