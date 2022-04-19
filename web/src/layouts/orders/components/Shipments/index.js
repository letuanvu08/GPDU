import Card from "@mui/material/Card";
// Material Dashboard 2 React components
import MDBox from "../../../../components/MDBox";
import MDTypography from "../../../../components/MDTypography";
import MDButton from "../../../../components/MDButton";
import Shipment from "../Shipment";
import Grid from "@mui/material/Grid";
import { Pagination } from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import mapboxApi from "api/mapboxAPI";
import { setOrderSelected, addListOrder } from "reduces/ordersReducer";
import { useEffect, useState } from "react";
import orderApi from "api/orderApi";
function Shipments() {
  const { items, hasNext, offset, limit } = useSelector((state) => state.orders);
  const [page, setPage] = useState(1);
  const dispatch = useDispatch();
  useEffect(() => {
    fetchOrders({ offset: offset, limit: limit });
  }, [page]);

  const handleChangePage = (_, v) => {
    if (v > page && hasNext) {
      fetchOrders({offset: offset, limit: limit});
    }
    setPage(v);
    console.log("page: ", v);
  };
  const fetchOrders = ({ offset, limit }) => {
    orderApi
      .fetchListOrders({ offset: offset, limit: limit })
      .then((res) => {
        console.log(res.Data);
        dispatch(addListOrder(res.Data));
      })
      .catch((e) => console.log(e));
  };
  const handleOnViewDetail = (item)=>{
    console.log("sdfdsfdsgdgdgdsgds:", item);
    dispatch(setOrderSelected({item:item}));
  }
  return (
    <Card id="shipments">
      <MDBox pt={2} px={3}>
        <MDTypography variant="h6" fontWeight="medium">
          Orders Manager
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
            <MDButton variant="outlined" color="info" size="small" >
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
        <Grid
          p={1}
          container
          spacing={2}
          display="flex"
          flexDirection="row"
          justifyContent="center"
        >
          {items.slice((page-1) * limit, page * limit).map((item) => (
            <Grid item xs={12} md={6} xl={6} sx={{}}>
              <Shipment item={item} onViewDetail={handleOnViewDetail} />
            </Grid>
          ))}
        </Grid>
      </MDBox>
      <Grid sx={{}} display="flex" justifyContent="center">
        <Pagination count={5} page={page} shape="rounded" onChange={handleChangePage} />
      </Grid>
    </Card>
  );
}

export default Shipments;
