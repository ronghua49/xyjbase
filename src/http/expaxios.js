import axios from 'axios';
import config from './config';
import Cookies from "js-cookie";
import router from '@/router'
import qs from 'qs';

// 使用vuex做全局loading时使用
// import store from '@/store'

export default function $axios(options) {
  return new Promise((resolve, reject) => {
    let tmpbaseURL =  options.hostUrl ? options.hostUrl : config.baseUrl;
    let tmpTimeout = options.timeout ? options.timeout : config.timeout;
    let tmpHeaders = options.formType ?  config.formHeaders : config.headers;
    const instance = axios.create({
      baseURL: tmpbaseURL,
      headers: tmpHeaders,
      timeout: tmpTimeout,
      withCredentials: config.withCredentials
    })

    // request 拦截器
    instance.interceptors.request.use(
      config => {
        let token = Cookies.get('token');
        if (!token) {
          // 重定向到登录页面
          router.push('/mpLogin')
        }
        if (config.method === 'post'){
          config.data.token = token;
          config.data = qs.stringify(config.data);
        }else{
          config.params.token = token;
        }
        return config
      },
      error => {
        // 请求错误时
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
        return Promise.reject(error) // 在调用的那边可以拿到(catch)你想返回的错误信息
      }
    )

    // response 拦截器
    instance.interceptors.response.use(
      response => {
        let data;
        // IE9时response.data是undefined，因此需要使用response.request.responseText(Stringify后的字符串)
        if(response.headers.filename){
          data = {};
          data.data= response.data;
          data.filename = response.headers.filename;
        }else if (response.data == undefined) {
          data = JSON.parse(response.request.responseText)
        } else {
          data = response.data
        }

        // 根据返回的code值来做不同的处理
        switch (data.rc) {
          case 1:
            console.log(data.desc)
            break;
          case 0:
            store.commit('changeState')
            // console.log('登录成功')
          default:
        }
        return data
      },
      err => {
        let error = 'MP平台错误信息：';
        if (err && err.response) {
          switch (err.response.status) {
            case 400:
              err.message = error + '请求错误'
              break
            case 401:
              err.message = error + '未授权，请登录'
              break
            case 403:
              err.message = error + '拒绝访问'
              break
            case 404:
              err.message = error + `请求地址出错: ${err.response.config.url}`
              break
            case 408:
              err.message = error + '请求超时'
              break
            case 500:
              err.message = error + err.response.data.message;
              break
            case 501:
              err.message = error + '服务未实现'
              break
            case 502:
              err.message = error + '网关错误'
              break
            case 503:
              err.message = error + '服务不可用'
              break
            case 504:
              err.message = error + '网关超时'
              break
            case 505:
              err.message = error + 'HTTP版本不受支持'
              break
            default:
          }
        }else{
          err.message = error + err.message;
        }
        return Promise.reject(err) // 返回接口返回的错误信息
      }
    )

    // 请求处理
    instance(options).then(res => {
      resolve(res)
      return false
    }).catch(error => {
      reject(error)
    })
  })
}
