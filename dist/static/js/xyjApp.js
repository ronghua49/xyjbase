function xyjfn(){
	
}
xyjfn.prototype = {
	ajax: function ajax(url,methods,params, callback, async) {
			var that = this;
			var ajaxOptions = {
				type: methods,
				url: url,
				data: params,
				cache: false,
				async: async ? false : true,
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				dataType: 'json',
				success: function success(data) {
					if(callback && typeof callback == 'function') {
						callback(data);
					}
				},
				error: function error(XMLHttpRequest, textStatus, errorThrown) {
					if(textStatus == "timeout") {
						console.error("网络异常超时，请重试", 1);
						window.AppInterace.ToastDialog('', '网络异常超时，请重试', 0, 2000);
						return;
					}
					if(XMLHttpRequest.status == 404) {
						window.AppInterace.ToastDialog('', '找不到页面了', 0, 2000);
						return;
					}
					if(XMLHttpRequest.status == 500) {
						window.AppInterace.ToastDialog('', '服务器出错了', 0, 2000);
						return;
					}
					console.error('错误代码:' + XMLHttpRequest.status + " 错误信息:" + errorThrown, 1);
				}
			};
			Zepto.ajax(ajaxOptions);
		},
		time: null,
		toast: function(text,duration){
			var template = $("<span class='dialog' style='font-size: 14px;word-break: break-all;color: white;border-radius: 5px;padding: 4px 8px;position: fixed;bottom: 40px;margin: 0 auto;background: rgba(0,0,0,0.8);transition: all .8s;'>"+ text +"</span>");
			if ( $(".dialog").length == 0 ) {
				$('body').append(template);
				var width = $(".dialog").width();
				$(".dialog").css('left','calc(50% - '+ width/2 +'px)');
				time = setTimeout(function(){
					$(".dialog").css('opacity',0);
					setTimeout(function(){
						$(".dialog").remove();
					},800);
				},duration?duration:1000);
			}
		}
}
window.xyjApp = new xyjfn();