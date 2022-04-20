import OrdersPage from "layouts/orders";
import { Route as RouteIcon } from "icons/route";
import { Order as OrderIcon } from "icons/order";
import AddOrders from "layouts/addOrder";
import { Truck } from "icons/truck";
import VehicleScreen from "layouts/vehicles";
const routes = [
  {
    type: "collapse",
    name: "Orders",
    key: "orders",
    icon: <OrderIcon fontSize="small" />,
    route: "/orders",
    component: <OrdersPage />,
  },
  {
    type: "collapse",
    name: "Vehicles",
    key: "vehicles",
    icon: <Truck fontSize="small" />,
    route: "/vehicles",
    component: <VehicleScreen />,
  },
];

export default routes;
