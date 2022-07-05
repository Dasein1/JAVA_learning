# 1.travel plan
```java
import java.util.Scanner;

public class Main{

    static int N=510;
    static int[][] g=new int[N][N];//稠密图用邻接矩阵
    static int[] dist=new int[N];//记录当前最优距离
    static boolean[] visw=new boolean[N];//是否确定最短
    static int n,m;
    static int INF=10010;

    /*
   该算法主要由三步组成：
   1.迭代n次 以保证所有点都被找到
   2.寻找未确定的最小边(初始化起点为0，其他为正无穷，所以肯定是先找起点,
       之后就是在更新了距离的点中找)
       找到最小边之后，visw[t]表示到t这条边已确定
   3.寻找t-j的最短距离 比较(1-t)+(t-j)和1-j哪条边最小 做取舍
   */
    public static int dijkstra(){
        for(int i=1;i<=n;i++) dist[i]=INF;
        dist[1]=0;//初始化起点
        for(int i=0;i<n;i++){//1.迭代n次 有n个点 就要找n次最小边
            //2.寻找未确定的最小边
            int t=-1;
            for(int j=1;j<=n;j++){
                if(!visw[j]&&(t==-1||dist[j]<dist[t]) ){//遇到小于dist[t]的边就更新
                    t=j;
                }
            }
            visw[t]=true;
            //3.寻找t-j的最短距离
            for(int j=1;j<=n;j++){
                dist[j]=Math.min(dist[j],dist[t]+g[t][j]);
            }
        }
        if(dist[n]==INF) return -1;
        else return dist[n];
    }

    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        for(int i=1;i<=n;i++){
            for(int j=1;j<=n;j++){
                if(i==j) g[i][j]=0;
                else g[i][j]=INF;
            }
        }

        for(int i=0;i<m;i++){
            int x=sc.nextInt(),
                    y=sc.nextInt(),
                    z=sc.nextInt();
            g[x][y]=Math.min(g[x][y],z);//有重边 选择最短的x-y
        }

        System.out.println(dijkstra());
    }

}
```


# 2.running game
```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main{

    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int x=sc.nextInt();
        int y=sc.nextInt();
        int z=sc.nextInt();

        Solution solution=new Solution();
        solution.search(x,y,z,0);


        System.out.println(solution.result);
    }

}


class Solution{
    public int result=0;
    public void search(int start,int end,int z,int step){
        Queue<Integer> queue=new LinkedList<>();
        queue.add(start);
        boolean find=false;
        while(queue.size()>0){
            step++;
            int length=queue.size();
            for(int i=0;i<length;i++){
                int cur=queue.poll();
                if(cur!=end){
                    queue.add(cur+1);
                    queue.add(cur-1);
                    queue.add(cur*3);
                }
                else{
                    result=step-1;
                    find=true;
                }
            }
            if(find==true){
                break;
            }
        }
        return;


    }
}
```

