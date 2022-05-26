import copy
import random
import time

from RoutingOperators import fitness_vehicle, fitness


class TabuSearch:
    def __init__(self, orders: list, num_vehicle, matrix, start_time, config):
        self.orders = orders
        self.num_vehicle = num_vehicle
        self.matrix = matrix
        self.start_time = start_time
        self.config = config

    def get_neighbors(self, state):
        neighbors1 = self.get_neighbors_shuffle_each_vehicle(state, self.config.neighbors_size // 3)
        neighbors2 = self.get_neighbors_reassign_same_orders_vehicle(state, self.num_vehicle,
                                                                     self.config.neighbors_size // 3)
        neighbors3 = self.get_neighbors_reorders(state, self.config.neighbors_size // 3)
        return neighbors1 + neighbors2 + neighbors3

    def get_neighbors_shuffle_each_vehicle(self, state, size):
        neighbors = []
        node_vehicles_state = state['node_vehicles']
        orders = state['orders']
        for _ in range(size):
            candidate = dict()
            candidate['orders'] = orders.copy()
            node_vehicles = copy.deepcopy(node_vehicles_state)
            candidate['node_vehicles'] = node_vehicles
            self.shuffle(candidate)
            self.cal_fitness(candidate)

            neighbors.append(candidate)
        return neighbors

    def get_neighbors_reassign_same_orders_vehicle(self, state, num_vehicle, size):
        neighbors = []
        orders = state['orders']
        for _ in range(size):
            candidate = self.generate_candidate(orders.copy())
            neighbors.append(candidate)
        return neighbors

    def get_neighbors_reorders(self, state, size):
        neighbors = []
        orders = state['orders']
        for _ in range(size):
            new_orders = orders.copy()
            random.shuffle(new_orders)
            candidate = self.generate_candidate(new_orders)
            neighbors.append(candidate)
        return neighbors

    def generate_candidate(self, orders):
        candidate = dict()
        candidate['orders'] = orders
        indexs = self.random_index(0, len(orders), self.num_vehicle)
        node_vehicles = []
        from_index = indexs[0]
        for to_index in indexs[1:]:
            nodes = self.get_node_order(orders[from_index:to_index])
            node_vehicles.append(nodes)
            from_index = to_index
        candidate['node_vehicles'] = node_vehicles
        self.shuffle(candidate)

        self.cal_fitness(candidate)
        return candidate

    def get_node_order(self, orders):
        nodes = []
        for order in orders:
            nodes.append(order * 2)
            nodes.append(order * 2 + 1)
        return nodes

    def init_candidate(self):
        state = dict()
        state['orders'] = list(range(len(self.orders)))
        node_vehicles = []
        order_vehicles = []
        indexs = self.random_index(0, len(self.orders), self.num_vehicle)
        from_index = indexs[0]
        for to_index in indexs[1:]:
            order_vehicle = list(range(from_index, to_index))
            node_vehicles.append(self.get_node_order(order_vehicle))
            from_index = to_index
        state['node_vehicles'] = node_vehicles
        self.shuffle(state)
        self.cal_fitness(state)
        return state

    def cal_fitness(self, state):
        fitness_state = fitness(state, self.start_time, self.orders, self.matrix)
        state['fitness'] = fitness_state['travel_duration'] * self.config.weight_travel + fitness_state[
            'waiting_time'] * self.config.weight_waiting + fitness_state['waiting_time'] * self.config.weight_late
        return state

    def tabu_search(self):
        tabu_list = []
        best_candidate = self.init_candidate()
        s_best = best_candidate
        tabu_list.append(best_candidate)
        stop = False
        best_keep_turn = 0
        start_time = time.time()
        turn = 0
        while not stop:
            neighbors = self.get_neighbors(best_candidate)
            best_candidate = neighbors[0]
            for candidate in neighbors[1:]:
                if best_candidate['fitness'] > candidate['fitness'] and not self.is_in_tabu_list(tabu_list, candidate):
                    best_candidate = candidate
            print("local best: ", [best_candidate['node_vehicles']])
            if s_best['fitness'] > best_candidate['fitness']:
                s_best = best_candidate
                best_keep_turn = 0
            if best_keep_turn > self.config.stopping_turn * len(self.orders)    :
                stop = True
            tabu_list.append(best_candidate)
            if len(tabu_list) > self.config.neighbors_size:
                tabu_list.pop(0)
            best_keep_turn += 1
            turn += 1
            best_fitness = s_best['fitness']
            print('global best: ', [s_best['node_vehicles']])
            print(f'Turn: {turn}, fitness of best_candidate: {best_fitness}')
        exec_time = time.time() - start_time
        result = dict()
        result['best'] = s_best
        result['exec_time'] = exec_time
        return result

    def is_in_tabu_list(self, tabu_list, candidate):
        for i in tabu_list:
            if self.is_the_same(i, candidate):
                return True
        return False

    def is_the_same(self, candidate1, candidate2):
        for c1, c2 in zip(candidate1['node_vehicles'], candidate2['node_vehicles']):
            if len(c1) != len(c2):
                return False
            for node1, node2 in zip(c1, c2):
                if node1 != node2:
                    return False
        return True

    def shuffle(self, candidate):
        node_vehicles = []
        for node_vehicle in candidate['node_vehicles']:
            node_shuffle = []
            random.shuffle(node_vehicle)
            while len(node_vehicle) > 0:
                node = node_vehicle.pop(0)
                if node % 2 == 0:
                    if node not in node_shuffle:
                        node_shuffle.append(node)
                elif (node - 1) not in node_shuffle:
                    node_shuffle.append(node - 1)
                    node_vehicle.append(node)
                    random.shuffle(node_vehicle)
                elif node not in node_shuffle:
                    node_shuffle.append(node)
            node_vehicles.append(node_shuffle)
        candidate['node_vehicles'] = node_vehicles

    def random_index(self, from_index, to_index, num_block):
        arr = list(range(from_index + 1, to_index -1))
        indexs = []
        while len(indexs) < num_block - 1:
            index = random.choice(arr)
            arr.remove(index)
            indexs.append(index)
        indexs.append(from_index)
        indexs.append(to_index -1)
        indexs.sort()
        return indexs
