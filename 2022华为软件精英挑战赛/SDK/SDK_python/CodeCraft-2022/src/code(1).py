import numpy as np
import configparser
import time
# import string
# @profile
def main():
    start=time.time()*1000
    conf=configparser.ConfigParser()
    conf.read("simulated_data/config.ini")
    qos_constraint=conf.items("config")[0][1]
    # print(qos_constraint)

    # demand='data/demand.csv'  #需求地址
    # qos='data/qos.csv'     #qos地
    # site_bandwidth='data/site_bandwidth.csv'   #site_bandwidth地址

    # demand='data_test/demand.csv'  #需求地址
    # qos='data_test/qos.csv'     #qos地址
    # site_bandwidth='data_test/site_bandwidth.csv'   #site_bandwidth地址

    demand='simulated_data/demand.csv'  #需求地址
    qos='simulated_data/qos.csv'     #qos地
    site_bandwidth='simulated_data/site_bandwidth.csv'   #site_bandwidth地址

##########读文件

    #  100*10  时间*客户
    with open(demand,encoding = 'utf-8') as f:
        data = np.loadtxt(f,str,delimiter = ",", skiprows = 0)
        customer_name=data[0,1:]
        data_demand = np.array(data[1:,1:])
        data_demand = data_demand.astype(float)
    # print(sum(sum(data_demand)))
    # print((data_demand[:,1]))

    data_demand111=data_demand.copy()

    #   100*10   边缘节点*客户
    with open(qos,encoding = 'utf-8') as f:
        data = np.loadtxt(f,str,delimiter = ",", skiprows = 1)
        node_name=data[:,0]
        data_qos = np.array(data[:,1:])
        data_qos = np.array(data_qos < qos_constraint)
        # print(data_qos)


    #    100*1   边缘节点
    with open(site_bandwidth,encoding = 'utf-8') as f:
        data = np.loadtxt(f,str,delimiter = ",", skiprows = 1)
        data_bandwidth = np.array(data[:, 1:])
        data_bandwidth = data_bandwidth.astype(np.uint64)

##########初始化

    # 常量
    time_total=data_demand.shape[0]
    customer_total=data_demand.shape[1]
    node_total=data_qos.shape[0]


    # 全局变量5%门限
    th = int(time_total*0.05)

    #  100*100 时间*边缘节点
    time_node = np.dot(data_demand,np.transpose(data_qos))
    #print(time_node)

    def get_maxnode(nums_used,MAX_node_index):
        lst = list(MAX_node_index[1])
        freq = nums_used[lst]
        return np.argmin(freq)


    # 指示矩阵
    #spaceIndex=np.zeros(time_node.shape,np.uint32)
    nums_used=np.zeros(node_total)

    #  带宽成本
    cost=0

    # 输出矩阵 时间-边缘节点-客户
    time_node_custom= np.zeros((time_total,node_total,customer_total))

