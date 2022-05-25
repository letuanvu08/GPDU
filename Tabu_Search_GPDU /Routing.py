from ReadInput import read_input
from TabuSearch.TabuSearch import TabuSearch

def routing(orders, matrix, num_vehicle, config, start_time):
    tabu_search = TabuSearch(orders, num_vehicle, matrix, start_time, config)
    result = tabu_search.tabu_search()
    print("Best solution be found: ")
    solution = result['best']
    exec_time = result['exec_time']
    print("fitness: ", solution['fitness'])
    print("routing: ", [solution['node_vehicles']])
    print('exec_time: ', exec_time,'s')
