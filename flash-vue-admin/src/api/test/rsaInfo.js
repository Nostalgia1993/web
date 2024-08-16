import request from '@/utils/request'
export default {
    getList:function(params) {
        return request({
            url: '/rsa/info/list',
            method: 'get',
            params
        })
    },
    add:function(params) {
        return request({
            url: '/rsa/info',
            method: 'post',
            data: params
        })
    },
    update:function(params) {
        return request({
            url: '/rsa/info',
            method: 'PUT',
            data: params
        })
    },
    remove:function(id) {
        return request({
            url: '/rsa/info',
            method: 'delete',
            params: {
                id: id
            }
        })
    }
}
