public class test {
    public static void main(String[] args) {
        System.out.println(get_distance(new double[]{715,129,35},new double[]{7*90,2*80,10}));
        System.out.println(get_distance(new double[]{715,129,35},new double[]{7*90,1*80,10}));
        System.out.println(get_distance(new double[]{715,129,35},new double[]{8*90,2*80,10}));
        System.out.println(get_distance(new double[]{715,129,35},new double[]{8*90,1*80,10}));

        System.out.println(get_distance(new double[]{45.73, 45.26, 0},new double[]{715,129,35}));
        System.out.println(get_distance(new double[]{45.73, 45.26, 0},new double[]{7*90,2*80,10}));
        System.out.println(get_distance(new double[]{45.73, 45.26, 0},new double[]{7*90,1*80,10}));
        System.out.println(get_distance(new double[]{45.73, 45.26, 0},new double[]{8*90,2*80,10}));
        System.out.println(get_distance(new double[]{45.73, 45.26, 0},new double[]{8*90,1*80,10}));
    }

    public static double get_distance(double[] node1, double[] node2) {
        double temp_2=Math.pow(node1[0] - node2[0], 2) + Math.pow(node1[1] - node2[1], 2) + Math.pow(node1[2] - node2[2], 2);
        temp_2=Math.floor(temp_2*2)/2;
        //	Math.floor(Math.sqrt(Math.pow(node1[0] - node2[0], 2) + Math.pow(node1[1] - node2[1], 2) + Math.pow(node1[2] - node2[2], 2))/10000)*10000;
        double tmp=Math.sqrt(temp_2);
        tmp=Math.floor(tmp*2)/2;
        return tmp;
    }
}
