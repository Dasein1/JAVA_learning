public class Main{

public static void main(String[] args){

 new Task1().doCheck();

new Task2().doCheck();

}

}







abstract class Check{

 public abstract void step1();

public abstract void step2();

 public void step3(){

 System.out.println("提交巡查结果");

};

final public void doCheck(){

 this.step1();

this.step2();

this.step3();

}

}



class Task1 extends Check{

@Override

 public void step1(){

 System.out.println("现场扫描视频");

}



@Override

public void step2(){

System.out.println("巡检机房的服务器的状态");

}

}



class Task2 extends Check{

@Override

public void step1(){

System.out.println("远程视频");

}



@Override

public void step2(){

 System.out.println("巡检课堂的学生出勤率");

}

}