import rsaApi from "@/api/github/rsa";
import {getApiUrl} from '@/utils/utils'
import permission from '@/directive/permission/index.js'

export default {
  name: 'github',
  directives: {permission},
  data() {
    return {
      formVisible: false,
      formTitle: '生成公钥',
      rsaForm:{},
      listQuery: {
        page: 1,
        limit: 20,
        emailAddress: undefined,
        startDate: undefined,
        endDate: undefined
      },
      //生成后回显的值
      idRsaPublic:'',
      generalEmail:'',
      rangeDate: undefined,
      total: 0,
      list: null,
      listLoading: true,
      selRow: {},
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }
        ]
      }

    }
  },
  computed: {
    rules() {
      return {
        emailAddress: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      let queryData = this.listQuery
      if (this.rangeDate) {
        queryData['startDate'] = this.rangeDate[0]
        queryData['endDate'] = this.rangeDate[1]

      }
      rsaApi.getRsaList(queryData).then(response => {
        this.list = response.data.records
        this.listLoading = false
        this.total = response.data.total
      })
    },
    search() {
      this.listQuery.page = 1
      this.fetchData()
    },
    generalVue() {
      // this.idRsaPublic = '',
      this.formVisible = true
    },
    general() {
      console.info('1111')
      if (this.validateEmail(this.generalEmail)) {
        rsaApi.generalEmail({
          emailAddress: this.generalEmail
        }).then(response => {
          this.$message({
            message: this.$t('common.optionSuccess'),
            type: 'success'
          })
          this.idRsaPublic = response.data
          // this.back()
        })
      } else {
        this.$message({
          message: this.$t('请输入正确的邮箱地址'),
          type: 'success'
        })
        return false
      }
      // this.$refs['form'].validate((valid) => {
      //   if (valid) {
      //     rsaApi.generalEmail({
      //       emailAddress: this.generalEmail
      //     }).then(response => {
      //       this.$message({
      //         message: this.$t('common.optionSuccess'),
      //         type: 'success'
      //       })
      //       this.idRsaPublic = response.data
      //       // this.back()
      //     })
      //   } else {
      //     return false
      //   }
      // })
    },
    validateEmail(email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(email);
    },
    reset() {
      this.listQuery.email = undefined
      this.listQuery.startDate = undefined
      this.listQuery.endDate = undefined
      this.rangeDate = ''
      this.listQuery.page = 1
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleClose() {

    },
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.fetchData()
    },
    changeSize(limit) {
      this.listQuery.limit = limit
      this.fetchData()
    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow
    },
    back() {
      this.$router.go(-1)
    },
  }
}
