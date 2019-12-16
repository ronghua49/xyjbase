/**
 * 时间日期相关操作
 */


/**
 * 时间格式化
 * 将 2018-09-23T11:54:16.000+0000 格式化成 2018/09/23 11:54:16
 * @param datetime 国际化日期格式
 */
export function format (datetime) {
  return formatWithSeperator(datetime, "/", ":");
}

/**
 * 时间格式化
 * 将 2018-09-23T11:54:16.000+0000 格式化成类似 2018/09/23 11:54:16
 * 可以指定日期和时间分隔符
 * @param datetime 国际化日期格式
 */
export function formatWithSeperator (datetime, dateSeprator, timeSeprator) {
  if (datetime != null) {
    const dateMat = new Date(datetime);
    const year = dateMat.getFullYear();
    let month = dateMat.getMonth() + 1;
    month = month.toString().length == 1 ? "0" + month : month;
    let day = dateMat.getDate();
    day = day.toString().length == 1 ? "0" + day : day;
    let hh = dateMat.getHours();
    hh = hh.toString().length == 1 ? "0" + hh : hh;
    let mm = dateMat.getMinutes();
    mm = mm.toString().length == 1 ? "0" + mm : mm;
    let ss = dateMat.getSeconds();
    ss = ss.toString().length == 1 ? "0" + ss : ss;
    const timeFormat = year + dateSeprator + month + dateSeprator + day + " " + hh + timeSeprator + mm + timeSeprator + ss;
    return timeFormat;
  }
}
