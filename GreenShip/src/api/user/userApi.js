import httpClient from '../httpClient';
import APIPathEnum from '../../constants/ApiPathEnum'
;
export default userApi = {
  login: ({userName, password}) => httpClient.post(APIPathEnum.LOGIN, {userName, password}),
  refreshToken: body => httpClient.post(APIPathEnum.REFRESH_TOKEN, body),
  register: ({userName, password, fullName}) => httpClient.post(APIPathEnum.REGISTER, {userName, password, fullName}),
  getProfile: () => httpClient.get(APIPathEnum.PROFILE)
};