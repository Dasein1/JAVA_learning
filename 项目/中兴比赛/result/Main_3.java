import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Main_3 {

}

class DP{
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
    double t_end=Double.MAX_VALUE;
    double t_cur;
    int[] relation = new int[4];
    int[][] search_move = new int[][]{{-1, 1}, {1, 1}, {-1, -1}, {1, -1}};
    int index_relation;
    double[][] location =new double[][]{{-614,1059,24},{-934,715,12},{1073,291,37},{715,129,35},{186,432,21},{-923,632,37},{833,187,24},{-63,363,11}};
    Stack<double[]> path = new Stack<>();
    ArrayList<double[]> result=new ArrayList<>();
    int[][] boundary=new int[][]{{1,0,0,1},{0,0,1,1},{1,1,0,0},{0,1,1,0}};
    int m_start=0;
    int m_end=0;
    int n_start=0;
    int n_end=0;


    public DP(double[] start,
                    double[] end,
                    double v,
                    double H,
                    double d_IntraOrbit,
                    double d_InterOrbit,
                    double d,
                    double D,
                    double tf,
                    double t_start) {
        this.start = Arrays.copyOf(start,start.length);
        this.end = Arrays.copyOf(end,end.length);
        this.v = v;
        this.H = H;
        this.d_IntraOrbit = d_IntraOrbit;
        this.d_InterOrbit = d_InterOrbit;
        this.d = d;
        this.D = D;
        this.tf = tf;
        this.t_start = t_start;
        update_relation(start, end);
        update(t_start);
        this.t_cur = t_start;
        update_boundary(this.start,this.end);
    }

    // 根据起点和终点找到起点将转发的第一个无人机
    public double[] start_firstnode_search() {
        double[] tmp_node = new double[]{Math.floor(start[0] / d_IntraOrbit), Math.floor(start[1] / d_InterOrbit), H};
        // System.out.printf(Arrays.toString(tmp_node));
        double[] dxdy = get_dxdy(relation);
        // System.out.println(Arrays.toString(tmp_node));
        double[] cur_node = new double[]{tmp_node[0] + dxdy[0], tmp_node[1] + dxdy[1], H};
        // System.out.println(Arrays.toString(cur_node));
        double[] node = null;
        double min_distance = Double.MAX_VALUE;
        double[] res = new double[3];
        for (int m = (int) cur_node[0];Math.abs(m*d_IntraOrbit-start[0])<=D ||((m*d_IntraOrbit-start[0])*search_move[index_relation][0])<=0; m += search_move[index_relation][0]) {

            // System.out.println("第一个无人机："+m+","+n);
            // node = new double[]{m * d_IntraOrbit, n * d_InterOrbit, H};
            // if (communicate_distance(start, node) > D) {
            //     break;
            // } else {
            for (int n = (int) cur_node[1];Math.abs(n*d_InterOrbit-start[1])<=D || ((n*d_InterOrbit-start[1])*search_move[index_relation][1])<=0; n += search_move[index_relation][1]) {
                node = new double[]{m * d_IntraOrbit, n * d_InterOrbit, H};
                // if (communicate_distance(start, node) > D) {
                //     break;
                // } else {
                // System.out.println("第一个无人机："+m+","+n);
                if (communicate_distance(start, node) <=D && get_distance(node, end) < min_distance) {
                    res[0] = m;
                    res[1] = n;
                    min_distance = get_distance(node, end);
                }
            }
        }

        // 记录路径，更新终点位置
        double dt = tf + get_distance(start, new double[]{res[0] * d_IntraOrbit, res[1] * d_InterOrbit, H}) / 10000;
        dt=Math.floor(dt*10000)/10000;
        res[2]=dt;
//        t_cur = t_start + dt;
//        double[] record = {t_cur, res[0], res[1]};
//        path.add(record);
//        update_end(dt);
//        System.out.println(res[0]+","+res[1]);
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

    private void update_relation(double[] start, double[] end) {
        if (start[0] <= end[0]) {
            if (start[1] <= end[1]) {
                index_relation=1;
                relation[1] = 1;
            } else if (start[1] > end[1]) {
                relation[3] = 1;
                index_relation=3;
            }
        } else if (start[0] > end[0]) {
            if (start[1] <= end[1]) {
                index_relation=0;
                relation[0] = 1;
            } else if (start[1] > end[1]) {
                relation[2] = 1;
                index_relation=2;
            }
        }
    }

    private double communicate_distance(double[] jizhan, double[] node) {
        // 设基站为A点，无人机为M点，s_AM表示AM之间的距离
        double s_AM_2 = Math.pow(jizhan[0] - node[0], 2) + Math.pow(jizhan[1] - node[1], 2) + Math.pow(jizhan[2] - node[2], 2);
        s_AM_2=Math.floor(s_AM_2*10)/10;
        double s_AM = Math.sqrt(s_AM_2);
        s_AM=Math.floor(s_AM*2)/2;
        double s_AM_changed = Math.sqrt(Math.pow(jizhan[0] - v * (tf + s_AM / 10000) - node[0], 2) + Math.pow(jizhan[1] - node[1], 2) + Math.pow(H, 2));
        s_AM_changed=Math.floor(s_AM_changed*10)/10;
        return s_AM_changed;
    }

    public double get_distance(double[] node1, double[] node2) {
        double temp_2=Math.pow(node1[0] - node2[0], 2) + Math.pow(node1[1] - node2[1], 2) + Math.pow(node1[2] - node2[2], 2);
        temp_2=Math.floor(temp_2*2)/2;
        //	Math.floor(Math.sqrt(Math.pow(node1[0] - node2[0], 2) + Math.pow(node1[1] - node2[1], 2) + Math.pow(node1[2] - node2[2], 2))/10000)*10000;
        double tmp=Math.sqrt(temp_2);
        tmp=Math.floor(tmp*2)/2;
        return tmp;
    }

    // 更新起点、终点和高台位置
    private void update(double t){
        end[0]=end[0]-v*t;
        start[0]=start[0]-v*t;
        for(double[] x:location){
            x[0]=x[0]-v*t;
        }
        return;
    }

    private void update_boundary(double[] start,double[] end){
        m_start=(int)Math.floor(start[0] / d_IntraOrbit)+boundary[index_relation][0];
        n_start=(int)Math.floor(start[1] / d_IntraOrbit)+boundary[index_relation][1];
        m_end=(int)Math.floor(end[0] / d_IntraOrbit)+boundary[index_relation][0];
        n_end=(int)Math.floor(end[1] / d_IntraOrbit)+boundary[index_relation][1];
        return;
    }

    public double[][] dp{}

}