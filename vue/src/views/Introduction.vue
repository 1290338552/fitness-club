<template>
  <div>
    <div class="card"  >
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button clearable type="warning" @click="reset">重 置</el-button>
    </div>
    <div class="card" v-if="data.user.role === 'USER'">
      <el-button @click="handleAdd" type="primary" >新增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">
        <el-table-column label="健身标准图">
          <template #default="scope">
            <el-image style="width: 50px;height: 50px;border-radius: 5px;display: block" v-if="scope.row.img" :src="scope.row.img" :preview-src-list=[scope.row.img] :preview-teleported="true" ></el-image>
          </template>
        </el-table-column>

        <el-table-column prop="title" label="健身攻略标题" width="180" />
        <el-table-column prop="categoryTitle" label="健身分类" />
        <el-table-column prop="userName" label="发布用户" />
        <el-table-column prop="content" label="攻略内容" >
          <template v-slot="scope">
              <el-button type="primary" @click="viewContent(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" />

        <el-table-column label="操作" >
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-size="data.pageSize"
          layout="total,size, prev, pager, next,jumper"
          :page-sizes="[5, 10, 20]"
          :total="data.total"
          @current-change="load"
          @size-change="load"

      />
    </div>

    <el-dialog title="健身信息" v-model="data.formVisible" width="60%" destroy-on-close>
      <el-form  ref="formRef" :model="data.form"  label-width="80px" style="padding: 20px 30px 20px 0">

        <el-form-item prop="img" label="健身标准" >
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{token:data.user.token}"
              :on-success="handleUploadSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>

        <el-form-item prop="title" label="健身标题" >
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入健身攻略标题"/>
        </el-form-item>

        <el-form-item prop="category" label="健身分类" >
          <el-select
              v-model="data.form.categoryId"
              placeholder="请选择健身蕾丝"
              style="width: 100%"
          >
            <el-option
                v-for="item in data.categoryData"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="攻略内容" >
          <div style="border: 1px solid #ccc;width: 100%">
            <Toolbar
                style="border: 1px solid #ccc;"
              :editor="editorRef"
              :mode="mode"
            />
            <Editor
                style="height: 200px;overflow-y: hidden"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
                 />
          </div>
        </el-form-item>
        <!--        <el-form-item prop="time" label="发布时间" >-->
        <!--          <el-input v-model="data.form.time" autocomplete="off" placeholder="请输入发布时间"/>-->
        <!--        </el-form-item>-->

      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="健身信息" v-model="data.viewVisible" width="60%" destroy-on-close>
      <div v-html="data.content"></div>
    </el-dialog>

  </div>
</template>

<script setup>

import {Search} from "@element-plus/icons-vue";
import {onBeforeUnmount,reactive,ref,shallowRef} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Editor,Toolbar} from "@wangeditor/editor-for-vue";
import '@wangeditor/editor/dist/css/style.css'//引入富文本css


const data  = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || "{}"),
  title:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  form:{},
  formVisible:false,
  content:null,
  viewVisible:false,
  categoryData:[],
})

//wangEditor5初始化开始
const editorRef = shallowRef(null)//编辑器实例，必须用shallowRef
const mode = 'default'
const editorConfig = {MENU_CONF:{}}
//图片上传配置
editorConfig.MENU_CONF['uploadImg'] = {
  headers: {
    token: data.user.token
  },
  server: 'http://localhost:9999/files/wang/upload',//服务端图片上传接口
  fieldName: 'file', //服务端图片上传接口参数
}
//组件销毁时，即及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor ==  null) return
  editor.destroy()
})
//记录editor实例
const handleCreated = (editor) => {
  editorRef.value = editor
}
//wangEditor5初始化结束


const load = () => {
  request.get("/introduction/selectPage",{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      title:data.title
    }
  }).then(res => {
    if (res.code === "200"){
      data.tableData = res.data?.list
      data.total = res.data?.total
    }else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const viewContent = (content) => {
  data.content = content
  data.viewVisible = true
}

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))//深拷贝给表单
  data.formVisible = true//回显
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复','请确认删除',{type: 'warning'}).then(res => {
    request.delete("/introduction/delete/"+id).then(res => {
      if (res.code === '200'){
        ElMessage.success("删除成功")
        load()
      }else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const handleUploadSuccess = (res) => {
  if (res.code === '200'){
    ElMessage.success("上传成功")
    data.form.img = res.data
  }else {
    ElMessage.error(res.msg)
  }
}

const reset = () => {
  data.title = null
  load()
}

const add = () =>{
  request.post("/introduction/add",data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("新增成功")
      data.formVisible = false
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () =>{
  request.put("/introduction/update",data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("修改成功")
      data.formVisible = false
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}
const save = () => {

      data.form.id ? update() : add() //通过id判断是修改还是新增，如果没有就新增有就修改

}

const categoryData = () => {
  request.get("/category/selectAll").then(res => {
    if (res.code === '200'){
      data.categoryData = res.data
    }else {
      ElMessage.error(res.msg)
    }
  })
}
categoryData()
</script>
