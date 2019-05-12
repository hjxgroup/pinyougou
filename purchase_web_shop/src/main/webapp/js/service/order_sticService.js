//服务层
app.service('orderSticService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.showData=function(year,goodsName){
		return $http.post('../order/showData.do?year='+year+"&goodsName="+goodsName);
	}
});
