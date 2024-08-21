import request from '@/utils/request'
export  default {
  getRsaList:function (params) {
    return request({
      url: '/github/rsaList',
      method: 'get',
      params
    })
  },
  generalEmail:function (params) {
    return request({
      url: '/github/generalRsa',
      method: 'get',
      params
    })
  },
}
