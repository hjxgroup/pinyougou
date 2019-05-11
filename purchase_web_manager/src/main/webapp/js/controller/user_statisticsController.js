 //控制层 
app.controller('user_statisticsController' ,function($scope,$controller,user_statisticsService){

    $scope.getNewTime = function () {
        var date = new Date();
        var fullYear = date.getFullYear();
        var month = date.getMonth() + 1;
        var ms = month < 10 ? "-" + "0" + month : "-" + month;
        var rs = fullYear + ms;
        return rs;
    }

	$scope.searchEntity={status:3};//定义搜索对象
	//搜索
	// 显示状态
	$scope.status = ["冻结","新增","关闭","全部"];

	//	图表js
    // 基于准备好的dom，初始化echarts实例
    $scope.myChart = echarts.init(document.getElementById('main'));


    // 指定图表的配置项和数据
    //读取列表数据绑定到表单中
    $scope.findAll=function(flag){
        if(flag==0) {
            var newData = $scope.getNewTime() + "-01";
            var month = parseInt(newData.substr(5, 2));
            var year = parseInt(newData.substr(0, 4));
            year = year - 1;
            $scope.start = year + "-" + month + "-01";
            $scope.end = newData;

        }else {
            $scope.start=$scope.searchEntity.start+"-01";
            $scope.end=$scope.searchEntity.end+"-01";
        }
        $scope.statusValue = $scope.searchEntity.status;
        user_statisticsService.findAll($scope.start,$scope.end,$scope.statusValue).success(
            function(response){
                $scope.data=response;
                //   数组遍历            集合名,map(function (元素名))
                $scope.dateList = $scope.data.map(function (item) {
                    //json字符串转为json对象  格式[]
                    var itemValue=JSON.parse(item)
                    return itemValue[0];
                });
                $scope.valueList = $scope.data.map(function (item) {
                    var itemValue=JSON.parse(item)
                    return itemValue[1]*10;
                });

                // $scope.data=[["2018-01",73],["2018-02",68],["2018-03",92],["2018-04",130],["2018-05",245],["2018-06",139],["2018-07",115],["2018-08",111],["2018-09",309],["2018-10",206],["2018-010",130],["2018-11",245],["2018-12",139],["2019-01",115],["2019-02",111],["2019-03",309],["2019-04",206]];
                // $scope.dateList = $scope.data.map(function (item) {
                //     return item[0];
                // });
                // $scope.valueList = $scope.data.map(function (item) {
                //     return item[1];
                // });


                $scope.option = {

                    // Make gradient line here
                    visualMap: [{
                        show: false,
                        type: 'continuous',
                        seriesIndex: 0,
                        min: 0,
                        max: 400
                    }, {
                        show: false,
                        type: 'continuous',
                        seriesIndex: 1,
                        dimension: 0,
                        min: 0,
                        max: $scope.dateList.length - 1
                    }],


                    title: [{
                        left: 'center',
                        text: '每月'+$scope.status[$scope.searchEntity.status]+'账户统计'
                    }],
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: [{
                        data: $scope.dateList
                    }],
                    yAxis: [{
                        splitLine: {show: false}
                    }],
                    grid: [{
                        bottom: '60%'
                    }, {
                        top: '60%'
                    }],
                    series: [{
                        type: 'line',
                        showSymbol: false,
                        data: $scope.valueList
                    }]
                };
                $scope.myChart.setOption($scope.option);
            }
        );
    }



    // 使用刚指定的配置项和数据显示图表。
    //$scope.myChart.setOption($scope.option);

});	
