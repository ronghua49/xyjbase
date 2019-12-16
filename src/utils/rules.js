export function QQV (rule,value,callback){
  if(!value){
    return callback(new Error("请输入QQ号"));
  }else if(/^[1-9][0-9]{4,10}$/.test(value)) {
    callback();
  }else{
    return callback(new Error('请输入正确的QQ号'));
  }
}

export function notQQV (rule,value,callback){
  if(!value){
    callback();
    return;
  }else if(/^[1-9][0-9]{4,10}$/.test(value)) {
    callback();
  }else{
    return callback(new Error('请输入正确的QQ号'));
  }
}
// 类似金钱,首位可以为0,最多2位小数
export function checkMoney (rule,value,callback) {
  // const reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
  const reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
  if(!reg.test(value)){
    return callback(new Error('请填写数字,最多2位小数'));
  } else {
    callback()
  }
}

// 验证数据合法性但非必填向，类似金钱,首位不为0,最多2位小数允许不输入
export function notCheckMoney (rule,value,callback) {
  // const reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
  const reg = /^([\+ \-]?(([1-9]\d*)|(0)))([.]\d{0,2})?$/;
  if(!value){
    callback();
    return;
  }else if(!reg.test(value)){
    return callback(new Error('请填写数字,最多2位小数'));
  } else {
    callback()
  }
}

//身份证验证
export function checkIdentityId (rule,value,callback) {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
  if(!value){
    return callback(new Error('证件号码不能为空'));
  } else if(reg.test(value)){
    return callback(new Error('证件号码不正确'));
  } else {
    callback();
  }
}

//身份证验证,非必填
export function notCheckIdentityId (rule,value,callback) {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
  if(!value){
    callback();
    return;
  } else if(reg.test(value)){
    return callback(new Error('证件号码不正确'));
  } else {
    callback();
  }
}


// 整数
export function checkInterNum (rule,value,callback) {
//  const reg = /^[0-9]*[1-9][0-9]*$/;
  const  reg = /^(0|[1-9][0-9]*|-[1-9][0-9]*)$/;
  if(!value){
    return callback(new Error('请填写整数'));
  }else if(!reg.test(value)){
    return callback(new Error('请输入整数'));
  }else{
    callback();
  }
}

// 整数,非必填
export function notCheckInterNum (rule,value,callback) {
  const  reg = /^(0|[1-9][0-9]*|-[1-9][0-9]*)$/;
  if(!value){
    callback();
    return;
  }else if(!reg.test(value)){
    return callback(new Error('请输入整数'));
  }else{
    callback();
  }
}

// 数字
export function checkNumber (rule,value,callback) {
  const  reg = /^-?\d+(\.\d+)?$/;
  if(!value && value !== 0){
    return callback(new Error('请填写数字'));
  }else if(!reg.test(value)){
    return callback(new Error('请输入数字'));
  }else{
    callback();
  }
}

// 数字,非必填
export function notCheckNumber (rule,value,callback) {
  const  reg = /^-?\d+(\.\d+)?$/;
  if(!value){
    callback();
    return;
  }else if(!reg.test(value)){
    return callback(new Error('请输入数字'));
  }else{
    callback();
  }
}


export default {
  QQ:[{required: true,validator:QQV,trigger:'change'}],   //QQ号
  notQQ:[{required: false,validator:notQQV,trigger:'change'}],   //QQ号,非必填

  phone:[{required: true, pattern: /^1[345789]\d{9}$/, message:'目前只支持中国大陆的手机号码',trigger: 'change'}], //手机号
  notPhone:[{required: false, pattern: /^1[345789]\d{9}$/, message:'目前只支持中国大陆的手机号码',trigger: 'change'}], //手机号,非必填

  checkMoney:[{required: true,validator:checkMoney,trigger:'change'}], //金额两位小数
  notCheckMoney:[{required: false,validator:notCheckMoney,trigger:'change'}], //金额两位小数,非必填

  checkId:[{required: true,validator:checkIdentityId,trigger:'change'}], //身份证
  notCheckId:[{required: false,validator:notCheckIdentityId,trigger:'change'}], //身份证,非必填

  checkInt:[{required: true,validator:checkInterNum,trigger:'change'}] , //整数
  notCheckInt:[{required: false,validator:notCheckInterNum,trigger:'change'}] , //整数,非必填

  checkNum:[{required: true,validator:checkNumber,trigger:'change'}] , //数字
  notcheckNum:[{required: false,validator:notCheckNumber,trigger:'change'}] , //数字,非必填
  //长度区间检测
  checkLength: function (tmin,tmax) {
    return [{ required: true,min:tmin, max:tmax, message: '字符长度在'+tmin+'至'+tmax+'之间!', trigger: 'change' }]
  },
  //长度检测
  maxlength: function (max) {
    return [{ required: true,min:1, max:max, message: '最多输入'+max+'个字符!', trigger: 'change' }]
  },
  //输入必填项
  checkMustFill:[{ required: true, message: '该输入项为必填项!', trigger: 'change',whitespace: true }],
  //选择必填项
  checkSelectValue:[{ required: true, message: '该项为必选项!', trigger: 'change',whitespace: true }],
  //数组选择必填项
  checkSelectArray:[{ required: true, type: "array", message: '该项为必选项!', trigger: 'change',whitespace: true }],
}
