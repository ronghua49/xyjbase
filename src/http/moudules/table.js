import axios from '../axios'
/*
 * 表格初始化请求
 */
// 查询表格初始化数据
export const tablefind = data => {
  return axios({
    url: 'tableFind',
    method: 'post',
    data
  })
}
