import APIPathEnum from '~/constants/APIPathEnum';
import httpClient from '../httpClient';
;
export default userApi = {
  login: ({userName, password}) => httpClient.post(APIPathEnum.LOGIN, {userName, password}),
  refreshToken: body => httpClient.post(APIPathEnum.REFRESH_TOKEN, body),
  register: ({userName, password, email, type}) => httpClient.post(APIPathEnum.REGISTER, {userName, password, email, type}),
  getProfile: () => httpClient.get(APIPathEnum.PROFILE)
};