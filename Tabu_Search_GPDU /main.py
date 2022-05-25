from ReadInput import read_input
from Routing import routing
from configs.config import Config

if __name__ == '__main__':
    input = read_input('../TestCaseGenerator/testcase.txt')
    config = Config()
    routing(input['orders'], input['matrix'], input['num_vehicle'], config, 0)
