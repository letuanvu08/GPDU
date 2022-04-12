package hcmut.thesis.gpduserver.service.impl;

import hcmut.thesis.gpduserver.models.entity.DistrictArea;
import hcmut.thesis.gpduserver.models.request.districtArea.RequestAddDistrictArea;
import hcmut.thesis.gpduserver.repository.DistrictAreaRepository;
import hcmut.thesis.gpduserver.service.DistrictAreaService;
import hcmut.thesis.gpduserver.utils.GsonUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DistrictAreaServiceImpl implements DistrictAreaService {

  @Value("${districtName}")
  private String districtName;

  @Autowired
  private DistrictAreaRepository districtAreaRepository;

  @Override
  public DistrictArea getDistrictArea() {
    try {
      Optional<DistrictArea> districtArea = districtAreaRepository.getById(districtName);
      log.info("getDistrictArea, districtName: {}, result: {}", districtName,
          GsonUtils.toJsonString(districtArea.orElse(null)));
      return districtArea.orElse(null);
    } catch (Exception e) {
      log.error("Error when getDistrictArea, districtName: {}, exception: {}", districtName,
          e.getMessage());
      return null;
    }
  }

  @Override
  public DistrictArea addDistrictArea(RequestAddDistrictArea request) {
    try {
      DistrictArea districtAreaRequest = DistrictArea.builder()
          .boundary(request.getBoundary())
          .districtName(districtName)
          .storages(request.getStorages())
          .build();
      Optional<DistrictArea> districtArea = districtAreaRepository.insert(districtAreaRequest);
      log.info("addDistrictArea, districtName: {},request: {}, result: {}",
          districtName, GsonUtils.toJsonString(request),
          GsonUtils.toJsonString(districtArea.orElse(null)));
      return districtArea.orElse(null);
    } catch (Exception e) {
      log.error("Error when getDistrictArea, districtName: {},request: {}, exception: {}",
          districtName, GsonUtils.toJsonString(request), e.getMessage());
      return null;
    }

  }
}
