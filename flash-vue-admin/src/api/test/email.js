import request from '@/utils/request'
export default {
    getList:function(params) {
        return request({
            url: '/email/list',
            method: 'get',
            params
        })
    },
    add:function(params) {
        return request({
            url: '/email',
            method: 'post',
            data: params
        })
    },
    update:function(params) {
        return request({
            url: '/email',
            method: 'PUT',
            data: params
        })
    },
    remove:function(id) {
        return request({
            url: '/email',
            method: 'delete',
            params: {
                id: id
            }
        })
    }
}
