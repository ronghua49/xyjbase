import global from '@/utils/global.js'
import axios from 'axios'
import Cookies from 'js-cookie'
import router from '../router'

export const common = axios.create({
  baseURL: global.baseUrl,
});
export const yhService = axios.create({
  baseURL: global.yhUrl,
})
/*基础模块*/
export const zwlService = axios.create({
  baseURL: global.baseUrl,
})
// 支付设置端口
export const zfSetService = axios.create({
  baseURL: global.zfUrl,
})
/* 活动品牌*/
export const activeService = axios.create({
  baseURL: global.activeUrl,
})
/* 查询*/
export const queryService = axios.create({
  baseURL: global.queryUrl,
})
/* 查询2*/
export const cloneService = axios.create({
  baseURL: global.cloneUrl,
})
export default {
  common,
  yhService,
  zwlService,
  zfSetService,
  activeService,
  queryService,
  cloneService,
}
/* *************************** 基础模块   ************************* */
/*请求拦截*/
zwlService.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  let token = Cookies.get('token')
//   1. 请求开始的时候可以结合 vuex 开启全屏 loading 动画
//   2. 带上token
  if (token) {
    config.headers.token = token
  } else {
    // 重定向到登录页面
    router.push('/login')
  }
  return config;
}, function (error) {
  // 对请求错误做些什么
  console.log('request:', error)
  // 1. 判断请求超时
  if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
    console.log('timeout请求超时')
    // return service.request(originalRequest);// 再重复请求一次
  }
  // 2. 需要重定向到错误页面
  const errorInfo = error.response
  console.log(errorInfo)
  if (errorInfo) {
    error = errorInfo.data  // 页面那边catch的时候就能拿到详细的错误信息,看最下边的Promise.reject
    const errorStatus = errorInfo.status; // 404 403 500 ...
    router.push({
      path: `/error/${errorStatus}`
    })
  }
  return Promise.reject(error)
});
/*响应拦截*/
zwlService.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  if (response.data.code != 200) {
    this.$toast({message: this.data.msg, position: "bottom"});
    return
  }
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});
/**************************** 业务模块   **************************/
/*请求拦截*/
yhService.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  let token = Cookies.get('token')
//   1. 请求开始的时候可以结合 vuex 开启全屏 loading 动画
  console.log('准备发送请求...')
//   2. 带上token
  if (token) {
    config.headers.token = token
  } else {
    // 重定向到登录页面
    router.push('/login')
  }
  return config;
}, function (error) {
  // 对请求错误做些什么
  console.log('request:', error)
  // 1. 判断请求超时
  if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
    console.log('timeout请求超时')
    // return service.request(originalRequest);// 再重复请求一次
  }
  // 2. 需要重定向到错误页面
  const errorInfo = error.response
  console.log(errorInfo)
  if (errorInfo) {
    error = errorInfo.data  // 页面那边catch的时候就能拿到详细的错误信息,看最下边的Promise.reject
    const errorStatus = errorInfo.status; // 404 403 500 ...
    router.push({
      path: `/error/${errorStatus}`
    })
  }
  return Promise.reject(error)
});
/*响应拦截*/
yhService.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});
/**************************** 公共支付   **************************/
/*请求拦截*/
common.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  let token = Cookies.get('token')
//   1. 请求开始的时候可以结合 vuex 开启全屏 loading 动画
  console.log('准备发送请求...')
//   2. 带上token
  if (token) {
    config.headers.token = token
  } else {
    // 重定向到登录页面
    router.push('/login')
  }
  return config;
}, function (error) {
  // 对请求错误做些什么
  console.log('request:', error)
  // 1. 判断请求超时
  if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
    console.log('timeout请求超时')
    // return service.request(originalRequest);// 再重复请求一次
  }
  // 2. 需要重定向到错误页面
  const errorInfo = error.response
  console.log(errorInfo)
  if (errorInfo) {
    error = errorInfo.data  // 页面那边catch的时候就能拿到详细的错误信息,看最下边的Promise.reject
    const errorStatus = errorInfo.status; // 404 403 500 ...
    router.push({
      path: `/error/${errorStatus}`
    })
  }
  return Promise.reject(error)
});
/*响应拦截*/
common.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});
/**************************** 支付设置   **************************/
/*请求拦截*/
zfSetService.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  let token = Cookies.get('token')
//   1. 请求开始的时候可以结合 vuex 开启全屏 loading 动画
  console.log('准备发送请求...')
