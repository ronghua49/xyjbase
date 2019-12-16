import axios from '../axios'
import config from "@/utils/global.js"

/**
 *宋建国：2019/8/7 14:11
 *描述：创建流程模型后，用户可以根据模型modelId操作流程模型（编辑、删除）,创建后返回创建的流程模型信息"
 */
export const createModel = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/model/createModel?code='+data.code,
    method: 'post',
  })
}

/**
 *宋建国：2019/8/7 14:12
 *描述：获取全部流程定义列表
 */
export const AllModelList = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/model/AllModelList',
    method: 'post',
    data
  })
}
/**
 *宋建国：2019/8/9 17:31
 *描述：创建空流程模型
 */
export const deleteModel = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/model/deleteModel',
    method: 'get',
    params:data
  })
}
/**
 *宋建国：2019/8/10 12:37
 *描述：流程模型部署
 */
export const deployModel = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/model/deployModel',
    method: 'get',
    params: data
  })
}

/**
 *宋建国：2019/8/10 12:37
 *描述：导出流程模型XML文件
 */
export const exportModelXml = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/model/exportXml',
    method: 'get',
    params: data
  })
}

/**
 *宋建国：2019/8/13 14:53
 *描述：导入流程模型XML文件
 *版本：1.0
 */
export const importXml = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/model/importXml',
    method: 'post',
    headers:{
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}

/**
 *宋建国：2019/8/15 13:13
 *描述：获取流程定义列表
 *版本：1.0
 */
export const getAllProcessDefinitionList = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/process/getAllProcessDefinitionList',
    method: 'get',
    params: data
  })
}

/**
 *宋建国：2019/8/16 0:50
 *描述：删除流程定义
 *版本：1.0
 */
export const delProcessDefinition = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/process/delProcessDefinition',
    method: 'get',
    params: data
  })
}

/**
 *宋建国：2019/8/16 23:56
 *描述：通过流程定义生成模型流程
 *版本：1.0
 */
export const processToModel = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/process/processToModel',
    method: 'post',
    data
  })
}
/**
 *宋建国：2019/8/16 23:56
 *描述：根据流程实例ID获取流程图，高亮当前任务节点
 *版本：1.0
 */
export const seeWorkflow = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/flowDiagram/getDiaImageByInstance/'+data,
    method: 'get',
    responseType:'arraybuffer'
  })
}
/**
 *宋建国：2019/9/26 16:22
 *描述：根据流程实例ID获取流程图，高亮当前任务节点及历史节点
 *版本：1.0
 */
export const getImageByInstance = (data) =>{
  return axios({
    hostUrl: config.workflow,
    url: '/flowDiagram/getImageByInstance/'+data,
    method: 'get',
    responseType:'arraybuffer'
  })
}
/**
 *宋建国：2019/8/16 23:56
 *描述：根据业务ID获取流程图，高亮当前任务节点
 *版本：1.0
 */
export const getDiaImageByBusinessKey = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/flowDiagram/getDiaImageByBusinessKey/'+data,
    method: 'get',
    responseType:'arraybuffer'
  })
}
/**
 *宋建国：2019/8/16 23:56
 *描述：根据任务ID获取流程图，只高亮当前任务节点
 *版本：1.0
 */
export const seeWorkflowTask = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/flowDiagram/getDiagram/'+data,
    method: 'get',
    responseType:'arraybuffer'
  })
}
/**
 *宋建国：2019/8/16 23:56
 *描述：删除流程实例
 *版本：1.0
 */
export const deleteProessdefInstance = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/processInstance/deleteProessdefInstance/'+data,
    method: 'get',
  })
}
/**
 *宋建国：2019/8/16 23:56
 *描述：流程实例挂起/激活
 *版本：1.0
 */
export const suspendProcessInstanceById = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/processInstance/suspendProcessInstanceById/'+data,
    method: 'get',
  })
}
/**
 *宋建国：2019/9/25 15:11
 *描述：启动审批流
 *版本：1.0
 */