# 3.合法公式
```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main{

    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        String string=sc.nextLine();

        Solution solution=new Solution();



        if(solution.judge(string)) {
            System.out.println("valid");
        }
        else{
            System.out.println("invalid");
        }
    }

}


class Solution {
    public int result = 0;

    public boolean judge(String string) {
        int kuohao = 0;
        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (kuohao == 0) {
                if (i == 0) {
                    if ((string.charAt(i) < 'a'|| string.charAt(i) > 'z') && string.charAt(i) != '(') {
                        return false;
                    } else if (string.charAt(i) == '(') {
                        kuohao++;
                    }
                } else if (string.charAt(i - 1) == '+' || string.charAt(i - 1) == '-') {
                    if ((string.charAt(i) < 'a' || string.charAt(i) > 'z') && string.charAt(i) != '(') {
                        return false;
                    } else if (string.charAt(i) == '(') {
                        kuohao++;
                    }
                    }
                else if (string.charAt(i - 1) >= 'a' && string.charAt(i - 1) <= 'z') {
                    if (string.charAt(i) != '+' && string.charAt(i) != '-') {
                        return false;
                    } else if (string.charAt(i - 1) == ')') {
                        if (string.charAt(i) != '+' && string.charAt(i) != '-') {
                            return false;
                        }
                    }
                }
                else {
                    return false;
                }

            }
            else {
                if (string.charAt(i - 1) >= 'a' && string.charAt(i - 1) <= 'z') {
                    if (string.charAt(i) != '+' && string.charAt(i) != '-' && string.charAt(i) != ')') {
                        return false;
                    } else {
                        if (string.charAt(i) == ')') {
                            kuohao--;
                        }
                    }
                } else if (string.charAt(i - 1) == '+' || string.charAt(i - 1) == '-') {
                    if ((string.charAt(i) < 'a' || string.charAt(i) > 'z') && string.charAt(i) != '(') {
                        return false;
                    } else if (string.charAt(i) == '(') {
                        kuohao++;
                    }
                } else if (string.charAt(i - 1) == '(') {
                    if ((string.charAt(i) < 'a'|| string.charAt(i) > 'z') && string.charAt(i) != '(') {
                        return false;
                    } else if (string.charAt(i) == '(') {
                        kuohao++;
                    }
                }
                else if(string.charAt(i - 1) == ')'){
                    if(string.charAt(i)=='('){
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }
}
```


# 4.好友组
```java
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        UF uf = new UF(N);
        for (int i = 0; i < M; i++) {
            int p = sc.nextInt()-1;
            int q = sc.nextInt()-1;
            if (uf.connected(p, q)) {
                continue;
            }
            uf.union(p, q);
        }
        int[] tmp=new int[N];
        for(int i=0;i<N;i++){
            tmp[i]=uf.find(i);
        }
        HashMap<Integer,Integer> hs = new HashMap<>();
        for(int i=0;i<N;i++){
            if(hs.containsKey(tmp[i])){
                int a=hs.get(tmp[i]);
                hs.put(tmp[i],a+1);
            }
            else{
                hs.put(tmp[i],1);
            }
        }
        int nums=0;
        for(int value: hs.values()){
            if(value==1){
                nums+=1;
            }
        }

        System.out.println(uf.cunt()-nums);
    }
}

class UF{
    private  int[] id;
    private int count;
    public UF(int N){
        count=N;
        id=new int[N];
        for(int i=0;i<N;i++){
            id[i]=i;
        }
    }
    public int cunt(){
        return count;
    }
    public boolean connected(int p,int q){
        return find(p)==find(q);
    }
    public int find(int p){
        return id[p];
    }
    public void union(int p,int q){
        int pID=find(p);
        int qID=find(q);

        if(pID==qID){
            return;
        }
        for(int i=0;i<id.length;i++){
            if(id[i]==pID){
                id[i]=qID;
            }
        }
        count--;
    }
}
```


# 序列评分
```java

```

# MAXRETREAT
```java
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] prices=new int[N];
        for(int i=0;i<N;i++){
            prices[i]=sc.nextInt();
        }
        Solution solution=new Solution();
        System.out.println(solution.maxProfit(prices));

    }
}

//class Solution {
//    public int maxProfit(int[] prices) {
//        int retreat = 0;
//        int n=prices.length;
//        if (n==1){
//            return 0;
//        }
//
//        int[] dp=new int[n];
//        int tmp=0;
//        for(int i=1;i<n;i++){
//            tmp=Math.max(tmp+prices[i-1]-prices[i],prices[i-1]-prices[i]);
//            retreat=Math.max(tmp,retreat);
//        }
//        return retreat;
//    }
//}
//  回退要大于0

class Solution{
    public int maxProfit(int[] prices){
        int n=prices.length;
        int result=0;
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                result=Math.max(prices[i]-prices[j],result);
            }
        }
        return result;
    }
}
```