# 44.数字序列中某一位的数字
![](images/2022-02-22-15-32-26.png)
```java
class Solution {
    public int findNthDigit(int n) {
        long start=1;  //位数范围的start
        long end;     //位数范围的end
        int i=1;  //整数的位数
        while(true){
            end=(int)(i*9*Math.pow(10,i-1))+start-1;  //Math开头大写，方法参数的数据类型都为double，返回值也为double
            if (n>=start && n<=end){
                break;
            }
            else{
                start=end+1;
                i++;
            }
        }
        long nn=n;
        int kth=(int)(n-start);
        int shang=kth/i;
        int yu=kth%i;
        // 得到第n位数所在的整数
        int number=(int)(Math.pow(10,i-1))+shang;
        // 第n位对应的数字即为整数number的倒数第n_位
        int n_=i-yu;
        // 用末位取余法得到res
        int res=0;
        for(int j=0;j<n_;j++){
            res=number%10;
            number/=10;
        }
        return res;
    }
}
```
## 思路：
    1.k位数在题目中的字符序列中有各自的位数范围，根据数学规律找到n在哪个范围内[start,end]
    2.找到第n位所在的整数
    3.找到第n位对应的数字
## 问题：
    1.如果 start和end用int类型，那么当n=1000000000，执行代码时它们会溢出。
    2.如何取得整数的每一位？
    3.一个数的次方



# 56 - I.数组中数字出现的次数
![](images/2022-02-23-16-37-54.png)

## 思路：
    1.简化为除1个数字之外，其他数字都出现了两次的问题，发现对数组中所有数异或运算后结果为那个只出现了1次的数字。
    2.根据该规律将题目中的数组拆分为两个子数组，每个子数组包含一个只出现了一次的数组
      如何进行拆分呢？
      假设只出现了1次数字为a,b，那么找到a,b的二进制数哪一位不同就可以依次进行分组计算异或。




# 45.把数组排成最小的数
![](images/2022-02-24-23-33-56.png)
![](images/2022-02-24-23-37-18.png)
算法流程：
1.int数组转化为String数组
2.自定义compare方法比较两个字符串前后顺序
3.对String数组使用快速排序方法

## 问题：
1.int类型怎么转换为String？
2.string.compareTo()方法的使用
3.字符串数组怎么连接成一个字符串？


## 代码：
```java
class Solution {
    public String minNumber(int[] nums) {
        String[] numbers=new String[nums.length];
        StringBuilder minnum=new StringBuilder();
        for (int i=0;i<nums.length;i++){
            numbers[i]=String.valueOf(nums[i]);
        }
        quickSort(numbers,0,nums.length-1);
        for(String x:numbers){
            minnum.append(x);
        }
        return minnum.toString();
    }
    void quickSort(String[] nums,int low,int high){
        if (low>=high){
            return;
        }
        int i=low;
        int j=high;
        String tmp=null;
        while(i<j){
            while(compare(nums[low],nums[j])<=0&i<j){
                j--;
            }
            while(compare(nums[low],nums[i])>=0&i<j){
                i++;
            }
            tmp=nums[i];
            nums[i]=nums[j];
            nums[j]=tmp;
        }
        tmp=nums[low];
        nums[low]=nums[j];
        nums[j]=tmp;
        quickSort(nums,low,j-1);
        quickSort(nums,j+1,high);
    }
    int compare(String m,String n){
        String mn=m+n;
        String nm=n+m;
        return mn.compareTo(nm);
    }

}

```



# 56 - II.数组中数字出现的数字 II
![](images/2022-02-24-16-26-12.png)
## 思路：
### 哈希表
    1.创建一个空的哈希表
    2.如果不存在该键，则值为1，否则值+1
    3.遍历哈希表，找到值为1的键（有比遍历更好的方法吗）

### 位运算 自动状态机




# 46.把数组翻译成字符串
![](images/2022-02-28-16-03-15.png)
## 1.深度优先搜索
### 思路：
1）在for循环中递归
2）满足终止条件时用count计数


### 代码：
```java
class Solution {
    int count=0;
    public int translateNum(int num) {
        String number = String.valueOf(num);
        backtracking(0,number);
        return count;
    }
    void backtracking(int k,String number){
        String subnumber;
        if (k>=number.length()-1){
            count+=1;
            return;
        }
        for(int i=k+1;i<=k+2;i++){
            subnumber=number.substring(k,i);
            if (Integer.valueOf(subnumber)>=0 && Integer.valueOf(subnumber)<=25){
                backtracking(i,number);
            }
            if (subnumber.equals("0"))  break;
        }
    }
}
```


## 2.动态规划
### 代码：
```java
class Solution {
    public int translateNum(int num) {
        String number = String.valueOf(num);
        if (number.length()==1){
            return 1;
        }
        int[] dp=new int[number.length()+1];
        int tmp;
        dp[0]=1;
        dp[1]=1;
        for(int i=2;i<=number.length();i++){
            if (number.substring(i-2,i-1).equals("0")){
                dp[i]=dp[i-1];
                continue;
            }
            tmp=Integer.valueOf(number.substring(i-2,i));
            if(tmp>=0 && tmp <=25){
                dp[i]=dp[i-2]+dp[i-1];
            }
            else{
                dp[i]=dp[i-1];
            }
        }
        return dp[number.length()];
    }   
}
```

### 思路：
到第i个数字的翻译方法数与到第i-1个和第i-2个的有关，寻找它们之间的联系

## 问题：
1.解题中对int num的处理：
转换为String，利用string.substring方法取数，利用string.equals()方法与0和25作比较。
2.当第i-1个数为0时，无法与第i个数连接翻译。