//服务层
app.service('salesService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.sales_pre=function(data){
		return $http.post('../sales/salespre.do?startdata='+data.start+"&enddata="+data.end);
	}
});
