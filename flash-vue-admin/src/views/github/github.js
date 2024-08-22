import githubApi from '@/api/github/github'
import {getApiUrl} from '@/utils/utils'
import permission from '@/directive/permission/index.js'

export default {
  name: 'github',
  directives: {permission},
  data() {
    return {
      formVisible: false,
      formTitle: '添加注册记录',
      deptList: [],
      isAdd: true,
      form: {
        id: '',
        emailAddress: '',
        githubName: '',
        repositoryName: '',
        sshUrl: '',
        password: '',
        timeSubmit:undefined
      },

      listQuery: {
        page: 1,
        limit: 20,
        emailAddress: undefined,
        githubName: undefined,
        startDate: undefined,
        endDate: undefined
      },
      rangeDate: undefined,
      total: 0,
      list: null,
      listLoading: true,
      selRow: {},
      pwdType: 'password',
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
        ],
        githubName: [
          {required: true, message: '账户名称不能为空', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'}
        ],
        repositoryName: [
          {required: true, message: '仓库名称不能为空', trigger: 'blur'}
        ],
        sshUrl: [
          {required: true, message: 'ssh链接不能为空', trigger: 'blur'}
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
      githubApi.getList(queryData).then(response => {
        this.list = response.data.records
        this.listLoading = false
        this.total = response.data.total
      })
    },
    search() {
      this.listQuery.page = 1
      this.fetchData()
    },
    save() {
      console.info('1111')
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.validateEmail(this.form.emailAddress)) {
            // const content = this.form.content.split('%').join('%25')
            githubApi.save({
              id: this.form.id,
              emailAddress: this.form.emailAddress,
              githubName: this.form.githubName,
              repositoryName: this.form.repositoryName,
              sshUrl: this.form.sshUrl,
              password: this.form.password
            }).then(response => {
              this.$message({
                message: this.$t(response.msg),
                type: 'success'
              })
              this.formVisible = false;
            })
          } else {
            this.$message({
              message: this.$t('请输入正确的邮箱地址'),
              type: 'success'
            })
            return false
          }
        } else {
          return false
        }
      })
    },
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    reset() {
      this.listQuery.email = undefined
      this.listQuery.accountName = undefined
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
    validateEmail(email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(email);
    },
    back() {
      this.$router.go(-1)
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
    add() {
      this.formTitle = '新增注册记录'
      this.isAdd = true
      this.selRow = {}
      this.form = {}
      this.formVisible = true
    },
    checkSel() {
      if (this.selRow && this.selRow.id) {
        return true
      }
      this.$message({
        message: this.$t('common.mustSelectOne'),
        type: 'warning'
      })
      return false
    },
    editItem(record) {
      console.info(record)
      this.selRow = record
      this.formTitle = '编辑注册记录'
      this.edit()
    },
    edit() {
      if (this.checkSel()) {
        this.isAdd = false
        this.form = this.selRow
        this.formVisible = true
        // this.$router.push({path: '/github/githubEdit', query: {id: this.selRow.id}})
      }
    }

  }
}
