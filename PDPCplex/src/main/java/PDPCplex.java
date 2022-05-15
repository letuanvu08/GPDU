import ilog.concert.*;
import ilog.cplex.IloCplex;
import input.Input;
import models.Order;
import utils.TimeUtils;

import java.util.Arrays;

public class PDPCplex {
    private Input input;
    private IloCplex cplex;
    private int nodeNum;


    public PDPCplex(Input input) {
        this.input = input;
        nodeNum = input.getOrders().size() * 2;
        try {
            cplex = new IloCplex();
            this.buildModel();
        } catch (IloException e) {
            System.err.println("Concert exception caught: " + e);
        }
    }

    public void exportModel(String fileName) throws IloException {
        cplex.exportModel(fileName);
    }

    public Response solve() throws IloException {
        long startTime = System.currentTimeMillis();
        cplex.setParam(IloCplex.Param.TimeLimit, 3600);
        cplex.setParam(IloCplex.Param.RootAlgorithm, IloCplex.Algorithm.Primal);
        boolean solved = cplex.solve();
        long endTime = System.currentTimeMillis();
        return Response.builder()
                .cost(solved ? cplex.getObjValue() : 0)
                .time((endTime - startTime) / 1000)
                .solved(solved)
                .build();
    }

    private void buildModel() throws IloException {
        int nodeVarNum = nodeNum * nodeNum * input.getVehicles().size();
        int vehicleVarNum = input.getVehicles().size() * nodeNum;
        int repoVarNum = nodeNum * input.getVehicles().size();
        IloNumVar[] xNodeVars = cplex.boolVarArray(nodeVarNum);
        IloNumVar[] xVehicleVars = cplex.boolVarArray(vehicleVarNum);
        IloNumVar[] xRepoVars = cplex.boolVarArray(repoVarNum);
        IloNumVar[] xVehicleRepoVars = cplex.boolVarArray(input.getVehicles().size());
        IloNumVar[] wNodeVars = cplex.intVarArray(nodeNum, 0, TimeUtils.WORKING_TIME_LIMIT);
        IloNumVar[] lNodeVars = cplex.intVarArray(nodeNum, 0, TimeUtils.WORKING_TIME_LIMIT);
        IloNumVar[] tNodeVars = cplex.intVarArray(nodeNum, 0, TimeUtils.WORKING_TIME_LIMIT);

        cplex.addMinimize(this.createObjectiveFunction(xNodeVars, xVehicleVars, xRepoVars, wNodeVars, lNodeVars, xVehicleRepoVars));
        // c1 && c2
        for (int i = 0; i < input.getVehicles().size(); i++) {
            cplex.addEq(this.getComingRepoVarExpr(i, xRepoVars, xVehicleRepoVars), 1);
            cplex.addEq(this.getLeavingVehicleVarExpr(i, xVehicleVars, xVehicleRepoVars), 1);
        }
        // c3
        for (int i = 0; i < nodeNum; i++) {
            for (int j = 0; j < input.getVehicles().size(); j++) {
                IloLinearNumExpr comingExpr = this.getComingNodeVarExpr(i, j, xNodeVars, xVehicleVars);
                IloLinearNumExpr leaveExpr = this.getLeavingNodeVarExpr(i, j, xNodeVars, xRepoVars);
                cplex.addEq(comingExpr, leaveExpr);
            }
        }
        // c4
        for (int i = 0; i < nodeNum * nodeNum; i += nodeNum + 1) {
            for (int j = 0; j < input.getVehicles().size(); j++) {
                cplex.addEq(xNodeVars[j * nodeNum * nodeNum + i], 0);
            }
        }
        // c5
        for (int i = 0; i < nodeNum; i++) {
            IloLinearNumExpr comingExpr = this.getComingNodeVarExpr(i, xNodeVars, xVehicleVars);
            IloLinearNumExpr leaveExpr = this.getLeavingNodeVarExpr(i, xNodeVars, xRepoVars);
            cplex.addEq(comingExpr, 1);
            cplex.addEq(leaveExpr, 1);
        }
        // c6
        for (int i = 0; i < input.getVehicles().size(); i++) {
            for (int j = 0; j < input.getOrders().size(); j++) {
                IloLinearNumExpr comingPickupExpr = this.getComingNodeVarExpr(j * 2, i, xNodeVars, xVehicleVars);
                IloLinearNumExpr comingDeliveryExpr = this.getComingNodeVarExpr(j * 2 + 1, i, xNodeVars);
                cplex.addEq(comingPickupExpr, comingDeliveryExpr);
            }
        }
//         time constraints: late cost > waiting cost
        for (int j = 0; j < nodeNum; j++) {
            Order.Node node = j % 2 == 0 ? input.getOrders().get(j / 2).getPickup() :
                    input.getOrders().get(j / 2).getDelivery();
            for (int i = 0; i < nodeNum; i++) {
                IloNumExpr expr1 = cplex.prod(TimeUtils.WORKING_TIME_LIMIT, cplex.diff(1, this.getXExpr(i, j, xNodeVars)));
                IloNumExpr expr2 = cplex.sum(tNodeVars[i], input.getDuration().getOrderNodeMatrix().get(i).get(j));
                cplex.addGe(tNodeVars[j], cplex.diff(expr2, expr1));
                cplex.addGe(wNodeVars[j], cplex.diff(cplex.diff(tNodeVars[j], expr2), expr1));
                cplex.addGe(lNodeVars[j], cplex.diff(cplex.diff(tNodeVars[j], node.getLatestTime()), expr1));
            }
            for (int i = 0; i < input.getVehicles().size(); i++) {
                IloNumExpr expr1 = cplex.prod(TimeUtils.WORKING_TIME_LIMIT, cplex.diff(1, xVehicleVars[i * nodeNum + j]));
                float expr2 = input.getDuration().getVehicleMatrix().get(i).get(j);
                cplex.addGe(tNodeVars[j], cplex.diff(expr2, expr1));
                cplex.addGe(wNodeVars[j], cplex.diff(cplex.diff(tNodeVars[j], expr2), expr1));
                cplex.addGe(lNodeVars[j], cplex.diff(cplex.diff(tNodeVars[j], node.getLatestTime()), expr1));
            }
            cplex.addGe(tNodeVars[j], node.getEarliestTime());
            if (j % 2 == 0) {
                cplex.addLe(tNodeVars[j], tNodeVars[j + 1]);
            }
        }
    }


