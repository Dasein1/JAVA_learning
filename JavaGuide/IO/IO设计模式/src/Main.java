import java.util.*;

public class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int N=sc.nextInt();
        long[] x=new long[N];
        long[] r=new long[N];
        for(int i=0;i<N;i++){
            x[i]=sc.nextLong();
        }
        long result_1=0;
        long result_2=0;
        for(int k=0;k<N;k++){
            r[k]=getResult1(N,x,k);
        }
        for(int k=0;k<N;k++){
            r[k]=r[k]+getResult2(N,x,k);
        }

        long absMax=0;
        long result=0;
        for(int i=1;i<N;i++){
            if(Math.abs(r[i])>absMax){
                result=Math.abs(r[i]);
            }
        }
        for(long value:r){
            System.out.printf(value+" ");
        }
        System.out.println(result);



    }

    public static long getResult1(int N,long[] x,int k){
        long sum=0;
        for(int i=0;i+k<N;i++){
            sum=sum+x[i]*x[i+k];
        }
        return sum;
    }

    public static long getResult2(int N,long[] x,int k){
        long sum=0;
        for(int i=0;i<k+1;i++){
            sum=sum+x[i]*x[i+N-1-k];
        }
        return sum;
    }

}

