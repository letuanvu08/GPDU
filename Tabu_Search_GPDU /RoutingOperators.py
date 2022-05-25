from model import RoutingMatrix


def get_node(orders, node_index):
    return orders[node_index // 2].pickup if node_index % 2 == 0 else orders[node_index // 2].delivery


def fitness(candidate, start_time, orders, matrix):
    total_travel_duration = 0
    total_waiting_time = 0
    total_late_time = 0
    for i in range(len(candidate["node_vehicles"])):
        fitness_node_vehicle = fitness_vehicle(i, candidate["node_vehicles"][i], start_time, orders, matrix)
        total_travel_duration += fitness_node_vehicle['travel_duration']
        total_waiting_time += fitness_node_vehicle['waiting_time']
        total_late_time += fitness_node_vehicle['late_time']
    return set_result_fitness(total_travel_duration, total_waiting_time, total_late_time)


def fitness_vehicle(vehicle_index, node_vehicle, start_time, orders, matrix: RoutingMatrix):
    late_time = 0
    waiting_time = 0
    if len(node_vehicle) == 0:
        travel_duration = matrix.get_duration_depot_vehicle(vehicle_index)
        return set_result_fitness(travel_duration, waiting_time, late_time)
    travel_duration = matrix.get_duration_vehicle(vehicle_index, node_vehicle[0])

    vehicle_time = start_time + travel_duration
    node = get_node(orders, node_vehicle[0])
    if node.earliest_time > vehicle_time:
        waiting_time = get_waiting_time(vehicle_time, node.earliest_time)
        vehicle_time = node.earliest_time
    elif vehicle_time > node.latest_time:
        late_time = get_late_time(vehicle_time, node.latest_time)
    prev_node_index = 0
    for i in node_vehicle[1:]:
        next_node = get_node(orders, i)
        travel_duration += matrix.get_duration_order(prev_node_index, i)
        if next_node.earliest_time > vehicle_time:
            waiting_time += get_waiting_time(vehicle_time, next_node.earliest_time)
            vehicle_time = next_node.earliest_time
        elif vehicle_time > next_node.latest_time:
            late_time += get_late_time(vehicle_time, next_node.latest_time)
        prev_node_index = i
    travel_duration += matrix.get_duration_depot_order(prev_node_index)

    return set_result_fitness(travel_duration, waiting_time, late_time)


def get_waiting_time(vehicle_time, earliest_time):
    return max(0, earliest_time - vehicle_time)


def get_late_time(vehicle_time, latest_time):
    return max(0, vehicle_time - latest_time)


def set_result_fitness(travel_duration, waiting_time,late_time):
    result = dict()
    result["travel_duration"] = travel_duration
    result["waiting_time"] = waiting_time
    result["late_time"] = late_time
    return result
