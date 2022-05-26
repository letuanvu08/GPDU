class Config:
    def __init__(self):
        self.weight_travel = float(2)
        self.weight_waiting = float(0.01)
        self.weight_late = float(0.02)
        self.neighbors_size = 1000
        self.reshuffle = 0.9
        self.stopping_turn = 20
        self.tabu_size = 200
