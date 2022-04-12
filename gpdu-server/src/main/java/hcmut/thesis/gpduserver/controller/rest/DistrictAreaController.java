package hcmut.thesis.gpduserver.controller.rest;

import hcmut.thesis.gpduserver.models.entity.DistrictArea;
import hcmut.thesis.gpduserver.models.reponse.ApiResponse;
import hcmut.thesis.gpduserver.models.request.districtArea.RequestAddDistrictArea;
import hcmut.thesis.gpduserver.service.DistrictAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/district")
public class DistrictAreaController {

  @Autowired
  private DistrictAreaService districtAreaService;

  @PostMapping("")
  public ApiResponse<DistrictArea> addDistrictArea(@RequestBody RequestAddDistrictArea request) {
    DistrictArea districtArea = districtAreaService.addDistrictArea(request);
    return new ApiResponse<DistrictArea>().success(districtArea);
  }
}
