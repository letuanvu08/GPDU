class MatrixRouting:
    def __init__(self, matrix_order, matrix_vehicle, matrix_depot_order, matrix_depot_vehicle, num_order, num_vehicle):
        self.matrix_order = matrix_order
        self.matrix_vehicle = matrix_vehicle
        self.matrix_depot_order = matrix_depot_order
        self.matrix_depot_vehicle = matrix_depot_vehicle
        self.num_vehicle = num_vehicle
        self.num_order = num_order

    def get_duration_vehicle(self, vehicle_index, node_index):
        return self.matrix_vehicle[vehicle_index][node_index]

    def get_duration_order(self, from_node, to_node):
        return self.matrix_order[from_node][to_node]

    def get_duration_depot_order(self, order_index):
        return self.matrix_depot_order[order_index]

    def get_duration_depot_vehicle(self, vehicle_index):
        return self.matrix_depot_vehicle[vehicle_index]