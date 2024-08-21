import request from '@/utils/request'
export  default {
  getList: function (params) {
    return request({
      url: '/github/list',
      method: 'get',
      params
    })
  },
  save: function (params) {
    return request({
      url: '/github/save',
      method: 'post',
      data: params
    })
  },
  getRsaList:function (params) {
    return request({
      url: '/github/rsaList',
      method: 'get',
      params
    })
  },
  generalEmail:function (params) {
    return request({
      url: '/github/generalEmail',
      method: 'get',
      params
    })
  },
}
