import ilog.concert.*;
import ilog.cplex.IloCplex;
import input.Input;
import models.Order;
import utils.TimeUtils;

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

    public Response solve() throws IloException {
        long startTime = System.currentTimeMillis();
        System.out.println(cplex.getModel());
        cplex.setParam(IloCplex.Param.RootAlgorithm, IloCplex.Algorithm.Primal);
        cplex.solve();
        long endTime = System.currentTimeMillis();
        return Response.builder()
                .cost(cplex.getObjValue())
                .time((endTime - startTime) / 1000)
                .build();
    }

    private void buildModel() throws IloException {
        int nodeVarNum = nodeNum * nodeNum * input.getVehicles().size();
        int vehicleVarNum = input.getVehicles().size() * nodeNum;
        int repoVarNum = nodeNum * input.getVehicles().size();
        IloNumVar[] xNodeVars = cplex.boolVarArray(nodeVarNum);
        IloNumVar[] xVehicleVars = cplex.boolVarArray(vehicleVarNum);
        IloNumVar[] xRepoVars = cplex.boolVarArray(repoVarNum);
        IloNumVar[] tNodeVars = cplex.intVarArray(nodeNum * input.getVehicles().size(), 0, TimeUtils.WORKING_TIME_LIMIT);
        IloNumVar[] tVehicleVars = cplex.intVarArray(vehicleVarNum, 0, TimeUtils.WORKING_TIME_LIMIT);
        IloNumVar[] tRepoVars = cplex.intVarArray(input.getVehicles().size(), 0, TimeUtils.WORKING_TIME_LIMIT);
        IloNumVar[] wNodeVars = cplex.intVarArray(nodeNum * input.getVehicles().size(), 0, TimeUtils.WORKING_TIME_LIMIT);
        IloNumVar[] lNodeVars = cplex.intVarArray(nodeNum * input.getVehicles().size(), 0, TimeUtils.WORKING_TIME_LIMIT);
        IloNumVar[] oTimeVars = cplex.boolVarArray(nodeNum * input.getVehicles().size());
        IloNumVar[] oWaitingVars = cplex.boolVarArray(nodeNum * input.getVehicles().size());
        IloNumVar[] oLateVars = cplex.boolVarArray(nodeNum * input.getVehicles().size());
        cplex.addMinimize(this.createObjectiveFunction(xNodeVars, xVehicleVars, xRepoVars, wNodeVars, lNodeVars));
        // c3
        for (int i = 0; i < nodeNum; i++) {
            for (int j = 0; j < input.getVehicles().size(); j++) {
                IloLinearNumExpr comingExpr = this.getComingNodeVarExpr(i, j, xNodeVars, xVehicleVars);
                IloLinearNumExpr leaveExpr = this.getLeavingNodeVarExpr(i, j, xNodeVars, xRepoVars);
                cplex.addEq(comingExpr, leaveExpr);
            }
        }
        // c4
        for (int i = 0; i < xNodeVars.length; i += nodeNum + 1) {
            cplex.addEq(xNodeVars[i], 0);
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
                IloLinearNumExpr comingDeliveryExpr = this.getComingNodeVarExpr(j * 2 + 1, i, xNodeVars, xRepoVars);
                cplex.addEq(comingPickupExpr, comingDeliveryExpr);
            }
        }
        // time constraints
        // start time = 0
        for (int i = 0; i < tVehicleVars.length; i++) {
            cplex.addEq(tVehicleVars[i], 0);
        }
        // time
        for (int i = 0; i < input.getVehicles().size(); i++) {
            for (int j = 0; j < nodeNum; j++) {
                Order.Node node = j % 2 == 0 ? input.getOrders().get(j / 2).getPickup() :
                        input.getOrders().get(j / 2).getDelivery();
                IloNumExpr comingExpr = this.getComingTimeVarExpr(i, j, xNodeVars, tNodeVars, xVehicleVars);
                // t = max(earliest, coming)
                int index = i * nodeNum + j;
                cplex.addGe(tNodeVars[index], comingExpr);
                cplex.addGe(tNodeVars[index], node.getEarliestTime());
                cplex.addLe(tNodeVars[index], cplex.sum(comingExpr,
                        cplex.prod(TimeUtils.WORKING_TIME_LIMIT, cplex.sum(1, cplex.prod(-1, oTimeVars[index])))));
                cplex.addLe(tNodeVars[index], cplex.sum(node.getEarliestTime(),
                        cplex.prod(TimeUtils.WORKING_TIME_LIMIT, oTimeVars[index])));
                // waiting = max(0, earliest - coming)
                IloNumExpr waitingExpr = cplex.sum(cplex.prod(this.getComingNodeVarExpr(j, i, xNodeVars, xVehicleVars),
                        node.getEarliestTime()), cplex.prod(-1, comingExpr));
                cplex.addGe(wNodeVars[index], waitingExpr);
                cplex.addLe(wNodeVars[index], cplex.sum(waitingExpr, cplex.prod(TimeUtils.WORKING_TIME_LIMIT, oWaitingVars[index])));
                cplex.addLe(wNodeVars[index], cplex.prod(TimeUtils.WORKING_TIME_LIMIT, cplex.sum(1, cplex.prod(-1, oWaitingVars[index]))));
                // late = max(0, coming - latest)
                IloNumExpr lateExpr = cplex.sum(comingExpr, cplex.prod(-1, cplex.prod(this.getComingNodeVarExpr(j, i, xNodeVars, xVehicleVars),
                        node.getEarliestTime())));
                cplex.addGe(lNodeVars[index], lateExpr);
                cplex.addLe(lNodeVars[index], cplex.sum(lateExpr, cplex.prod(TimeUtils.WORKING_TIME_LIMIT, oLateVars[index])));
                cplex.addLe(lNodeVars[index], cplex.prod(TimeUtils.WORKING_TIME_LIMIT, cplex.sum(1, cplex.prod(-1, oLateVars[index]))));
            }
        }

    }

    private IloNumExpr createObjectiveFunction(IloNumVar[] nodeVars, IloNumVar[] vehicleVars, IloNumVar[] repoVars,
                                               IloNumVar[] wNodeVars, IloNumVar[] lNodeVar) throws IloException {
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
        IloLinearNumExpr wExpr = cplex.linearNumExpr();
        for (IloNumVar wNodeVar : wNodeVars) {
            wExpr.addTerm(1, wNodeVar);
        }
        IloLinearNumExpr lExpr = cplex.linearNumExpr();
        for (IloNumVar iloNumVar : lNodeVar) {
            lExpr.addTerm(1, iloNumVar);
        }
        return cplex.sum(cplex.prod(input.getCost().getTravel(), tExpr), cplex.sum(cplex.prod(input.getCost().getLate(), lExpr),
                cplex.prod(input.getCost().getWaiting(), wExpr)));
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
        for (int i = nodeOffset + node; i < nodeOffset + nodeNum * nodeNum; i += nodeNum) {
            expr.addTerm(1, nodeVars[i]);
        }
        int vehicleOffset = nodeNum * vehicle;
        expr.addTerm(1, vehicleVars[vehicleOffset + node]);
        return expr;
    }

    private IloLinearNumExpr getLeavingNodeVarExpr(int node, IloNumVar[] nodeVars, IloNumVar[] repoVars) throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();
        for (int i = node * nodeNum; i < nodeVars.length; i += nodeNum) {
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
        for (int i = 0; i < nodeNum; i += nodeNum) {
            expr.addTerm(1, nodeVars[nodeOffset + nodeNum * node + i]);
        }
        int repoOffset = vehicle * nodeNum;
        expr.addTerm(1, repoVars[repoOffset + node]);
        return expr;
    }

    private IloNumExpr getComingTimeVarExpr(int vehicle, int node, IloNumVar[] xNodeVars, IloNumVar[] tNodeVars,
                                            IloNumVar[] xVehicleVars) throws IloException {
        IloQuadNumExpr expr1 = cplex.quadNumExpr();
        IloLinearNumExpr expr2 = cplex.linearNumExpr();
        for (int i = 0; i < nodeNum; i += 1) {
            int xIndex = vehicle * nodeNum * nodeNum + i * nodeNum + node;
            expr1.addTerm(1, tNodeVars[vehicle * nodeNum + i], xNodeVars[xIndex]);
            expr2.addTerm(input.getDuration().getOrderNodeMatrix().get(i).get(node),
                    xNodeVars[xIndex]);


        }
        int index = vehicle * nodeNum + node;
        expr2.addTerm(input.getDuration().getVehicleMatrix().get(vehicle).get(node), xVehicleVars[index]);
        return cplex.sum(expr1, expr2);
    }
}
