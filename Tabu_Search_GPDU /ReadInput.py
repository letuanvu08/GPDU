from model.NodeOrder import NodeOrder
from model.Order import Order
from model.RoutingMatrix import MatrixRouting


def read_input(path):
    orders = []
    matrix_order = []
    matrix_vehicle = []
    with open(path, "r") as f:
        line = f.readline()

        num_order = int(line.strip().split('\t')[1])
        f.readline()
        for i in range(num_order):
            line = f.readline()
            temp = line.strip().split()
            pickup = NodeOrder(i*2, int(temp[4]), int(temp[5]))
            delivery = NodeOrder(i*2 + 1, int(temp[8]), int(temp[9]))
            order = Order(pickup, delivery)
            orders.append(order)
        f.readline()
        for i in range(2 * num_order):
            line = f.readline()
            temp = line.strip().split(',')
            matrix_order.append(list(map(int, temp)))
        capacity = int(f.readline().strip().split('\t')[1])
        num_vehicle = int(f.readline().strip().split('\t')[1])
        f.readline()
        for i in range(num_vehicle):
            f.readline()
        f.readline()
        for i in range(num_vehicle):
            line = f.readline()
            temp = line.strip().split(',')
            matrix_vehicle.append(list(map(int, temp)))
        f.readline()
        f.readline()
        line = f.readline().strip().split(',')
        depot_order = list(map(int, line))
        f.readline()
        line = f.readline().strip().split(',')
        depot_vehicle = list(map(int, line))
        matrix = MatrixRouting(matrix_order, matrix_vehicle, depot_order, depot_vehicle, num_order, num_vehicle)
        result = dict()
        result['matrix'] = matrix
        result['orders'] = orders
        result['num_vehicle'] = num_vehicle

    return result
