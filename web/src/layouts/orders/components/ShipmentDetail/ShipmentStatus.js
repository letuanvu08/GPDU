import Card from "@mui/material/Card";
import Icon from "@mui/material/Icon";
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

import TimelineItem from "components/Timeline/TimelineItem";
import Check from "icons/check";
import Cancel from "icons/cancel";
import StatusShipmentEnum from "constants/StatusShipmentEnum";
import UnfinishedIcon from "icons/UnfinishedIcon";
import UnCheck from "icons/unCheck";
import moment from "moment";

const otherSteps = [
  {
    step: "Order Received",
  },
  {
    step: "Assign to Driver",
  },
  {
    step: "Pickup package",
  },
  {
    step: "Delivered",
  },
  {
    step: "Done",
  },
];

function OrdersOverview({ statusList }) {
  console.log("status", statusList);
  return (
    <Card sx={{ height: "100%", width: "100%" }}>
      <MDBox pl={4} pr={2} py={2}>
        {!!statusList &&
          statusList.map((status) => (
            <TimelineItem
              color={status.status === StatusShipmentEnum.CANCEL ? "error" : "success"}
              icon={
                status.status === StatusShipmentEnum.FINISHED ? (
                  <Check />
                ) : status.status === StatusShipmentEnum.CANCEL ? (
                  <Cancel />
                ) : (
                  ""
                )
              }
              title={status.step}
              dateTime={
                status.timestamp && moment(status.timestamp).format("MMMM Do YYYY, h:mm:ss a")
              }
            />
          ))}
        {otherSteps.slice(statusList?.length || 0, otherSteps.length).map((status) => (
          <TimelineItem
            color={status.status === StatusShipmentEnum.CANCEL ? "error" : "success"}
            icon={<UnfinishedIcon/>}
            title={status.step}
            dateTime={
              status.timestamp && moment(status.timestamp).format("MMMM Do YYYY, h:mm:ss a")
            }
          />
        ))}
      </MDBox>
    </Card>
  );
}

export default OrdersOverview;
