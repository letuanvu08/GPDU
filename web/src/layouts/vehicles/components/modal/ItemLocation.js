import PropTypes from "prop-types";

// @mui material components
import Icon from "@mui/material/Icon";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";
import ArrowIncrease from "../../../../icons/arrowIncrease";
import { hideApartString } from "../../../../utils/StringUtils";
import moment from "moment";
import { Grid } from "@mui/material";
import TypeNode from "../../../../constants/TypeNode";
import ArrowDecrease from "../../../../icons/arrowDecrease";

function ItemLocation({
                        address,
                        customerName,
                        earliestTime,
                        latestTime,
                        location,
                        phone,
                        typeNode,
                        orderId,
                        onClick,
                      }) {
  const handelOnClick = () => {
    onClick(location);
  };
  return (
    <MDBox
      component="li"
      display="flex"
      justifyContent="space-between"
      alignItems="flex-start"
      bgColor={"grey-100"}
      borderRadius="lg"
      p={1}
      mb={1}
      mt={1}
    >
      <MDBox onClick={handelOnClick} width="100%" display="flex" flexDirection="column">
        <MDBox
          display="flex"
          justifyContent="left"
          alignItems="flex-start"
          flexDirection="column"
        >
          <Grid container display="flex" flexDirection="row" justifyContent="center">
            <Grid item md={1} xl={1} pt={1}>
              {typeNode === TypeNode.PICKUP ? <ArrowIncrease /> : <ArrowDecrease />}
            </Grid>
            <Grid item xs={12} md={11} xl={11} pl={1}>
              <MDTypography variant="button" fontWeight="medium" textTransform="capitalize">
                {hideApartString(address, 40)}
              </MDTypography>
            </Grid>
          </Grid>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            Customer name:&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium" textTransform="capitalize">
              {customerName}
            </MDTypography>
          </MDTypography>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            Phone number:&nbsp;&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium">
              {phone}
            </MDTypography>
          </MDTypography>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            OrderId:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium">
              {orderId}
            </MDTypography>
          </MDTypography>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            Duration:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium">
              {moment(earliestTime,).format("hh:mm")} - {" "}
              {moment(latestTime).format("hh:mm")}
            </MDTypography>
          </MDTypography>
        </MDBox>
      </MDBox>
    </MDBox>
  );
}

export default ItemLocation;
