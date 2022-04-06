package synch;


import java.util.*;
import java.util.concurrent.locks.*;

public class Bank{
    // 用户数
    private final double[] accounts;

    // 锁对象
    private Lock bankLock;
    // 条件对象
    private Condition sufficientFunds;

    // 初始化余额
    public Bank(int n,double initialBalance){
        accounts = new double[n];
        Arrays.fill(accounts,initialBalance);
        bankLock=new ReentrantLock();
        sufficientFunds=bankLock.newCondition();
    }

    
    public void transfer(int from,int to,double amount) throws InterruptedException{
        bankLock.lock();
        try{
            while(accounts[from]<amount) 
                sufficientFunds.await();
            System.out.print(Thread.currentThread());
            accounts[from]-=amount;
            System.out.printf(" %10.2f from %d to %d", amount, from, to);
            accounts[to]+=amount;
            System.out.printf(" Total Balance: %10.2f%n", getTotalBalance());
            sufficientFunds.signalAll();
        }
        finally{
            bankLock.unlock();
        }
    }

    public double getTotalBalance(){
        bankLock.lock();
        try{
            double sum=0;
            for(double a:accounts)
                sum+=a;
            
            return sum;
        }
        finally{
            bankLock.unlock();
        }
    }

    public int size(){
        return accounts.length;
    }
}