    private IloNumExpr createObjectiveFunction(IloNumVar[] nodeVars, IloNumVar[] vehicleVars, IloNumVar[] repoVars,
                                               IloNumVar[] wNodeVars, IloNumVar[] lNodeVar, IloNumVar[] vehicleRepoVars) throws IloException {
        IloLinearNumExpr tExpr = cplex.linearNumExpr();
        for (int i = 0; i < nodeVars.length; i++) {
            tExpr.addTerm(input.getDuration().getOrderNodeMatrix().get(i / nodeNum % nodeNum).get(i % nodeNum),
                    nodeVars[i]);
        }

        for (int i = 0; i < vehicleVars.length; i++) {
            tExpr.addTerm(input.getDuration().getVehicleMatrix().get(i / nodeNum).get(i % nodeNum),
                    vehicleVars[i]);

        }
        for (int j = 0; j < repoVars.length; j++) {
            tExpr.addTerm(input.getDuration().getRepoList().get(j % nodeNum), repoVars[j]);
        }
        for (int i = 0; i < vehicleRepoVars.length; i++) {
            tExpr.addTerm(input.getDuration().getVehicleRepoList().get(i), vehicleRepoVars[i]);
        }
        ////
        IloLinearNumExpr wExpr = cplex.linearNumExpr();
        for (IloNumVar wNodeVar : wNodeVars) {
            wExpr.addTerm(1, wNodeVar);
        }
        IloLinearNumExpr lExpr = cplex.linearNumExpr();
        for (IloNumVar iloNumVar : lNodeVar) {
            lExpr.addTerm(1, iloNumVar);
        }
        return cplex.sum(cplex.prod(input.getCost().getTravel(), tExpr),
                cplex.sum(cplex.prod(input.getCost().getWaiting(), wExpr), cplex.prod(input.getCost().getLate(), lExpr))
        );
    }

