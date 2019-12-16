import Cookies from "js-cookie";
import jquery from 'jquery';
import config from './config';

export default function $jqueryAjax(options) {
  let token = Cookies.get('token');
  let then = this;
  let tmpHeaders = options.formType ? config.formHeaders : {"token": token, "Content-Type": "application/json;charset=UTF-8" };
  let defaultOptions = {
    headers: tmpHeaders,
    url: config.baseUrl,
    type: 'GET',
    async: false,    //或false,是否异步
    data: {},
    timeout: config.timeout,    //超时时间
    dataType: 'json',    //返回的数据格式：
    beforeSend: function (xhr) {
    },
    success: function (data, textStatus, jqXHR) {
      if(options.xyjSuccess){
        if(typeof options.xyjSuccess === "function" ){
          options.xyjSuccess(data);
        }
      }
    },
    error: function (xhr, textStatus) {
      switch (textStatus) {
        case 400:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"请求错误");
            }else{
              then.$message.error("请求错误");
            }
          }
          break
        case 401:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"未授权，请登录");
            }else{
              then.$message.error("未授权，请登录");
            }
          }
          break
        case 403:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"拒绝访问");
            }else{
              then.$message.error("拒绝访问");
            }
          }
          break
        case 404:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,`请求地址出错: ${err.response.config.url}`);
            }else{
              then.$message.error(`请求地址出错: ${err.response.config.url}`);
            }
          }
          break
        case 408:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"请求超时");
            }else{
              then.$message.error("请求超时");
            }
          }
          break
        case 500:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"服务器内部错误");
            }else{
              then.$message.error("服务器内部错误");
            }
          }
          break
        case 501:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"服务未实现");
            }else{
              then.$message.error("服务未实现");
            }
          }
          break
        case 502:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"网关错误");
            }else{
              then.$message.error("网关错误");
            }
          }
          break
        case 503:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"服务不可用");
            }else{
              then.$message.error("服务不可用");
            }
          }
          break
        case 504:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"网关超时");
            }else{
              then.$message.error("网关超时");
            }
          }
          break
        case 505:
          if(options.xyjError){
            if(typeof options.xyjError === 'function'){
              options.xyjError(textStatus,"HTTP版本不受支持");
            }else{
              then.$message.error("HTTP版本不受支持");
            }
          }
          break
        default:
      }
    },
    complete: function () {
    }
  }
  Object.assign(defaultOptions,options);
  jquery.ajax(defaultOptions);
}
