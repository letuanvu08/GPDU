
import RoutePage from "layouts/routes";
import { Route as RouteIcon} from "icons/route";
import { Order as OrderIcon} from "icons/order";
import AddOrders from "layouts/addOrder";
const routes = [
  {
    type: "collapse",
    name: "Routes",
    key: "routes",
    icon: <RouteIcon fontSize="small"/>,
    route: "/routes",
    component: <RoutePage />,
  },
  {
    type: "collapse",
    name: "Add orders",
    key: "addOrders",
    icon: <OrderIcon fontSize="small"/>,
    route: "/addOrders",
    component: <AddOrders />,
  },
];

export default routes;
