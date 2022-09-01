public class Test {
    public static void main(String[] args) {
        A a=new A() {
        };
        a.hashCode();
    }
}
abstract class A{
    int a=0;
}

class B extends A{

}
