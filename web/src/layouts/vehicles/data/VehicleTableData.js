import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDAvatar from "components/MDAvatar";
import MDBadge from "components/MDBadge";

// Images
import team2 from "assets/images/team-2.jpg";
import team3 from "assets/images/team-3.jpg";
import team4 from "assets/images/team-4.jpg";
import truck from "icons/delivery-truck-5245.png";
import { Image } from "react-bootstrap";

export default function VehicleTableData({ vehicles }) {
  const Id = ({ image, id }) => (
    <MDBox display="flex" alignItems="center" lineHeight={1}>
      <Image src={image} style={{ width: 32, height: 32 }} />
      <MDBox ml={2} lineHeight={1}>
        <MDTypography  component="a" href="#" variant="caption" color="text" fontWeight="medium">
          {id}
        </MDTypography>
      </MDBox>
    </MDBox>
  );

  const renderRows = (vehicle) => {
    return {
      id: <Id image={truck} id={vehicle.id} />,
      ownerId:
        <MDBox ml={-1}>
          <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
            {vehicle.ownerId}
          </MDTypography>
        </MDBox>,
      type: (
        <MDBox ml={-1}>
          <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
            {vehicle.type}
          </MDTypography>
        </MDBox>
      ),
      capacity: (
        <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
          {vehicle.capacity}
        </MDTypography>
      ),
      volume: (
        <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
          {vehicle.volume}
        </MDTypography>
      ),
      vehicle: vehicle,
    };
  };

  return {
    columns: [
      { Header: "Id", accessor: "id", width: "25%", align: "left" },
      { Header: "OwnerId", accessor: "ownerId", align: "left" },
      { Header: "type", accessor: "type", align: "center" },
      { Header: "Capacity", accessor: "capacity", align: "center" },
      { Header: "Volume", accessor: "volume", align: "center" },
      { Header: "Vehicle", accessor: "vehicle", align: "center",  },
    ],

    rows: vehicles.map(vehicle => renderRows(vehicle)),
  };
}
