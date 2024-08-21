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
}
