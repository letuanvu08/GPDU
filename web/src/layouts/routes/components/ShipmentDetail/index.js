import { useMemo, useState, useEffect } from "react";
import Card from "@mui/material/Card";
import { Grid } from "@mui/material";
import AppBar from "@mui/material/AppBar";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import Icon from "@mui/material/Icon";
import breakpoints from "assets/theme/base/breakpoints";
import TimelineIcon from "@mui/icons-material/Timeline";
import { Order as OrderIcon } from "icons/order";
import ShipmentStatus from "./ShipmentStatus";
const TapValues = {
  SHIPMENT_STATUS: 0,
  ORDER_INFORMATION: 1,
};
function ShipmentDetail({ id, vehicle, order }) {
  const [tabsOrientation, setTabsOrientation] = useState("horizontal");
  const [tabValue, setTabValue] = useState(TapValues.SHIPMENT_STATUS);

  useEffect(() => {
    // A function that sets the orientation state of the tabs.
    function handleTabsOrientation() {
      return window.innerWidth < breakpoints.values.sm
        ? setTabsOrientation("vertical")
        : setTabsOrientation("horizontal");
    }

    /** 
     The event listener that's calling the handleTabsOrientation function when resizing the window.
    */
    window.addEventListener("resize", handleTabsOrientation);

    // Call the handleTabsOrientation function to set the state with the initial value.
    handleTabsOrientation();

    // Remove event listener on cleanup
    return () => window.removeEventListener("resize", handleTabsOrientation);
  }, [tabsOrientation]);
  const handleSetTabValue = (event, newValue) => {
    setTabValue(newValue);
  };

  return (
    <Card>
      <Grid container spacing={3} justifyContent="center">
        <Grid item sx={{ width: "100%" }}>
          <AppBar position="static">
            <Tabs orientation={tabsOrientation} value={tabValue} onChange={handleSetTabValue}>
              <Tab value={TapValues.SHIPMENT_STATUS} label="Status" icon={<TimelineIcon />} />
              <Tab
                value={TapValues.ORDER_INFORMATION}
                label="Information"
                icon={<OrderIcon fontSize="small" />}
              />
            </Tabs>
          </AppBar>
        </Grid>
        <Grid item sx={{ width: "100%" }}>
          {tabValue === TapValues.SHIPMENT_STATUS ? (
            <ShipmentStatus statusList={statusList}/>
          ) : tabValue === TapValues.ORDER_INFORMATION ? (
            <Grid>order infor</Grid>
          ) : (
            <Grid>order infor</Grid>
          )}
        </Grid>
      </Grid>
    </Card>
  );
}
const statusList = [
  {
    title: "confirm order #1234",
    status: "success",
    createdTime: 1648374018943,
  },
  {
    title:"Assign vehicle",
    status:"success",
    createdTime: 1648374018943,
  },
  {
    title:"Pickup package",
    status:"cancel",
    createdTime: 1648374018943,
  }
];
// Setting default values for the props of ReportsBarChart
ShipmentDetail.defaultProps = {
  color: "dark",
  description: "",
};

// Typechecking props for the ReportsBarChart

export default ShipmentDetail;
