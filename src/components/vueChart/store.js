const key = 'VUE-CHAT-v3';

// 虚拟数据
//if (!localStorage.getItem(key)) {
    let now = new Date();
    let data = {
        // 登录用户
        user: {
            id: 1,
            name: 'admin',
            img: require('@/assets/images/1.jpg')
        },
        
        // 用户列表
        userList: [],

        // 会话列表
        sessionList: [],
    };
    
//  localStorage.setItem(key, JSON.stringify(data));
//}

export default {
    fetch () {
    	return data
//      return JSON.parse(localStorage.getItem(key));
    },
    save (store) {
        localStorage.setItem(key, JSON.stringify(store));
    }
};