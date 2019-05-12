//服务层
app.service('user_activeService',function($http) {

    //读取列表数据绑定到表单中
    this.findAll = function (start, end, userId) {
        return $http.get('../user_statistics/findAllByOrder.do?start='+start+'&end='+end+'&userId='+userId);
    }

})