public class Solution {
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
    int[] relation=new int[4];
    int[][] search_move=new int[][]{{-1,1},{1,1},{-1,-1},{1,-1}};
    int index_relation;
    

    public Solution(double[] start,
    double[] end,
    double v,
    double H,
    double d_IntraOrbit,
    double d_InterOrbit,
    double d,
    double D,
    double tf,
    double t_start){
        this.start=start;
        this.end=end;
        this.v=v;
        this.H=H;
        this.d_IntraOrbit=d_IntraOrbit;
        this.d_InterOrbit=d_InterOrbit;
        this.d=d;
        this.D=D;
        this.tf=tf;
        this.t_start=t_start;
        update_relation(start, end);
    }
    
    public start_firstnode_search(double[] start){
        double[] tmp_node=new double[]{Math.floor(start[0]/d_IntraOrbit),Math.floor(start[1]/d_InterOrbit),H};
        double[] dxdy=get_dxdy(relation);
        double[] cur_node=new double[]{tmp_node[0]+dxdy[0],tmp_node[1]+dxdy[1],H};
        for(int i=cur_node[0];;i+=search_move[index_relation][0]){
            int j=cur_node[1];
            if(dxdy)
        }
    }

    private double[] get_dxdy(int[] relation){
        if (relation[0]==1){
            index_relation=0;
            return new double[]{1.0,0.0};
        }
        else if(relation[1]==1){
            index_relation=1;
            return new double[]{0.0,0.0};
        }
        else if(relation[2]==1){
            index_relation=2;
            return new double[]{1.0,1.0};
        }
        else if(relation[3]==1){
            index_relation=3;
            return new double[]{0.0,1.0};
        }
        return null;
    }


    private void update_relation(double[] start,double[] end){
        if(start[0]<=end[0]){
            if(start[1]<=end[1]){
                relation[1]=1;
            }
            else if(start[1]>end[1]){   
                relation[3]=0;
            }
        }
        else if(start[0]>end[0]){
            if(start[1]<=end[1]){
                relation[0]=1;
            }
            else if(start[1]>end[1]){
                relation[2]=1;
            }
        }
    }
}



