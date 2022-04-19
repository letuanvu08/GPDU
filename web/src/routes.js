
import OrdersPage from "layouts/orders";
import { Route as RouteIcon} from "icons/route";
import { Order as OrderIcon} from "icons/order";
import AddOrders from "layouts/addOrder";
const routes = [
  {
    type: "collapse",
    name: "Orders",
    key: "orders",
    icon: <OrderIcon fontSize="small"/>,
    route: "/orders",
    component: <OrdersPage />,
  },
  {
    type: "collapse",
    name: "Vehicles",
    key: "addOrders",
    icon: <OrderIcon fontSize="small"/>,
    route: "/addOrders",
    component: <AddOrders />,
  },
];

export default routes;
