/* 
 * 接口统一集成模块
 */
import * as login from './moudules/login'
import * as user from './moudules/user'
import * as dept from './moudules/dept'
import * as role from './moudules/role'
import * as menu from './moudules/menu'
import * as dict from './moudules/dict'
import * as log from './moudules/log'
import * as common from './moudules/common'
import * as table from './moudules/table'
import * as expapi from './moudules/expapi'
import * as workflow from './moudules/workflow'
import * as wisdom from './moudules/Wisdom/wisdom'  //一体化接口
import * as leave from './moudules/leave/leave'  //审批流程测试项目
import * as im from './moudules/im'
// 默认全部导出
export default {
    login,
    user,
    dept,
    role,
    menu,
    dict,
    log,
    common,
    table,
    expapi,
    workflow,
    wisdom,
    leave,
    im
}
