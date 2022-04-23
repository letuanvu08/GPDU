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
import { hideApartString } from "utils/StringUtils";
function Shipment({ item, onViewDetail }) {

  const handleOnViewDetail= ()=>{
     onViewDetail(item);
  }
  return (
    <Card>
      <MDBox padding="1rem">
        <MDBox>
          <MDBox pt={1} px={1} display="flex" justifyContent="space-between" alignItems="center">
            <MDBox>
              <MDTypography component="div" variant="button" color="text" fontWeight="light">
                OrderId
              </MDTypography>
              <MDTypography variant="h6" textTransform="capitalize">
                {item.id}
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
                  {hideApartString(item.pickup.address,30)}
                </MDTypography>
                <MDTypography component="div" variant="button" color="text" fontWeight="light">
                  {moment(item.pickup.earliestTime).format("hh:mm")} -{" "}
                  {moment(item.pickup.latestTime).format("hh:mm")}
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
                {hideApartString(item.delivery.address,30)}
                </MDTypography>
                <MDTypography component="div" variant="button" color="text" fontWeight="light">
                  {moment(item.delivery.earliestTime).format("hh:mm")} -{" "}
                  {moment(item.delivery.latestTime).format("hh:mm")}
                </MDTypography>
              </Grid>
            </Grid>
          </MDBox>
          <Divider />
          <MDBox p={1} display="flex" alignItems="left" flexDirection="column">
            <MDTypography component="div" variant="button" color="text" fontWeight="light">
              Customer
            </MDTypography>
            <MDTypography variant="h6" textTransform="capitalize">
              {item.userName}
            </MDTypography>
            {/* <MDTypography component="div" variant="button" color="text" fontWeight="light">
              {item.customer.phone}
            </MDTypography> */}
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
              onClick={handleOnViewDetail}
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
