import React, { useEffect } from "react";
import Grid from "@mui/material/Grid";
import MDBox from "../../components/MDBox";
import DashboardLayout from "components/LayoutContainers/DashboardLayout";
import DashboardNavbar from "components/Navbars/DashboardNavbar";
import ShipmentDetail from "./components/ShipmentDetail";
import Shipments from "./components/Shipments";
import ShipmentRouting from "./components/shipmentRouting.js/index.js";
import { useDispatch, useSelector } from "react-redux";
export default function OrdersPage() {
  const { itemSelected } = useSelector((state) => state.orders);
  return (
    <DashboardLayout>
      <DashboardNavbar absolute isMini />
      <MDBox mt={8}>
        <MDBox mb={3}>
          <Grid container spacing={1}>
            <Grid item xs={12} md={7}>
              <Shipments />
            </Grid>
            <Grid item xs={12} md={5}>
              <Grid item pb={1}>
                {itemSelected && <ShipmentDetail item={itemSelected} />}
              </Grid>
              <Grid item>{itemSelected && <ShipmentRouting item={itemSelected} />}</Grid>
            </Grid>
          </Grid>
        </MDBox>
      </MDBox>
    </DashboardLayout>
  );
}
