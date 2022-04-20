import Icon from "@mui/material/Icon";
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";

function OrderInformation({ item }) {
  return (
    <MDBox
      component="li"
      display="flex"
      justifyContent="space-between"
      alignItems="flex-start"
      bgColor={"grey-100"}
      borderRadius="lg"
      p={3}
      mb={1}
      mt={2}
    >
      <MDBox width="100%" display="flex" flexDirection="column">
        <MDBox
          display="flex"
          justifyContent="space-between"
          alignItems={{ xs: "flex-start", sm: "center" }}
          flexDirection={{ xs: "column", sm: "row" }}
          mb={2}
        >
          <MDTypography variant="button" fontWeight="medium" textTransform="capitalize">
            {item?.id}
          </MDTypography>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            Customer Name:&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium" textTransform="capitalize">
              {item.userName}
            </MDTypography>
          </MDTypography>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            Name package:&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium">
              {item.packageInfo?.name}
            </MDTypography>
          </MDTypography>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            Category package:&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium">
              {item.packageInfo?.category}
            </MDTypography>
          </MDTypography>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            Weight package:&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium">
              {item.packageInfo?.weight} Kg
            </MDTypography>
          </MDTypography>
        </MDBox>
        <MDBox mb={1} lineHeight={1}>
          <MDTypography variant="caption" color="text">
            Note:&nbsp;&nbsp;&nbsp;
            <MDTypography variant="caption" fontWeight="medium">
              {item.note}
            </MDTypography>
          </MDTypography>
        </MDBox>
      </MDBox>
    </MDBox>
  );
}

export default OrderInformation;
