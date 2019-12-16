import axios from '../../axios'
/**
 *宋建国：2019/8/7 14:11
 *描述：创建流程模型后，用户可以根据模型modelId操作流程模型（编辑、删除）,创建后返回创建的流程模型信息"
 */
export const save = (data,then) => {
  return axios({
    hostUrl: then.leave,
    url: '/xyjLeave/save',
    method: 'post',
    data:data
  })
}

/**
 *宋建国：2019/8/9 17:31
 *描述：创建空流程模型
 */
/*export const deleteModel = (data) => {
  return axios({
    hostUrl: config.workflow,
    url: '/model/deleteModel',
    method: 'get',
    params:data
  })
}*/
