import { ChartBar as ChartBarIcon } from "../icons/chart-bar";
import { Cog as CogIcon } from "../icons/cog";
import { Lock as LockIcon } from "../icons/lock";
import { ShoppingBag as ShoppingBagIcon } from "../icons/shopping-bag";
import { User as UserIcon } from "../icons/user";
import { UserAdd as UserAddIcon } from "../icons/user-add";
import { Users as UsersIcon } from "../icons/users";
import { XCircle as XCircleIcon } from "../icons/x-circle";
import { Order as OrderIcon} from "../icons/order";
import { Route as RouteIcon} from "../icons/route";
import { Truck as TruckIcon} from "../icons/truck";
export const ItemNavigation = [
  // {
  //   href: "/",
  //   icon: <ChartBarIcon fontSize="small" />,
  //   title: "Dashboard",
  // },
  {
    href: "/routes",
    icon: <RouteIcon fontSize="small" />,
    title: "Routes",
  },
  {
    href: "/vehicles",
    icon: <TruckIcon fontSize="small" />,
    title: "vehicles",
  },
  {
    href: "/orders",
    icon: <OrderIcon fontSize="small" />,
    title: "Orders",
  },
  // {
  //   href: "/account",
  //   icon: <UserIcon fontSize="small" />,
  //   title: "Account",
  // },
  // {
  //   href: "/settings",
  //   icon: <CogIcon fontSize="small" />,
  //   title: "Settings",
  // },
]