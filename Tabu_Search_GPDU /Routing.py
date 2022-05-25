from ReadInput import read_input
from TabuSearch.TabuSearch import TabuSearch

def routing(orders, matrix, num_vehicle, config, start_time):
    tabu_search = TabuSearch(orders, num_vehicle, matrix, start_time, config)
    tabu_search.tabu_search()
