import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.List;

public class PDPCplex {
    private Config config;
    private IloCplex cplex;

    public PDPCplex(Config config) {
        this.config = config;
        try {
            cplex = new IloCplex();
            this.buildModel();
        } catch (IloException e) {
            System.err.println("Concert exception caught: " + e);
        }
    }

    public Response solve() {

    }

    private void buildModel() throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        IloNumVar[] x = cplex.intVarArray(config.getOrderNumber() * config.getVehicleNumber() * 2, 0, 1);
        for (int i = 0; i < x.length; i++) {
            expr.addTerm(, );
        }
        cplex.addMinimize(expr);
    }
}
