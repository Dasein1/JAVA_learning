import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Main_3 {
    public static void main(String[] args) {
        DecimalFormat dec = new DecimalFormat("0.0000");
        double[][] start={{45.73, 45.26, 0},{1200, 700, 0},{ -940, 1100, 0}};
//        double[][] start = {{45.73, 45.26, 0}};
        double[][] end={{45.73, 45.26, 0},{1200, 700, 0},{ -940, 1100, 0}};
//        double[][] end = {{ -940, 1100, 0}};
        double[] t_start = {0, 4.7, 16.4};
//        double[] t_start = {0};
        double[] t_wait = {0, 0.11, 0.22, 0.33};
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("result.txt"));
            for (int cur_t_start = 0; cur_t_start < t_start.length; cur_t_start++) {
                for (int i = 0; i < start.length; i++) {
                    for (int j = 0; j < end.length; j++) {
                        for (int k = 0; k < 4; k++) {
                            if(i==j){
                                continue;
                            }
                            int node_nums=3;
                            if(k==3){
                                node_nums=1;
                            }
                            DP solution = new DP(start[i], end[j], 5, 10, 90, 80, 115, 70, 0.1, t_start[cur_t_start] + t_wait[k]);
                            solution.dp();
                            List<int[]> pp = solution.end_path;
                            out.write(dec.format(t_start[cur_t_start]) + "," + i + "," + j + "," + dec.format((solution.t_end - t_start[cur_t_start])*node_nums)+","+node_nums);
                            System.out.println(dec.format(t_start[cur_t_start]) + "," + i + "," + j + "," + dec.format((solution.t_end - t_start[cur_t_start])*node_nums)+","+node_nums);
                            out.newLine();
                            int[] x = null;
                            for (int z = 0; z < pp.size(); z++) {
                                x = pp.get(z);
                                if (x.length == 2) {
                                    out.write("(" + dec.format(solution.dp[(x[0]-solution.m_start)/solution.search_move[solution.index_relation][0]][(x[1]-solution.n_start)/solution.search_move[solution.index_relation][1]]) + "," + x[0] + "," + x[1] + ")");
                                    System.out.print("(" + dec.format(solution.dp[(x[0]-solution.m_start)/solution.search_move[solution.index_relation][0]][(x[1]-solution.n_start)/solution.search_move[solution.index_relation][1]]) + "," + x[0] + "," + x[1] + ")");
                                } else {
                                    out.write("(" + dec.format(solution.location_time[x[0]]) + "," + x[0] + ")");
                                    System.out.print("(" + dec.format(solution.location_time[x[0]]) + "," + x[0] + ")");
                                }
                                if (z != pp.size() - 1) {
                                    out.write(",");
                                    System.out.print(",");
                                } else {
                                    System.out.println("");
                                }
                            }
                            out.newLine();
                        }

                    }
                }
            }

            out.close();
        } catch (IOException e) {
        }

    }
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
    double[] location_time=new double[]{10000,10000,10000,10000,10000,10000,10000,10000};
