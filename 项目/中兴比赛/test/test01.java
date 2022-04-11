// import java.io.BufferedWriter;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.text.DecimalFormat;
// import java.util.List;

// public class test01 {
//     public static void main(String[] args) {
//         DecimalFormat dec = new DecimalFormat("0.0000");
//         double[] start={45.73, 45.26, 0};
//         double[][] end={{1200, 700, 0},{-940, 1100, 0}};
//         double[] t_start={0, 4.7, 16.4};
//         int index=1;
//         // for(double[] cur_end :end){     
//         //     for(double cur_t_start:t_start){
//         //         Solution solution=new Solution(start, cur_end, 5, 10, 90, 80, 125, 70, 0.1, cur_t_start);
//         //         int[] first_node=solution.start_firstnode_search();
//         //         solution.transfer(first_node);
//         //         List<double[]> pp=solution.path;
//         //         System.out.println(dec.format(cur_t_start)+","+"0,"+index+","+dec.format(solution.t_end)+",1");
//         //         for(double[] x:pp){
//         //             System.out.println("("+dec.format(x[0])+","+((int)x[1])+","+((int)x[2])+")");
//         //         }
//         //     }
//         //     index++;
//         // }

//         try {
//             BufferedWriter out = new BufferedWriter(new FileWriter("result.txt"));
//             for(double[] cur_end :end){     
//                 for(double cur_t_start:t_start){
//                     Solution solution=new Solution(start, cur_end, 5, 10, 90, 80, 125, 70, 0.1, cur_t_start);
//                     int[] first_node=solution.start_firstnode_search();
//                     solution.transfer(first_node);
//                     List<double[]> pp=solution.path;
//                     out.write(dec.format(cur_t_start)+","+"0,"+index+","+dec.format(solution.t_end)+",1");
//                     out.newLine();
//                     for(double[] x:pp){
//                         out.write("("+dec.format(x[0])+","+((int)x[1])+","+((int)x[2])+")");
//                         out.newLine();
//                     }
//                 }
//                 index++;
//             }
//             out.close();
//         } catch (IOException e) {
//         }

        

//     }
// }
