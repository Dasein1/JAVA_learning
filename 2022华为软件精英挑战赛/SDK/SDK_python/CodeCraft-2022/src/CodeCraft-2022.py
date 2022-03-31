import numpy as np
import configparser
import time

start = time.time()
conf = configparser.ConfigParser()
conf.read("E:/2022华为软件精英挑战赛/线下调试数据/data/config.ini")
qos_constraint = conf.items("config")[0][1]
# print(qos_constraint)

demand = r'E:\2022华为软件精英挑战赛\线下调试数据\data\demand.csv'  # 需求地址
qos = r'E:\2022华为软件精英挑战赛\线下调试数据\data\qos.csv'  # qos地址
site_bandwidth = r'E:\2022华为软件精英挑战赛\线下调试数据\data\site_bandwidth.csv'  # site_bandwidth地址

#  100*10  时间*客户
with open(demand, encoding='utf-8') as f:
    data = np.loadtxt(f, str, delimiter=",", skiprows=0)
    customer_name = data[0, 1:]
    data_demand = np.array(data[1:, 1:])
    data_demand = data_demand.astype(np.float64)

#   100*10   边缘节点*客户
with open(qos, encoding='utf-8') as f:
    data = np.loadtxt(f, str, delimiter=",", skiprows=1)
    node_name = data[:, 0]
    data_qos = np.array(data[:, 1:])
    data_qos = np.array(data_qos < qos_constraint, np.float64)
    transpose_data_qos=np.transpose(data_qos)
    # print(data_qos)

#    100*1   边缘节点
with open(site_bandwidth, encoding='utf-8') as f:
    data = np.loadtxt(f, str, delimiter=",", skiprows=1)
    data_bandwidth = np.array(data[:, 1:])
    data_bandwidth = data_bandwidth.astype(np.float64)

# 常量
time_total = data_demand.shape[0]
customer_total = data_demand.shape[1]
node_total = data_demand.shape[0]

# 全局变量5%门限
th = int(time_total * 0.05)

#  100*100 时间*边缘节点
time_node = np.dot(data_demand, np.transpose(data_qos))


# print(time_node)

def get_maxnode(nums_used, MAX_node_index):
    lst = list(MAX_node_index[1])
    freq = nums_used[lst]
    return np.argmin(freq)


# 指示矩阵
# spaceIndex=np.zeros(time_node.shape,np.uint32)
nums_used = np.zeros(node_total)

#  带宽成本
cost = 0

# 输出矩阵 时间-边缘节点-客户
time_node_custom = np.zeros((time_total, node_total, customer_total), np.float64)

##step 1 :最值分配

while (np.min(nums_used) < th):
    MAX_node = np.max(time_node)
    MAX_node_index = np.where(time_node == MAX_node)
    MAX_node_list = list(zip(MAX_node_index[0], MAX_node_index[1]))
    max_index = MAX_node_list[get_maxnode(nums_used, MAX_node_index)]
    # print(max_index)
    cost += time_node[max_index[0], max_index[1]]
    # for i in range(data_demand.shape[1]):
    #     if(data_qos[max_index[1],i]==1):
    #         time_node_custom[max_index[0],max_index[1],i]=data_demand[max_index[0],i]
    cus = (data_qos[max_index[1], :] == 1)
    time_node_custom[max_index[0], max_index[1], cus] = data_demand[max_index[0], cus]

    nums_used[max_index[1]] += 1
    data_demand[max_index[0], data_qos[max_index[1], :] == 1] = 0
    time_node = np.dot(data_demand, np.transpose(data_qos))
    time_node[:, nums_used >= th] = 0
    if ~(np.any(data_demand)):
        break

