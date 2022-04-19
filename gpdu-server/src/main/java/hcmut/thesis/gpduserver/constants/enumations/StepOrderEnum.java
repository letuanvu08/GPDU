package hcmut.thesis.gpduserver.constants.enumations;

public enum StepOrderEnum {
  ORDER_RECEIVED("Order Received"),
  ASSIGNED("Assign to Driver"),
  PICKUP_PACKAGE("Pickup package"),
  DELIVERED("Delivered"),
  Done("Done");

  private final String label;

  public String getLabel() {
    return label;
  }

  StepOrderEnum(String label) {
    this.label = label;
  }
}
