import { useMemo } from "react";

// porp-types is a library for typechecking of props
import PropTypes from "prop-types";

// react-chartjs-2 components

// @mui material components
import Card from "@mui/material/Card";
import Divider from "@mui/material/Divider";
import MDBox from "components/MDBox";
import MDButton from "components/MDButton";
import MDTypography from "components/MDTypography";
import { Truck } from "icons/truck";
import ArrowIncrease from "icons/arrowIncrease";
import ArrowDecrease from "icons/arrowDecrease";
import { Grid } from "@mui/material";
import moment from "moment";
function Shipment({ id, vehicle, order }) {
  return (
    <Card>
      <MDBox padding="2rem">
        <MDBox>
          <MDBox pt={1} px={1} display="flex" justifyContent="space-between" alignItems="center">
            <MDBox>
              <MDTypography component="div" variant="button" color="text" fontWeight="light">
                Shipment number
              </MDTypography>
              <MDTypography variant="h6" textTransform="capitalize">
                {id}
              </MDTypography>
            </MDBox>
            <Truck fontSize="medium" />
          </MDBox>
          <Divider />
          <MDBox display="flex" alignItems="left" flexDirection="column">
            <Grid container display="flex" flexDirection="row" justifyContent="center">
              <Grid
                item
                xs={12}
                md={2}
                xl={2}
                p={1}
                display="flex"
                flexDirection="column"
                justifyContent="center"
              >
                <ArrowIncrease />
              </Grid>
              <Grid item xs={12} md={10} xl={10} px={1}>
                <MDTypography variant="h6" textTransform="capitalize">
                  {order.pickup.address}
                </MDTypography>
                <MDTypography component="div" variant="button" color="text" fontWeight="light">
                  {moment(order.pickup.earliestTime).format("hh:mm")} -{" "}
                  {moment(order.pickup.latestTime).format("hh:mm")}
                </MDTypography>
              </Grid>
            </Grid>
            <Grid container display="flex" flexDirection="row" justifyContent="center">
              <Grid
                item
                xs={12}
                md={2}
                xl={2}
                p={1}
                display="flex"
                flexDirection="column"
                justifyContent="center"
              >
                <ArrowDecrease />
              </Grid>
              <Grid item xs={12} md={10} xl={10} px={1}>
                <MDTypography variant="h6" textTransform="capitalize">
                  {order.delivery.address}
                </MDTypography>
                <MDTypography component="div" variant="button" color="text" fontWeight="light">
                  {moment(order.delivery.earliestTime).format("hh:mm")} -{" "}
                  {moment(order.delivery.latestTime).format("hh:mm")}
                </MDTypography>
              </Grid>
            </Grid>
          </MDBox>
          <Divider />
          <MDBox p={2} display="flex" alignItems="left" flexDirection="column">
            <MDTypography component="div" variant="button" color="text" fontWeight="light">
              Customer
            </MDTypography>
            <MDTypography variant="h6" textTransform="capitalize">
              {order.customer.name}
            </MDTypography>
            <MDTypography component="div" variant="button" color="text" fontWeight="light">
              {order.customer.phone}
            </MDTypography>
          </MDBox>
        </MDBox>
        <MDBox display="flex" justifyContent="center" alignItems="center">
            <MDButton
              component="a"
              target="_blank"
              rel="noreferrer"
              variant="outlined"
              size="small"
              color= "info"
            >
              View detail
            </MDButton>
        </MDBox>
      </MDBox>
    </Card>
  );
}

// Setting default values for the props of ReportsBarChart
Shipment.defaultProps = {
  color: "dark",
  description: "",
};

// Typechecking props for the ReportsBarChart

export default Shipment;
