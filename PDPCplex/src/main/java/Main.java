import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import input.Input;
import input.InputHandler;
import models.Order;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IloException {
        Input input = InputHandler.handle("../TestCaseGenerator/testcase.txt");
        if (input != null) {
            PDPCplex cplex = new PDPCplex(input);
            cplex.exportModel("model.lp");
            Response response = cplex.solve();
            System.out.println(response);
        }
    }
}
