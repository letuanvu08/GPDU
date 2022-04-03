import Card from "@mui/material/Card";
// Material Dashboard 2 React components
import MDBox from "../../../../components/MDBox";
import MDTypography from "../../../../components/MDTypography";
import MDButton from "../../../../components/MDButton";
import Shipment from "../Shipment";
import Grid from "@mui/material/Grid";
function Shipments() {
  return (
    <Card id="shipments">
      <MDBox pt={2} px={3}>
        <MDTypography variant="h6" fontWeight="medium">
          Shipment Manager
        </MDTypography>
      </MDBox>
      <MDBox
        display="flex"
        justifyContent="space-between"
        alignItems={{ xs: "flex-start", sm: "center" }}
        flexDirection={{ xs: "column", sm: "row" }}
        pt={1}
        px={2}
      >
        <MDBox display="flex" alignItems="center" mt={{ xs: 2, sm: 0 }} ml={{ xs: -1.5, sm: 0 }}>
          <MDBox mr={2}>
            <MDButton variant="outlined" color="info" size="small">
              view all
            </MDButton>
          </MDBox>
          <MDBox mr={2}>
            <MDButton variant="outlined" color="info" size="small">
              Active
            </MDButton>
          </MDBox>
        </MDBox>
      </MDBox>
      <MDBox>
        <Grid p={1} container spacing={2} display="flex" flexDirection="row" justifyContent="center">
            <Grid item xs={12} md={6} xl={6}  >
             <Shipment id={shipmentFake.id} order={shipmentFake.order} vehicle = {shipmentFake.vehicle}/>
            </Grid>
            <Grid item xs={12} md={6} xl={6} >
             <Shipment id={shipmentFake.id} order={shipmentFake.order} vehicle = {shipmentFake.vehicle}/>
            </Grid>
            <Grid item xs={12} md={6} xl={6}  >
             <Shipment id={shipmentFake.id} order={shipmentFake.order} vehicle = {shipmentFake.vehicle}/>
            </Grid>
            <Grid item xs={12} md={6} xl={6} >
             <Shipment id={shipmentFake.id} order={shipmentFake.order} vehicle = {shipmentFake.vehicle}/>
            </Grid>
        </Grid>
      </MDBox>
    </Card>
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
export default Shipments;