    private IloLinearNumExpr getComingNodeVarExpr(int node, IloNumVar[] nodeVars, IloNumVar[] vehicleVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        for (int i = 0; i < nodeVars.length; i += nodeNum) {
            expr.addTerm(1, nodeVars[i + node]);
        }
        for (int i = 0; i < vehicleVars.length; i += nodeNum) {
            expr.addTerm(1, vehicleVars[i + node]);
        }
        return expr;
    }

    private IloLinearNumExpr getComingNodeVarExpr(int node, int vehicle, IloNumVar[] nodeVars, IloNumVar[] vehicleVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        int nodeOffset = vehicle * nodeNum * nodeNum;
        for (int i = node; i < nodeNum * nodeNum; i += nodeNum) {
            expr.addTerm(1, nodeVars[nodeOffset + i]);
        }
        int vehicleOffset = nodeNum * vehicle;
        expr.addTerm(1, vehicleVars[vehicleOffset + node]);
        return expr;
    }

    private IloLinearNumExpr getComingNodeVarExpr(int node, int vehicle, IloNumVar[] nodeVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        int nodeOffset = vehicle * nodeNum * nodeNum;
        for (int i = node; i < nodeNum * nodeNum; i += nodeNum) {
            expr.addTerm(1, nodeVars[nodeOffset + i]);
        }
        return expr;
    }

    private IloLinearNumExpr getLeavingNodeVarExpr(int node, IloNumVar[] nodeVars, IloNumVar[] repoVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        for (int i = node * nodeNum; i < nodeVars.length; i += nodeNum * nodeNum) {
            for (int j = 0; j < nodeNum; j++) {
                expr.addTerm(1, nodeVars[i + j]);
            }
        }
        for (int i = node; i < repoVars.length; i += nodeNum) {
            expr.addTerm(1, repoVars[i]);
        }
        return expr;
    }

    private IloLinearNumExpr getLeavingNodeVarExpr(int node, int vehicle, IloNumVar[] nodeVars, IloNumVar[] repoVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        int nodeOffset = vehicle * nodeNum * nodeNum;
        for (int i = 0; i < nodeNum; i += 1) {
            expr.addTerm(1, nodeVars[nodeOffset + nodeNum * node + i]);
        }
        int repoOffset = vehicle * nodeNum;
        expr.addTerm(1, repoVars[repoOffset + node]);
        return expr;
    }


    private IloNumExpr getComingRepoVarExpr(int vehicle, IloNumVar[] repoVars, IloNumVar[] vehicleRepoVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        int repoOffset = vehicle * nodeNum;
        for (int i = 0; i < nodeNum; i++) {
            expr.addTerm(1, repoVars[repoOffset + i]);
        }
        expr.addTerm(1, vehicleRepoVars[vehicle]);
        return expr;
    }

    private IloNumExpr getLeavingVehicleVarExpr(int vehicle, IloNumVar[] vehicleVars, IloNumVar[] vehicleRepoVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        int vehicleOffset = vehicle * nodeNum;
        for (int i = 0; i < nodeNum; i++) {
            expr.addTerm(1, vehicleVars[vehicleOffset + i]);
        }
        expr.addTerm(1, vehicleRepoVars[vehicle]);
        return expr;
    }

    private IloNumExpr getXExpr(int fromNode, int toNode, IloNumVar[] nodeVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        for (int i = fromNode * nodeNum; i < nodeVars.length; i += nodeNum * nodeNum) {
            expr.addTerm(1, nodeVars[i + toNode]);
        }
        return expr;
    }
}