//   2. 带上token
  if (token) {
    config.headers.token = token
  } else {
    // 重定向到登录页面
    router.push('/login')
  }
  return config;
}, function (error) {
  // 对请求错误做些什么
  console.log('request:', error)
  // 1. 判断请求超时
  if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
    console.log('timeout请求超时')
    // return service.request(originalRequest);// 再重复请求一次
  }
  // 2. 需要重定向到错误页面
  const errorInfo = error.response
  console.log(errorInfo)
  if (errorInfo) {
    error = errorInfo.data  // 页面那边catch的时候就能拿到详细的错误信息,看最下边的Promise.reject
    const errorStatus = errorInfo.status; // 404 403 500 ...
    router.push({
      path: `/error/${errorStatus}`
    })
  }
  return Promise.reject(error)
});
/*响应拦截*/
zfSetService.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});
/**************************** 活动品牌   **************************/
/*请求拦截*/
activeService.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  let token = Cookies.get('token')
//   1. 请求开始的时候可以结合 vuex 开启全屏 loading 动画
  console.log('准备发送请求...')
//   2. 带上token
  if (token) {
    config.headers.token = token
  } else {
    // 重定向到登录页面
    router.push('/login')
  }
  return config;
}, function (error) {
  // 对请求错误做些什么
  console.log('request:', error)
  // 1. 判断请求超时
  if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
    console.log('timeout请求超时')
    // return service.request(originalRequest);// 再重复请求一次
  }
  // 2. 需要重定向到错误页面
  const errorInfo = error.response
  console.log(errorInfo)
  if (errorInfo) {
    error = errorInfo.data  // 页面那边catch的时候就能拿到详细的错误信息,看最下边的Promise.reject
    const errorStatus = errorInfo.status; // 404 403 500 ...
    router.push({
      path: `/error/${errorStatus}`
    })
  }
  return Promise.reject(error)
});
/*响应拦截*/
activeService.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});
/**************************** 查询   **************************/
/*请求拦截*/
queryService.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  let token = Cookies.get('token')
//   1. 请求开始的时候可以结合 vuex 开启全屏 loading 动画
  console.log('准备发送请求...')
//   2. 带上token
  if (token) {
    config.headers.token = token
  } else {
    // 重定向到登录页面
    router.push('/login')
  }
  return config;
}, function (error) {
  // 对请求错误做些什么
  console.log('request:', error)
  // 1. 判断请求超时
  if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
    console.log('timeout请求超时')
    // return service.request(originalRequest);// 再重复请求一次
  }
  // 2. 需要重定向到错误页面
  const errorInfo = error.response
  console.log(errorInfo)
  if (errorInfo) {
    error = errorInfo.data  // 页面那边catch的时候就能拿到详细的错误信息,看最下边的Promise.reject
    const errorStatus = errorInfo.status; // 404 403 500 ...
    router.push({
      path: `/error/${errorStatus}`
    })
  }
  return Promise.reject(error)
});
/*响应拦截*/
queryService.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});
/**************************** 查询 2  **************************/
/*请求拦截*/
cloneService.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  let token = Cookies.get('token')
//   1. 请求开始的时候可以结合 vuex 开启全屏 loading 动画
  console.log('准备发送请求...')
//   2. 带上token
  if (token) {
    config.headers.token = token
  } else {
    // 重定向到登录页面
    router.push('/login')
  }
  return config;
}, function (error) {
  // 对请求错误做些什么
  console.log('request:', error)
  // 1. 判断请求超时
  if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
    console.log('timeout请求超时')
    // return service.request(originalRequest);// 再重复请求一次
  }
  // 2. 需要重定向到错误页面
  const errorInfo = error.response
  console.log(errorInfo)
  if (errorInfo) {
    error = errorInfo.data  // 页面那边catch的时候就能拿到详细的错误信息,看最下边的Promise.reject
    const errorStatus = errorInfo.status; // 404 403 500 ...
    router.push({
      path: `/error/${errorStatus}`
    })
  }
  return Promise.reject(error)
});
/*响应拦截*/
cloneService.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});
