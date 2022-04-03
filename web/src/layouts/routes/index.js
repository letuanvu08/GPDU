import React from "react";
import Grid from "@mui/material/Grid";
import MDBox from "../../components/MDBox";
import DashboardLayout from "components/LayoutContainers/DashboardLayout";
import DashboardNavbar from "components/Navbars/DashboardNavbar";
import ShipmentDetail from "./components/ShipmentDetail";
// Billing page components
import Shipments from "./components/Shipments";
import ShipmentRouting from "./components/shipmentRouting.js";
export default function RoutePage() {
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
                <ShipmentDetail
                  id={shipmentFake.id}
                  order={shipmentFake.order}
                  vehicle={shipmentFake.vehicle}
                />
              </Grid>
              <Grid item >
                <ShipmentRouting/>
              </Grid>
            </Grid>
          </Grid>
        </MDBox>
      </MDBox>
    </DashboardLayout>
  );
}
const shipmentFake = {
  id: "0001",
  order: {
    pickup: {
      address: "Q7, HCM",
      earliestTime: 1648351524504,
      latestTime: 1648351587579,
    },
    delivery: {
      address: "Q7, HCM",
      earliestTime: 1648351524504,
      latestTime: 1648351587579,
    },
    customer: {
      name: "Tuan vu",
      phone: "0919523753",
    },
  },
  vehicle: {
    id: "123",
    type: "truck",
  },
};
