from random import shuffle


class NodeVehicle:
    def __int__(self, nodes):
        self.nodes = nodes

    def shuffle(self):
        shuffle(self.nodes)