//    List<List<List<int[]>>> path=new ArrayList<>();
    List<int[]>[][] path;
    ArrayList<double[]> result=new ArrayList<>();
    int[][] boundary=new int[][]{{1,0,0,1},{0,0,1,1},{1,1,0,0},{0,1,1,0}};
    int m_start=0;
    int m_end=0;
    int n_start=0;
    int n_end=0;
    double t_heng;
    double t_shu;
    Vector location_path=new Vector(8);
    double[][] dp;
    List<int[]> end_path=new ArrayList<>();
    {for(int i=0;i<8;i++){
        location_path.add(null);
    }
    }

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
        dp=new double[Math.abs(m_end-m_start)+1][Math.abs(n_end-n_start)+1];
        path=new List[Math.abs(m_end-m_start)+1][Math.abs(n_end-n_start)+1];
        t_heng=tf+90.0/10000.0;
        t_shu=tf+80.0/10000.0;
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
        double[] start_node=start_firstnode_search();
        m_start=(int)start_node[0];
        n_start=(int)start_node[1];
        m_end=(int)Math.floor(end[0] / d_IntraOrbit)+boundary[index_relation][2];
        n_end=(int)Math.floor(end[1] / d_InterOrbit)+boundary[index_relation][3];
        return;
    }

    public void dp(){
        dp[0][0]=t_cur;
        for(int i=0;i<Math.abs(m_end-m_start)+1;i++) {
            List<int[]> record_column;
//                if(i!=0){
//                    dp[i][0]=dp[i-1][0]+t_heng;0000000000
//                    record_column.addAll(path.get(i - 1).get(0));
//                    record_column.add(new int[]{m_start+search_move[index_relation][0]*i,n_start});
//                    record_row.add(record_column);
//                }
//                else if(i==0){
//                    record_column.add(new int[]{m_start,n_start});
//                    record_row.add(record_column);
//                }
            for (int j = 0; j < Math.abs(n_end - n_start) + 1; j++) {
                record_column = new ArrayList<>();
                if (i == 0 && j == 0) {
                    record_column.add(new int[]{m_start, n_start});
                    path[i][j]=record_column;
                    continue;
                }
                min_cost(dp, new int[]{i, j}, location_time);     // 高台0/无人机1，时间，高台或无人机索引
//                if (tmp_arr[0] == 0) {
//                    dp[i][j] = tmp_arr[1];
//                    record_column.addAll((List) location_path.get((int) tmp_arr[2]));
//                    record_column.add(new int[]{m_start + search_move[index_relation][0] * i, n_start + search_move[index_relation][1] * j});
//                    path[i][j]=record_column;
//
//                } else if (tmp_arr[0] == 1) {
//                    dp[i][j] = tmp_arr[1];
//                    record_column.addAll(path[(int) tmp_arr[2]][(int) tmp_arr[3]]);
//                    record_column.add(new int[]{m_start + search_move[index_relation][0] * i, n_start + search_move[index_relation][1] * j});
//                    path[i][j]=record_column;
//                }
            }
        }
//                    if(i==0){
//                        dp[i][j]=dp[i][j-1]+t_shu;
//                        record_column.addAll(path.get(i).get(j-1));
//                        record_column.add(new int[]{m_start+search_move[index_relation][0]*i,n_start+search_move[index_relation][1]*j});
//                        record_row.add(record_column);
//                    }
//                    else if(i!=0){
//                        if(dp[i-1][j]+t_heng<=dp[i][j-1]+t_shu){
//                            dp[i][j]=dp[i-1][j]+t_heng;
//                            record_column.addAll(path.get(i-1).get(j));
//                            record_column.add(new int[]{m_start+search_move[index_relation][0]*i,n_start+search_move[index_relation][1]*j});
//                            record_row.add(record_column);
//                        }
//                        else{
//                            dp[i][j]=dp[i][j-1]+t_shu;
//                            record_column.addAll(path.get(i).get(j-1));
//                            record_column.add(new int[]{m_start+search_move[index_relation][0]*i,n_start+search_move[index_relation][1]*j});
//                            record_row.add(record_column);
//                        }
//                    }


        arrive_end();

    }

    public void arrive_end(){
        double min_time_end=Double.MAX_VALUE;
        for(int i=0;i<Math.abs(m_end-m_start)+1;i++){
            for(int j=0;j<2;j++){
                double distance=communicate_distance(new double[]{end[0]-v*(dp[i][dp[0].length-j-1]-t_start),end[1],end[2]},new double[]{(m_start+i*search_move[index_relation][0])*d_IntraOrbit,(n_start+(dp[0].length - j - 1)*search_move[index_relation][1])*d_InterOrbit,H});
                if(distance<=D) {
                    double dt = tf + get_distance(new double[]{end[0] - v * (dp[i][dp[0].length - j - 1] - t_start), end[1], end[2]}, new double[]{(m_start + i * search_move[index_relation][0]) * d_IntraOrbit, (n_start + (dp[0].length - j - 1)* search_move[index_relation][1]) * d_InterOrbit, H}) / 10000;
                    if ((dp[i][dp[0].length - j - 1] + dt) < min_time_end) {
                        min_time_end = dp[i][dp[0].length - j - 1] + dt;
                        end_path = new ArrayList<>();
                        end_path.addAll(path[i][(dp[0].length - j - 1)]);
                        t_end = min_time_end;
                    }
                }
            }
        }
        for(int i=0;i<location.length;i++) {
            double distance = get_distance(end,location[i]);
            if(distance<=D){
                double dt=tf+distance/10000;
                if ((location_time[i] + dt) < min_time_end) {
                    min_time_end = location_time[i] + dt;
                    end_path = new ArrayList<>();
                    end_path.addAll((List)location_path.get(i));
                    t_end = min_time_end;
                }
            }
        }
    }


    // 高台0/无人机1，时间，高台或无人机索引
    public void min_cost(double dp[][],int[] cur_node,double[] location_time){
        double[] result;
        double min_time_gaotai=Double.MAX_VALUE;
        double min_index_gaotai=0;
        for(int i=0;i<location.length;i++){
            double distance=communicate_distance(new double[]{location[i][0]-v*(location_time[i]-t_start),location[i][1],location[i][2]},new double[]{(m_start+cur_node[0]*search_move[index_relation][0])*d_IntraOrbit,(n_start+cur_node[1]*search_move[index_relation][1])*d_InterOrbit,H});
            if(distance<=d){
                double dt=tf+get_distance(new double[]{location[i][0]-v*(location_time[i]-t_start),location[i][1],location[i][2]},new double[]{(m_start+cur_node[0]*search_move[index_relation][0])*d_IntraOrbit,(n_start+cur_node[1]*search_move[index_relation][1])*d_InterOrbit,H})/10000;
                if ((location_time[i]+dt)<min_time_gaotai){
                    min_time_gaotai=location_time[i]+dt;
                    min_index_gaotai=i;
                }
            }
        }
        double min_time_node;
        int[] min_index_node=new int[2];
        double time_heng_node=Double.MAX_VALUE;
        double time_shu_node=Double.MAX_VALUE;
        if(cur_node[0]!=0){
            time_heng_node=dp[cur_node[0]-1][cur_node[1]]+t_heng;
        }
        if(cur_node[1]!=0){
            time_shu_node=dp[cur_node[0]][cur_node[1]-1]+t_shu;
        }
        if(time_heng_node<=time_shu_node){
            min_index_node[0]=cur_node[0]-1;
            min_index_node[1]=cur_node[1];
            min_time_node=time_heng_node;
        }
        else{
            min_index_node[0]=cur_node[0];
            min_index_node[1]=cur_node[1]-1;
            min_time_node=time_shu_node;
        }
        if(min_time_gaotai<=min_time_node){
            result=new double[]{0,min_time_gaotai,min_index_gaotai};
        }
        else{
            result=new double[]{1,min_time_node,min_index_node[0],min_index_node[1]};
        }

        // 更新path
        List<int[]>record_column = new ArrayList<>();
        if (result[0] == 0) {
            dp[cur_node[0]][cur_node[1]] = result[1];
            record_column.addAll((List) location_path.get((int) result[2]));
            record_column.add(new int[]{m_start + search_move[index_relation][0] * cur_node[0], n_start + search_move[index_relation][1] * cur_node[1]});
            path[cur_node[0]][cur_node[1]]=record_column;

        } else if (result[0] == 1) {
            dp[cur_node[0]][cur_node[1]] = result[1];
            record_column.addAll(path[(int) result[2]][(int) result[3]]);
            record_column.add(new int[]{m_start + search_move[index_relation][0] * cur_node[0], n_start + search_move[index_relation][1] * cur_node[1]});
            path[cur_node[0]][cur_node[1]] = record_column;
        }


        // 更新location_time
        for(int i=0;i<location.length;i++){
            double distance=communicate_distance(new double[]{location[i][0]-v*(result[1]-t_start),location[i][1],location[i][2]},new double[]{(m_start+cur_node[0]*search_move[index_relation][0])*d_IntraOrbit,(n_start+cur_node[1]*search_move[index_relation][1])*d_InterOrbit,H});
            if(distance<=d){
                double dt=tf+get_distance(new double[]{location[i][0]-v*(result[1]-t_start),location[i][1],location[i][2]},new double[]{(m_start+cur_node[0]*search_move[index_relation][0])*d_IntraOrbit,(n_start+cur_node[1]*search_move[index_relation][1])*d_InterOrbit,H})/10000;
                if (result[1]+dt<location_time[i]){
                    List<int[]> tmp=new ArrayList<>();
                    tmp.addAll(path[cur_node[0]][cur_node[1]]);
                    tmp.add(new int[]{i});
                    location_path.set(i,tmp);
                    location_time[i]=dp[cur_node[0]][cur_node[1]]+dt;
                }
            }
        }
        return;
    }
}

