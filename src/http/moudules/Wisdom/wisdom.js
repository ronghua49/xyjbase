import axios from "../../axios";
import config from "@/utils/global.js"
/**
 *宋建国：2019/9/25 16:03
 *描述：aaaaasdfsadf
 *版本：1.0
 */
export const startWorkFlow = (data) => {
  let then = this;
  return axios({
    hostUrl: then.Parking,
    url: '/process/startProcess',
    method: 'post',
    data
  })
}
export const partPost = (data) => {
  let then = this;
  return axios({
    hostUrl: config.partplatz,
    url: data.url,
    method: 'post',
    data: data.data
  })
}

export const post = (data) => {
  let then = this;
  return axios({
    hostUrl: config.doorUrl,
    url: data.url,
    method: 'post',
    data: data.data
  })
}
export const alarmPost = (data) => {
  let then = this;
  return axios({
    hostUrl: config.alarmUrl,
    url: data.url,
    method: 'post',
    data: data.data
  })
}
export const alarmGet = (data) => {
  let then = this;
  return axios({
    hostUrl: config.alarmUrl,
    url: data.url,
    method: 'get',
    data: data.data
  })
}
