import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDAvatar from "components/MDAvatar";
import MDBadge from "components/MDBadge";

// Images
import truck from "icons/shipper.png";
import { Image } from "react-bootstrap";

export default function CustomerTableData({ customers }) {
  const Id = ({ image, id }) => (
    <MDBox display="flex" alignItems="center" lineHeight={1}>
      <Image src={image} style={{ width: 32, height: 32 }} />
      <MDBox ml={2} lineHeight={1}>
        <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
          {id}
        </MDTypography>
      </MDBox>
    </MDBox>
  );

  const renderRows = (customer) => {
    return {
      id: <Id image={truck} id={customer.id} />,
      userName:
        <MDBox ml={-1}>
          <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
            {customer.userName}
          </MDTypography>
        </MDBox>,
      email: (
        <MDBox ml={-1}>
          <MDTypography component="a" href="#" variant="caption" color="text" fontWeight="medium">
            {customer.email}
          </MDTypography>
        </MDBox>
      ),

      customer: customer,
    };
  };

  return {
    columns: [
      { Header: "Id", accessor: "id", width: "25%", align: "left" },
      { Header: "Username", accessor: "ownerId", align: "left" },
      { Header: "email", accessor: "type", align: "center" },
    ],
    rows: customers.map(customer => renderRows(customer)),
  };
}