consum_time_node_before=np.sum(time_node_custom,axis=2)
sorted_consum_before=np.sort(consum_time_node_before,axis=0)
print("before的demand")
print(np.max(data_demand))
time_node = np.dot(data_demand, np.transpose(data_qos))
# #平均分配
# nood_max = np.zeros(node_total,np.uint32)
# #每个用户连接的节点数、每个节点连接的用户数
# nodes_per_user = np.sum(data_qos, 0)
# user_per_nodes = np.sum(data_qos, 1)
# #求用户平均每时刻每个节点需求
# data_demand_avg = np.zeros(data_demand.shape,np.float)
# for i in range(customer_total):
#     data_demand_avg[:,i]=data_demand[:,i]/nodes_per_user[i]
# #找到最大需求对应的索引<时间，用户>
# MAX_user_demand = np.max(data_demand_avg)
# MAX_user_index = np.where(data_demand_avg == MAX_user_demand)
# MAX_user_list=list(zip(MAX_user_index[0],MAX_user_index[1]))
# #找到唯一的时间、用户索引
# user_index = MAX_user_list[get_maxnode(nodes_per_user, MAX_user_index)]
# #需求分配
# MAX_user_demand = data_demand[user_index]
# while()
# time_node_custom[user_index[0],:,user_index[1]] = MAX_user_demand*data_qos[:,user_index[1]]
#
#
# print(nodes_per_user.shape)


# while(np.max(time_node)==0 or np.min(nums_used)>=time_total):
threhold = 1200

for i in range(time_total):

    # 按threhold分配（按边缘节点所连接的用户需求之间的比例）
    used = [0] * node_total

    for _ in range(node_total):
        # 找到所连接用户的需求总和最大的节点，排序
        demand_unsorted = time_node[i, :]
        arr = []
        for _ in range(node_total):
            if demand_unsorted[_] != 0:
                arr.append((_, demand_unsorted[_]))
        arr.sort(key=lambda x: x[1], reverse=True)

        # 选取节点
        cur = 0
        while cur < len(arr):
            if used[arr[cur][0]] == 1:
                cur += 1
            else:
                break
        # 分配和更新
        if (cur != len(arr)):
            if (arr[cur][1] > threhold):
                provide = threhold
            else:
                provide = arr[cur][1]
            demand_node_customer = data_demand[i] * data_qos[arr[cur][0], :]
            sum_demand = np.sum(demand_node_customer)
            demand_provide = demand_node_customer / sum_demand * provide
            print("demand_provide")
            print(demand_provide)
            data_demand[i] -= demand_provide
            time_node = np.dot(data_demand, np.transpose(data_qos))
            time_node_custom[i, arr[cur][0], :] += demand_provide
            used[arr[cur][0]] = 1
        else:
            break

    # 直接分配（按边缘节点所连接的用户需求的比例）
    # 按所连接用户的需求总和将边缘节点按降序排序


    for _ in range(node_total):
        demand_unsorted = time_node[i, :]
        arr = []
        for _ in range(node_total):
            arr.append((_, demand_unsorted[_]))
        arr.sort(key=lambda x: x[1], reverse=True)

        # 选取节点
        if arr[0][1]==0:
            break
        else:
            demand_node_customer = data_demand[i] * data_qos[arr[0][0], :]
            data_demand[i] -= demand_node_customer
            time_node = np.dot(data_demand, np.transpose(data_qos))
            time_node_custom[i, arr[0][0], :] += demand_node_customer

# 判断用户需求的最小值是否为0
print(np.min(data_demand))

# 计算边缘节点带宽成本
## 取边缘节点在时间维度上带宽成本降序的第th+1个作为95分位带宽
consum_time_node=np.sum(time_node_custom,axis=2)
sorted_consum=np.sort(consum_time_node,axis=0,)
total_cost=np.sum(sorted_consum[-th-1,:])
print("total_cost")
print(total_cost)

np.set_printoptions(suppress=True)
print(consum_time_node)

chazhi=consum_time_node-consum_time_node_before
print(np.min(chazhi))
print("afterdemand:")
print(np.max(data_demand))
# print(customer_name)
# print(len(node_name))
print(np.max(time_node))

with open('F:/huawei/text.txt', 'w', encoding='utf-8') as f:
    for i in range(time_total):
        for j in range(node_total):
            text = ''
            text += node_name[j] + ":"
            for k in range(customer_total):
                if (time_node_custom[i, j, k] != 0):
                    text += "<" + customer_name[k] + "," + str(time_node_custom[i, j, k]) + ">" + ","
            if (text[-1] == ','):
                text = text[:-1]
            text += "\n"
            f.write(text)

end = time.time()
# print(end-start)
# print(time_total,customer_total,node_total)