export const startWorkFlow = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/process/startProcess',
    method: 'post',
    data
  })
}
/**
 *宋建国：2019/9/27 14:52
 *描述：查看流程实例ID查询审批记录
 *版本：1.0
 */
export const processHistory = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/history/processHistory',
    method: 'get',
    params: data
  })
}

/**
 *宋建国：2019/10/11 12:01
 *描述：获取节点审批按钮权限
 * taskId: 任务Id
 *版本：1.0
 */
export const getTaskIdButtonRole = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/task/getTaskIdButtonRole',
    method: 'get',
    params: data
  })
}
  /**
   *宋建国：2019/10/17 13:45
   *描述：审批接口
   * actionName: 处理类型 agree：根据流程图流转
   *                     selectAgree：允许选择下一步候选人审批
   *                     reject：驳回上一节点
   *                     rejectToStart： 驳回至发起人
   *                     rejectToAssign：驳回至指定节点
   *版本：1.0
   */
export const disposeTask = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/task/disposeTask',
    method: 'post',
    data: data
  })
}
/**
 *宋建国：2019/10/19 13:09
 *描述：获取下一步多个任务节点候选人
 *版本：1.0
 */
export const getApprovalInfo = (data) =>{
  return axios({
    hostUrl: config.workflow,
    url: '/process/getApprovalInfo',
    method: 'get',
    params: data
  })
}

/**
 *宋建国：2019/10/11 12:01
 *描述：获取节点审批按钮权限
 * 如果流程中存在并行网关、包容网关、排他网关，则接口返回数据会存在，可以根据此数据判断流程中是否有并行、包容、排他网关
 * 数据返回格式：List<Map<String,String>>
 *     Map：id：节点定义ID
 *          name: 节点定义名称
 *          nodetype: 节点类型 （ParallelGateway（并行网关）、
 *                              ExclusiveGateway（包容网关）、
 *                              UserTask{用户任务节点}、
 *                              ExclusiveGateway（排他网关））
 * 传入参数
 *  * taskId: 任务Id
 *  * varMap: 流程变量，用来判断节点间的连线是否有效
 *版本：1.0
 */
export const getUpperNode = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/process/getUpperNode',
    method: 'get',
    params: data
  })
}

/**
 *宋建国：2019/10/11 12:01
 *描述：获取节点审批按钮权限
 * 如果流程中存在并行网关、包容网关、排他网关，则接口返回数据会存在，可以根据此数据判断流程中是否有并行、包容、排他网关
 * 数据返回格式：List<Map<String,String>>
 *     Map：id：节点定义ID
 *          name: 节点定义名称
 *          nodetype: 节点类型 （ParallelGateway（并行网关）、
 *                              ExclusiveGateway（包容网关）、
 *                              UserTask{用户任务节点}、
 *                              ExclusiveGateway（排他网关））
 * 传入参数
 *  * taskId: 任务Id
 *  * varMap: 流程变量，用来判断节点间的连线是否有效
 *版本：1.0
 */
export const getUpperAllNode = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/process/getUpperAllNode',
    method: 'get',
    params: data
  })
}

/**
 *宋建国：2019/10/29 12:25
 *描述：根据流程任务ID获取发起节点
 * 如果流程中存在并行网关与包容网关，则接口返回数据会存在，可以根据此数据判断流程中是否有并行、包容网关
 * 数据返回格式：List<Map<String,String>>
 *     Map：id：节点定义ID
 *          name: 节点定义名称
 *          nodetype: 节点类型 （ParallelGateway（并行网关）、
 *                              ExclusiveGateway（包容网关）、
 *                              UserTask{用户任务节点}）
 *传入参数
 * taskId: 任务Id
 *版本：1.0
 */
export const getFirstNode = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/process/getFirstNode',
    method: 'get',
    params: data
  })
}
