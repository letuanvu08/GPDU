import MDBox from "../MDBox";
import MDTypography from "../MDTypography";
import Grid from "@mui/material/Grid";

export function MarkNumber({ number }) {
  return (
    <Grid
      container
      spacing={0}
      direction="column"
      alignItems="center"
      justifyContent="center"
      sx={{ backgroundColor: "red" }}
      borderRadius={20}
      width={24}
      height={24}>
      <MDTypography alignContent="center" color="white"
                    variant="caption" fontWeight="medium">
        {number}
      </MDTypography>
    </Grid>
  );
}