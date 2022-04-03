import Card from "@mui/material/Card";
import Icon from "@mui/material/Icon";
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

import TimelineItem from "components/Timeline/TimelineItem";
import Check from "icons/check";
import Cancel from "icons/cancel";
import StatusShipmentEnum from "constants/StatusShipmentEnum";
import moment from "moment";
function OrdersOverview({ statusList }) {
  console.log(StatusShipmentEnum)
  return (
    <Card sx={{ height: "100%", width: "100%" }}>
      <MDBox pl={4} pr={2} py={2}>
        {!!statusList &&
          statusList.map((status) => (
            <TimelineItem
              color={status.status === StatusShipmentEnum.CANCEL ? "error" : "success"}
              icon={
                status.status === StatusShipmentEnum.SUCCESS ? (
                  <Check />
                ) : status.status === StatusShipmentEnum.CANCEL ? (
                  <Cancel />
                ) : (
                  ""
                )
              }
              title={status.title}
              dateTime={status.createdTime && moment(status.createdTime).format("MMMM Do YYYY, h:mm:ss a")}
            />
          ))}
      </MDBox>
    </Card>
  );
}

export default OrdersOverview;
