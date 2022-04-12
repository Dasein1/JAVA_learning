
import java.lang.Math;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class Main{
public static void main(String[] args) {
    DecimalFormat dec = new DecimalFormat("0.0000");
    double[] start={45.73, 45.26, 0};
    double[][] end={{1200, 700, 0},{-940, 1100, 0}};
    double[] t_start={0, 4.7, 16.4};
    int index=1;
    // for(double[] cur_end :end){     
    //     for(double cur_t_start:t_start){
    //         Solution solution=new Solution(start, cur_end, 5, 10, 90, 80, 125, 70, 0.1, cur_t_start);
    //         int[] first_node=solution.start_firstnode_search();
    //         solution.transfer(first_node);
    //         List<double[]> pp=solution.path;
    //         System.out.println(dec.format(cur_t_start)+","+"0,"+index+","+dec.format(solution.t_end-cur_t_start));
    //         double[] x=null;
    //         for(int i=0;i<pp.size();i++){
    //             x=pp.get(i);
    //             System.out.print("("+dec.format(x[0])+","+((int)x[1])+","+((int)x[2])+")");
    //             if(i!=pp.size()-1){
    //                 System.out.print(",");
    //             }
    //             else{
    //                 System.out.println("");
    //             }
    //         }
    //     }
    //     index++;
    // }

    try {
        index=1;
        BufferedWriter out = new BufferedWriter(new FileWriter("result.txt"));
        for(double cur_t_start:t_start){     
            for(int j=0;j<end.length;j++){
                Solution solution=new Solution(start, end[j], 5, 10, 90, 80, 125, 70, 0.1, cur_t_start);
                int[] first_node=solution.start_firstnode_search();
                solution.transfer(first_node);
                List<double[]> pp=solution.path;
                out.write(dec.format(cur_t_start)+","+"0,"+(j+1)+","+dec.format(solution.t_end-cur_t_start));
                System.out.println(dec.format(cur_t_start)+","+"0,"+(j+1)+","+dec.format(solution.t_end-cur_t_start));
                out.newLine();
                double[] x=null;
                for(int i=0;i<pp.size();i++){
                    x=pp.get(i);
                    out.write("("+dec.format(x[0])+","+((int)x[1])+","+((int)x[2])+")");
                    System.out.print("("+dec.format(x[0])+","+((int)x[1])+","+((int)x[2])+")");
                    if(i!=pp.size()-1){
                        out.write(",");
                        System.out.print(",");
                    }
                    else{
                        System.out.println("");
                    }
                }
                out.newLine();
            }
            index++;
        }
        out.close();
    } catch (IOException e) {
    }
}

}

class Solution {
    double[] start;
    double[] end;
    double v;
    double H;
    double d_IntraOrbit;
    double d_InterOrbit;
    double d;
    double D;
    double tf;
    double t_start;
    double t_end;
    double t_cur;
    int[] relation = new int[4];
    int[][] search_move = new int[][]{{-1, 1}, {1, 1}, {-1, -1}, {1, -1}};
    int index_relation;
    List<double[]> path = new ArrayList<>();


    public Solution(double[] start,
                    double[] end,
                    double v,
                    double H,
                    double d_IntraOrbit,
                    double d_InterOrbit,
                    double d,
                    double D,
                    double tf,
                    double t_start) {
        this.start = start;
        this.end = end;
        this.v = v;
        this.H = H;
        this.d_IntraOrbit = d_IntraOrbit;
        this.d_InterOrbit = d_InterOrbit;
        this.d = d;
        this.D = D;
        this.tf = tf;
        this.t_start = t_start;
        update_relation(start, end);
        update_start(t_start);
        update_end(t_start);
        this.t_cur = t_start;
    }

    // 根据起点和终点找到起点将转发的第一个无人机
    public int[] start_firstnode_search() {
        double[] tmp_node = new double[]{Math.floor(start[0] / d_IntraOrbit), Math.floor(start[1] / d_InterOrbit), H};
        // System.out.printf(Arrays.toString(tmp_node));
        double[] dxdy = get_dxdy(relation);
        double[] cur_node = new double[]{tmp_node[0] + dxdy[0], tmp_node[1] + dxdy[1], H};
        double[] node = null;
        double min_distance = Double.MAX_VALUE;
        int[] res = new int[2];
        for (int m = (int) cur_node[0]; ; m += search_move[index_relation][0]) {
            int n = (int) cur_node[1];
            node = new double[]{m * d_IntraOrbit, n * d_InterOrbit, H};
            if (communicate_distance(node, start) > D) {
                break;
            } else {
                for (; ; n += search_move[index_relation][1]) {
                    node = new double[]{m * d_IntraOrbit, n * d_InterOrbit, H};
                    if (communicate_distance(node, start) > D) {
                        break;
                    } else {
                        if (get_distance(node, end) < min_distance) {
                            res[0] = m;
                            res[1] = n;
                            min_distance = get_distance(node, end);
                        }
                    }
                }
            }
        }

        // 记录路径，更新终点位置
        double dt = tf + get_distance(start, new double[]{res[0] * d_IntraOrbit, res[1] * d_InterOrbit, H}) / 10000;
        dt=Math.ceil(dt*10000)/10000;
        t_cur = t_start + dt;
        double[] record = {t_cur, res[0], res[1]};
        path.add(record);
        update_end(dt);
        return res;
    }

