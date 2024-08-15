import request from '@/utils/request'
export  default {
  getList: function (params) {
    return request({
      url: '/github/list',
      method: 'post',
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
