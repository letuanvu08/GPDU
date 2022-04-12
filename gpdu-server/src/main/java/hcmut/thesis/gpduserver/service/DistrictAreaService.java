package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.models.entity.DistrictArea;
import hcmut.thesis.gpduserver.models.request.districtArea.RequestAddDistrictArea;

public interface DistrictAreaService {
  DistrictArea getDistrictArea();
  DistrictArea addDistrictArea(RequestAddDistrictArea request);
}