    private double[] get_dxdy(int[] relation) {
        if (relation[0] == 1) {
            index_relation = 0;
            return new double[]{1.0, 0.0};
        } else if (relation[1] == 1) {
            index_relation = 1;
            return new double[]{0.0, 0.0};
        } else if (relation[2] == 1) {
            index_relation = 2;
            return new double[]{1.0, 1.0};
        } else if (relation[3] == 1) {
            index_relation = 3;
            return new double[]{0.0, 1.0};
        }
        return null;
    }

    // 更新位置关系
    private void update_relation(double[] start, double[] end) {
        if (start[0] <= end[0]) {
            if (start[1] <= end[1]) {
                relation[1] = 1;
            } else if (start[1] > end[1]) {
                relation[3] = 1;
            }
        } else if (start[0] > end[0]) {
            if (start[1] <= end[1]) {
                relation[0] = 1;
            } else if (start[1] > end[1]) {
                relation[2] = 1;
            }
        }
    }


    // 基站和无人机传输信号需要的距离
    private double communicate_distance(double[] jizhan, double[] node) {
        // 设基站为A点，无人机为M点，s_AM表示AM之间的距离
        double s_AM_2 = Math.pow(jizhan[0] - node[0], 2) + Math.pow(jizhan[1] - node[1], 2) + Math.pow(jizhan[2] - node[2], 2);
        double s_AM = Math.sqrt(s_AM_2);
        double s_AM_changed = Math.sqrt(Math.pow(jizhan[0] - v * (tf + s_AM / 10000) - node[0], 2) + Math.pow(jizhan[1] - node[1], 2) + Math.pow(H, 2));
        return s_AM_changed;
    }

    // 计算两点之间的距离
    public double get_distance(double[] node1, double[] node2) {
        return Math.sqrt(Math.pow(node1[0] - node2[0], 2) + Math.pow(node1[1] - node2[1], 2) + Math.pow(node1[2] - node2[2], 2));
    }

    // 更新终点位置
    private void update_end(double t) {
        end[0] = end[0] - v * t;
        return;
    }

    // 更新起点位置
    private void update_start(double t) {
        start[0] = start[0] - v * t;
        return;
    }


    // 从第一个无人机开始转发直到终点
    public void transfer(int[] cur_node) {
        double[] node = {cur_node[0] * d_IntraOrbit, cur_node[1] * d_InterOrbit, H};
        double[] tmp_node;
        int[] next_node = new int[2];
        double min_distance = Double.MAX_VALUE;
        if (communicate_distance(node, end) <= D) {
            t_end = t_cur + tf + get_distance(end, new double[]{cur_node[0] * d_IntraOrbit, cur_node[1] * d_InterOrbit, H}) / 10000;
            t_end=Math.ceil(t_end*10000)/10000;
            return;
        }
        for (int m = cur_node[0]; ; m += search_move[index_relation][0]) {
            int n = cur_node[1];
            // System.out.println(m);
            // System.out.println(n);
            tmp_node = new double[]{m * d_IntraOrbit, n * d_InterOrbit, H};
            if (get_distance(node, tmp_node) > d) {
                break;
            } else {
                for (; ; n += search_move[index_relation][1]) {
                    tmp_node = new double[]{m * d_IntraOrbit, n * d_InterOrbit, H};
                    // System.out.println(m);
                    // System.out.println(n);

                    if (get_distance(node, tmp_node) > d) {
                        break;
                    } else {
                        if (get_distance(tmp_node, end) < min_distance) {
                            next_node[0] = m;
                            next_node[1] = n;
                            min_distance = get_distance(tmp_node, end);
                        }
                    }
                }
            }
        }
        double dt = tf + get_distance(node, new double[]{next_node[0] * d_IntraOrbit, next_node[1] * d_InterOrbit, H})/10000;
        dt=Math.ceil(dt*10000)/10000;
        t_cur += dt;
        double[] record = {t_cur, next_node[0], next_node[1]};
        path.add(record);
        update_end(dt);
        // System.out.println(next_node[0]);
        // System.out.println(next_node[1]);
        // System.out.println(get_distance(end, new double[]{12* d_IntraOrbit, 11 * d_InterOrbit, H}));
        // System.out.println(Arrays.toString(end));
        // System.out.println("jieshu");
        transfer(next_node);
        return;
    }
}
