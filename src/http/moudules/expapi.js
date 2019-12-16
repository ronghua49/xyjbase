import expaxios from '../expaxios'

//获取mp平台树形菜单
export const getMyMenuTree = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/framework/getMyMenuTree.do',
    method: 'get',
    params: data
  })
}

//mp登录
export const login = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/login/validlogin.do',
    method: 'post',
    data: data
  })
}

//处理代办
export const stephandler = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/step/stephandler.do',
    method: 'get',
    params: data
  })
}
//我的待办
export const pending = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/mytask/pending.do',
    method: 'get',
    params:data
  })
}
//我发起的
export const launch = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/mytask/launch.do',
    method: 'get',
    params:data
  })
}

//我审阅的
export const already =(data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/mytask/already.do',
    method: 'get',
    params:data
  })
}
//我关注的
export const attention =(data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/mytask/attention.do',
    method: 'get',
    params:data
  })
}
//草稿
export const draft =(data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/mytask/draft.do',
    method: 'get',
    params:data
  })
}
//获取当前用户权限下的审批流树
export const getuserpermissiontree = (data) => {
  return expaxios({
    forType: true,
    url: '/mp/wfengine/mytask/getuserpermissiontree.do',
    method: 'get',
    params: data
  });
}
//执行工作流
export const handleWFAction = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/step/handleWFAction.do',
    method: 'post',
    data: data
  })
}
//获取流程图片
export const getprocessgraph = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/mytask/getprocessgraph.do',
    method: 'get',
    params: data
  })
}

//获取审批下一步信息
export const getNextStepInfo = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/step/getNextStepInfo.do',
    method: 'post',
    data: data
  })
}

//获取驳回下一步信息
export const getBackInfo = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/SelectorNew/getBackInfo.do',
    method: 'get',
    params: data
  })
}


//关注与取消关注
export const updateAttention = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/wfengine/mytask/updateattention.do',
    method: 'post',
    data: data
  })
}

//获取员工信息
export const getUserList = (data) => {
  return expaxios({
    formType: true,
    url: '/mp/org/user/list.do',
    method: 'get',
    params: data
  })
}

//为保持Mp平台token有效性调用接口
export const NavigationLogAdd = (data) => {
  return expaxios({
    formType: true,
    url: "/mp/log/NavigationLogAdd.do",
    method: 'post',
    data: data
  })
}

