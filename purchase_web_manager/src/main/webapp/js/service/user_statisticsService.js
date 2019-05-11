//服务层
app.service('user_statisticsService',function($http) {

    //读取列表数据绑定到表单中
    this.findAll = function (start, end, statusValue) {
        return $http.get('../user_statistics/findAll.do?start='+start+'&end='+end+'&statusValue='+statusValue);
    }

})