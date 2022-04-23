import * as React from "react";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useMemo } from "react";
import { getPolyline, getRoutingByVehicleId, setLocationSelected } from "reduces/routingReducer";
import MDBox from "components/MDBox";
import Grid from "@mui/material/Grid";
import Shipments from "../../../orders/components/Shipments";
import ShipmentDetail from "../../../orders/components/ShipmentDetail";
import ShipmentRouting from "../../../orders/components/shipmentRouting.js";
import LocationInfos from "./LocationInfos";
import useWindowDimensions from "../../../../hooks/useWindowDimensions";
import RoutesVehicleMap from "./RoutesVehicleMap";


export function ModalRoute({ item, open, setOpen }) {
  const { routing, polyline } = useSelector(state => state.routing);
  const windowDimensions = useWindowDimensions();
  const style = useMemo(() => ({
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: windowDimensions.width - 100,
    height: windowDimensions.height - 100,
    bgcolor: "background.paper",
    border: "2px solid #000",
    boxShadow: 24,
    p: 1,
  }), [windowDimensions]);

  const dispatch = useDispatch();
  useEffect(() => {
    if (!!item) {
      dispatch(getRoutingByVehicleId({ vehicleId: item.id }))
        .then(res => {
          if (res.payload) {
            dispatch(getPolyline({ routing: res.payload }));
            dispatch(setLocationSelected(res.payload.nodes?.[0].location));
          }
        });
    }
  }, [item]);
  const handleClose = () => setOpen(false);
  return (
    <div>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"

      >
        <MDBox sx={style}>
          <MDBox>
            <Grid container spacing={1}>
              <Grid item xs={12} md={4}>
                <LocationInfos items={routing?.nodes || []} />
              </Grid>
              <Grid item xs={12} md={8}>
                <RoutesVehicleMap />
              </Grid>
            </Grid>
          </MDBox>
        </MDBox>
      </Modal>
    </div>
  );
}