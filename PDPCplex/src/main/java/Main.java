import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import input.Input;
import input.InputHandler;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IloException {
        Input input = InputHandler.handle("../TestCaseGenerator/testcase.txt");
        if (input != null) {
            PDPCplex cplex = new PDPCplex(input);
            Response response = cplex.solve();
            System.out.println(response);
        }

    }
}
