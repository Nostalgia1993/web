<template>
  <div class="app-container">
    <div class="block">
      <el-row  :gutter="20">
        <el-col :span="6">
          <el-input v-model="listQuery.emailAddress" size="mini" placeholder="邮箱"></el-input>
        </el-col>
        <el-col :span="6">
          <el-input v-model="listQuery.githubName" size="mini" placeholder="账户名称"></el-input>
        </el-col>
        <el-col :span="8">
          <el-date-picker
            v-model="rangeDate"
            size="mini"
            type="datetimerange"
            :picker-options="pickerOptions"
            range-separator="至"
            start-placeholder="发布起始日期"
            end-placeholder="发布截至日期"
            value-format="yyyyMMddHHmmss"
            align="right">
          </el-date-picker>
        </el-col>
        <el-col :span="4">
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-refresh" @click.native="reset">{{ $t('button.reset') }}</el-button>
        </el-col>
      </el-row>
      <br>
      <el-row>
        <el-col :span="24">
          <el-button type="success" size="mini" icon="el-icon-plus" @click.native="add" v-permission="['/github']">{{ $t('button.add') }}</el-button>
          <el-button type="primary" size="mini"  icon="el-icon-edit" @click.native="edit" v-permission="['/github']">{{ $t('button.edit') }}</el-button>

<!--          <el-button type="success" size="mini" icon="el-icon-plus" @click.native="add" >新增</el-button>-->
<!--          <el-button type="primary" size="mini"  icon="el-icon-edit" @click.native="edit" >修改</el-button>-->
        </el-col>
      </el-row>
    </div>


    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">

      <el-table-column label="邮箱">
        <template slot-scope="scope">
          {{scope.row.emailAddress}}
        </template>
      </el-table-column>
      <el-table-column label="账户名称">
        <template slot-scope="scope">
          {{scope.row.githubName}}
        </template>
      </el-table-column>
      <el-table-column label="仓库名称">
        <template slot-scope="scope">
          {{scope.row.repositoryName}}
        </template>
      </el-table-column>
      <el-table-column label="仓库地址">
        <template slot-scope="scope">
          {{scope.row.sshUrl}}
        </template>
      </el-table-column>

      <el-table-column label="提交时间">
        <template slot-scope="scope">
          {{scope.row.timeSubmit}}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
<!--          <el-button type="text" size="mini" icon="el-icon-edit" @click.native="editItem(scope.row)" v-permission="['/github']">{{ $t('button.edit') }}</el-button>-->
          <el-button type="text" size="mini" icon="el-icon-edit" @click.native="editItem(scope.row) " v-permission="['/github']">修改</el-button>
<!--          <el-button type="text" size="mini" icon="el-icon-delete" @click.native="removeItem(scope.row)" v-permission="['/github']">{{ $t('button.delete') }}</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100,500]"
      :page-size="listQuery.limit"
      :total="total"
      :current-page.sync="listQuery.page"
      @size-change="changeSize"
      @current-change="fetchPage"
      @prev-click="fetchPrev"
      @next-click="fetchNext">
    </el-pagination>

    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="70%">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="邮箱"  >
              <el-input v-model="form.emailAddress" minlength=1></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户名称"  >
              <el-input v-model="form.githubName" minlength=1></el-input>
            </el-form-item>
          </el-col>
<!--          <el-col :span="12">-->
<!--            <el-form-item label="账户密码"  >-->
<!--              <el-input v-model="form.password" minlength=1></el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->

          <el-col :span="12">
            <el-form-item label="账户密码"  >
            <el-input
              :type="pwdType"
              v-model="form.password"
              name="password"
              auto-complete="on"
            />
<!--            <span class="show-pwd" @click="showPwd">-->
<!--              <svg-icon icon-class="eye" />-->
<!--            </span>-->
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="仓库名称"  >
              <el-input v-model="form.repositoryName" minlength=1></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仓库地址"  >
              <el-input v-model="form.sshUrl" minlength=1></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="提交时间"  >
              <el-input v-model="form.timeSubmit" minlength=1 :disabled = true></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="save">{{ $t('button.submit') }}</el-button>
          <el-button @click.native="formVisible = false">{{ $t('button.cancel') }}</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>
  </div>
</template>

<script src="./github.js"></script>


<style rel="stylesheet/scss" lang="scss" scoped>
  @import "../../styles/common";
</style>
