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
import OrderInformation from "./OrderInformation";
const TapValues = {
  SHIPMENT_STATUS: 0,
  ORDER_INFORMATION: 1,
};
function ShipmentDetail({ item }) {
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
      <Grid container justifyContent="center">
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
            <ShipmentStatus statusList={item.historyStatus} />
          ) : tabValue === TapValues.ORDER_INFORMATION ? (
            <OrderInformation item={item} />
          ) : (
            <Grid>order infor</Grid>
          )}
        </Grid>
      </Grid>
    </Card>
  );
}

// Typechecking props for the ReportsBarChart

export default ShipmentDetail;
