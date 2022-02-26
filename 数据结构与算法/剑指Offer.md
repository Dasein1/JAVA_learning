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
