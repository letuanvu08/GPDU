import Card from "@mui/material/Card";
// Material Dashboard 2 React components
import MDBox from "../../../../components/MDBox";
import MDTypography from "../../../../components/MDTypography";
import { useDispatch, useSelector } from "react-redux";
import ItemLocation from "./ItemLocation";
import { createTheme, Paper } from "@mui/material";
import { ThemeProvider } from "@mui/material/styles";
import List from "@mui/material/List";
import useWindowDimensions from "../../../../hooks/useWindowDimensions";
import { setLocationSelected } from "../../../../reduces/routingReducer";

function LocationInfos({ items }) {
  const dispatch = useDispatch();
  const window = useWindowDimensions();
  const handleOnViewDetail = (item) => {
    dispatch(setLocationSelected(item));
  };
  return (
    <Card id="delete-account" sx={{ overflow: "", height: "100%" }}>
      <MDBox pt={3} px={2} pb={3}>
        <MDTypography variant="h6" fontWeight="medium">
          Request Information
        </MDTypography>
      </MDBox>
      <Paper style={{ maxHeight: window.height - 190, overflow: "auto" }}>
        <List>

          <MDBox pt={1} pb={2} px={2}>
            <MDBox component="ul" display="flex" flexDirection="column" p={0} m={0}>

              {items && items.map((item, index) =>
                <ItemLocation key={index} {...item} onClick={handleOnViewDetail} />,
              )}

            </MDBox>
          </MDBox>
        </List>
      </Paper>
    </Card>
  );
}

const theme = createTheme({
  overrides: {
    MuiCssBaseline: {
      "@global": {
        "*::-webkit-scrollbar": {
          width: "10px",
        },
        "*::-webkit-scrollbar-track": {
          background: "#E4EFEF",
        },
        "*::-webkit-scrollbar-thumb": {
          background: "#1D388F61",
          borderRadius: "2px",
        },
      },
    },
  },
});

export default LocationInfos;
