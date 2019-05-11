// 定义服务层:
app.service("salesService",function($http){
	this.salesChart = function(start,end){
		return $http.get("../sales/salesChart.do?start="+start+"&end="+end);
	}
});