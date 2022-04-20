import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import MDBox from "components/MDBox";
import DashboardLayout from "components/LayoutContainers/DashboardLayout";
import DashboardNavbar from "components/Navbars/DashboardNavbar";
import DataTable from "./components/DataTable";
import VehicleTableData from "layouts/vehicles/data/VehicleTableData";
import MDTypography from "components/MDTypography";
import { useSelector, useDispatch } from "react-redux";
import { fetchVehicles } from "../../reduces/vehiclesReducer";
import { setSelectedVehicle } from "../../reduces/vehiclesReducer";
import { useEffect } from "react";

function VehicleScreen() {
  const dispatch = useDispatch();
  const { vehicles, offset, limit, vehicleSelected } = useSelector(state => state.vehicles);
  const { columns, rows } = VehicleTableData({ vehicles: vehicles });
  useEffect(() => {
    dispatch(fetchVehicles({ offset: offset, limit: limit }));
  }, []);
  useEffect(() => {
    console.log("vehicles: ", vehicles);
    console.log("offset: ", offset);
    console.log("limit: ", limit);
    console.log("rows:", rows)
  }, [vehicles]);

  const handleSelectVehicle = (item)=>{
    dispatch(setSelectedVehicle(item))
  }
  return (
    <DashboardLayout>
      <DashboardNavbar />
      <MDBox pt={6} pb={3}>
        <Grid container spacing={6}>
          <Grid item xs={12}>
            <Card>
              <MDBox
                mx={2}
                mt={-3}
                py={3}
                px={2}
                variant="gradient"
                bgColor="info"
                borderRadius="lg"
                coloredShadow="info"
              >
                <MDTypography variant="h6" color="white">
                  Vehicle Manager
                </MDTypography>
              </MDBox>
              <MDBox pt={3}>
                <DataTable
                  table={{ columns, rows }}
                  isSorted={true}
                  entriesPerPage={true}
                  showTotalEntries={true}
                  canSearch={true}
                  onClickRow={handleSelectVehicle}
                />
              </MDBox>
            </Card>
          </Grid>
          <Grid item xs={12}>

          </Grid>
        </Grid>
      </MDBox>
    </DashboardLayout>
  );
}

export default VehicleScreen;