######    step 1 :最值分配
    # 最大带宽 时间-边缘节点
    data_bandwidth_alltime = np.tile(data_bandwidth.T,(time_total,1))
    time_node_max = np.minimum(time_node,data_bandwidth_alltime)

    data_qos_e = data_qos.copy()
    end_must = int(th*node_total*0.7)
    while end_must:
        # 要考虑节点容量上限
        t_idx = np.argmax(time_node_max)
        max_node_ = (t_idx//node_total ,t_idx % node_total)   #（时间，边缘节点n）
        # max_node_ = np.unravel_index(np.argmax(time_node), time_node.shape)
        cus = data_qos_e[max_node_[1], :]                       # n节点连接的所有用户
        dem = data_demand[max_node_[0], cus]               # 这些用户在t时刻的需求
        if time_node_max[max_node_]< time_node[max_node_]:
            user_idx = np.nonzero(cus)[0]                      # n节点连接所有用户的索引
            dem_sort_idx = np.argsort(dem)                   # n节点所用用户在t时刻的需求排序索引
            dem_sort = dem[dem_sort_idx]                       # 排序后的具体的值
            for u in range(1,user_idx.size+1):
                current_vol = time_node_max[max_node_]
                res = dem_sort[-u]
                place_in_dem = dem_sort_idx[-u]
                if current_vol > res:
                    current_vol -= res
                    time_node_custom[max_node_[0], max_node_[1], user_idx[place_in_dem]] += res
                else:
                    time_node_custom[max_node_[0], max_node_[1], user_idx[place_in_dem]] += current_vol
                    dem[dem_sort_idx[-u]] = current_vol
                    dem[dem_sort_idx[:-u]] = 0
                    break

        else:
            time_node_custom[max_node_[0], max_node_[1], cus] = dem       # t时刻，n节点分给用户


        dif_node_flow = np.dot(data_qos_e[:, cus],dem)                # 
        time_node[max_node_[0],:] -= dif_node_flow                      #更新time-node的值
        nums_used[max_node_[1]] += 1                                    #n节点使用次数加1
        data_demand[max_node_[0], data_qos_e[max_node_[1], :]] -= dem       # t时刻，这些用户
        if nums_used[max_node_[1]] >= th:
            time_node[:, max_node_[1]] = 0
            data_qos_e[max_node_[1],:] = 0
        time_node_max = np.minimum(time_node,data_bandwidth_alltime)
        end_must -= 1
        
########step 2：平均分配

    busy_time = np.argsort(np.sum(data_demand, 1))
    node_max = np.zeros(node_total)
    # 每个用户连接的节点数、每个节点连接的用户数
    nodes_per_user = np.sum(data_qos, 0)
    user_per_nodes = np.sum(data_qos, 1)

    # 求用户平均每时刻每个节点需求
    data_demand_avg = np.zeros(data_demand.shape)
    for i in range(customer_total):
        data_demand_avg[:,i]=data_demand[:,i]/nodes_per_user[i]

    # 遍历开始

    while data_demand_avg.any():
        # 找到最大需求对应的索引<时间，用户>
        MAX_user_demand_avg = np.max(data_demand_avg)
        MAX_user_index = np.nonzero(data_demand_avg == MAX_user_demand_avg)
        # MAX_user_list=list(zip(MAX_user_index[0],MAX_user_index[1]))
        # 找到唯一的时间、用户索引
        max_index = get_maxnode(nodes_per_user, MAX_user_index)
        user_index = (MAX_user_index[0][max_index], MAX_user_index[1][max_index])
        # 需求分配

        # 准备数据：当前时刻需求、排序后的节点用户数值以及其对应节点索引，并找出峰值非零节点

        MAX_user_demand = data_demand[user_index]
        cs_node_index = np.nonzero(data_qos[:, user_index[1]])[0]
        cs_node_user = user_per_nodes[cs_node_index]
        cs_node_max = node_max[cs_node_index]

        # 划分峰值零节点与非零节点.注意如果全是零节点直接执行均分
        zero_node = []
        node_id_zero = np.array([])
        if True:
            if not cs_node_max.any():
                # 平均分配
                time_node_custom[user_index[0], cs_node_index, user_index[1]] = MAX_user_demand_avg * data_qos[cs_node_index, user_index[1]]
                # 更新节点峰值数据
                node_max[cs_node_index] = MAX_user_demand_avg
            else:
                if not cs_node_max.all():
                    zero_node = np.nonzero(cs_node_max == 0)[0]
                    node_id_zero = cs_node_index[zero_node]
                node_id_nonzero = np.delete(cs_node_index, zero_node)
                node_user_nonzero = np.delete(cs_node_user, zero_node)
            # 对非零节点的服务用户数按升序排序
                idx_sort = np.argsort(node_user_nonzero)
                node_user_sort = node_user_nonzero[idx_sort]
            # 按升序返回非零节点的id
                node_id_sort = node_id_nonzero[idx_sort]

            # 整体分配原则：先针对节点最大值非0的按照用户数从小到大进行分配，如果还剩余需求，则对节点最大值为0的进行均分
                # 非零

                for i in range(node_user_sort.size):
                    node = node_id_sort[i]
                    # res = node_max[node]-time_node[user_index[0],node] 
                    res = node_max[node]- np.sum(time_node_custom[user_index[0],node,:])
                    if MAX_user_demand > res:
                        MAX_user_demand -= res
                        time_node_custom[user_index[0], node, user_index[1]] = res
                    else:
                        time_node_custom[user_index[0], node, user_index[1]] += MAX_user_demand
                        MAX_user_demand = 0
                        break
                # 零
                if MAX_user_demand:
                    if node_id_zero.size:
                        avg_demand = MAX_user_demand/node_id_zero.size
                        # 平均分配
                        time_node_custom[user_index[0], node_id_zero, user_index[1]] = avg_demand * data_qos[node_id_zero, user_index[1]]
                        # 更新节点峰值数据
                        node_max[node_id_zero] = avg_demand
                    else:
                        avg_demand = MAX_user_demand/node_id_nonzero.size
                        time_node_custom[user_index[0], node_id_nonzero, user_index[1]] += avg_demand
                        node_max[node_id_nonzero] += avg_demand


        # 当前时刻用户资源置0
        data_demand_avg[user_index] = 0
    #     # 更新时间-边缘节点表（按用户求和）
        # time_node = np.sum(time_node_custom, axis=2)


############调配
    time_node_custom = np.round(time_node_custom)
    temp = 0
    # for i in range(time_total):
    #     for k in range(customer_total):
    #         con_k = np.nonzero(data_qos[:,k])[0]
    #         temp = np.sum(time_node_custom[i,con_k,k]) - data_demand111[i,k]
            
    #         if temp != 0:
    #             # print(np.sum(time_node_custom[i,con_j,j]))
    #             # print(data_demand111[i,j])
    #             # print(customer_name[j],i)
    #             # print(con_j)
    #             if temp < 0:
    #                 conk_pos = np.argmin(time_node_custom[i,con_k,k])
    #             else:
    #                 conk_pos = np.argmax(time_node_custom[i,con_k,k])
    #             # print(node_name[con_j[conj_pos]])
    #             # print(time_node_custom[i,con_j[conj_pos],j])
    #             time_node_custom[i,con_k[conk_pos],k] -= temp
    #             # print(time_node_custom[i,con_j[conj_pos],j])
                # print('\n')


    # resulto = np.nonzero(time_node_custom)
    # t_res = resulto[0]
    # n_res = resulto[1]
    # c_res = resulto[2]
    def concat_string(txt,node,cost):
        lst = [txt, "<", node, ",", cost, ">", ","]
        return "".join(lst)


    # def write_in(file):
    #     t,c,n = time_total,customer_total,node_total
    #     tnc = time_node_custom.copy()
    #     cn = customer_name.copy()
    #     dq = data_qos.copy()
    #     dd = data_demand111.copy()
    #     for ii in range(t):
    #         for jj in range(c):
    #             # print(jj)
    #             con_j = np.nonzero(dq[:,jj])[0]
    #             temp = np.sum(tnc[ii,con_j,jj]) - dd[ii,jj]
    #             if temp != 0:
    #                 if temp < 0:
    #                     conj_pos = np.argmin(tnc[ii,con_j,jj])
    #                 else:
    #                     conj_pos = np.argmax(tnc[ii,con_j,jj])      
    #                 tnc[ii,con_j[conj_pos],jj] -= temp
    #                 # print(temp,tnc[ii,con_j,jj])

    #             text = ''.join([cn[jj],":"])
    #             ddd = np.sum(tnc[ii, :, jj])
    #             for kk in range(con_j.size):
    #                 print(kk)
    #                 if tnc[ii, con_j[kk], jj] != 0:
    #                     text = concat_string(text, node_name[con_j[kk]],str(tnc[ii, con_j[kk], jj])[:-2])
    #                     # text += "<" + node_name[kk] + "," + str(time_node_custom[ii, kk, jj])[:-2] + ">" + ","
    #                     ddd -= tnc[ii, con_j[kk], jj]
    #                     if ddd:
    #                         break

    #             if text[-1] == ',':
    #                 text = text[:-1]
    #             text += "\n"
    #             file.write(text)


############输出
    text=''
    with open('output/solution.txt','w',encoding='utf-8') as f:
        for i in range(time_total):
            for j in range(customer_total):
                con_j = np.nonzero(data_qos[:,j])[0]
                temp = np.sum(time_node_custom[i,con_j,j]) - data_demand111[i,j]
                if temp != 0:
                    if temp < 0:
                        conj_pos = np.argmin(time_node_custom[i,con_j,j])
                    else:
                        conj_pos = np.argmax(time_node_custom[i,con_j,j])      
                    time_node_custom[i,con_j[conj_pos],j] -= temp
                
                
                text = customer_name[j]+":"
                ddd = np.sum(time_node_custom[i,:,j])
                
                k_start = (time_node_custom[i,con_j,j]!=0).argmax(axis=0)
                for k in range(k_start,con_j.size) :
                    if time_node_custom[i,con_j[k],j]!=0 :
                        text+=("<"+node_name[con_j[k]]+","+str(time_node_custom[i,con_j[k],j])[:-2]+">"+",")
                        ddd -= time_node_custom[i,con_j[k],j]
                        if ddd == 0:
                            break
                if text[-1]==',':
                    text=text[:-1]
                text+=("\n")
                f.write(text)



    # with open('output/solution.txt','w',encoding='utf-8') as f:
    #     write_in(f)

    end=time.time()*1000
    # print(timeread-start)
    # print(timestep1-timeread)
    # print(timestep2-timestep1)
    print(end-start)
    # print(time_total,customer_total,node_total)

if __name__ == '__main__':
    main()