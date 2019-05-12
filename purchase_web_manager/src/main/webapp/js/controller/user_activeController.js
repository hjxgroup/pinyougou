 //控制层 
app.controller('user_activeController' ,function($scope,$controller,user_activeService){

    $scope.getNewTime = function () {
        var date = new Date();
        var fullYear = date.getFullYear();
        var month = date.getMonth() + 1;
        var ms = month < 10 ? "-" + "0" + month : "-" + month;
        var rs = fullYear + ms;
        return rs;
    }

	$scope.searchEntity={};//定义搜索对象

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
        $scope.userId = $scope.searchEntity.user_Id;
        user_activeService.findAll($scope.start,$scope.end,$scope.userId).success(
            function(response){
                $scope.data=response;
                $scope.myChart.setOption(
                        $scope.option = {
                        title: {
                            text: 'Beijing AQI'
                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        xAxis: {
                            data: $scope.data.map(function (item) {
                                var itemValue=JSON.parse(item);
                                return itemValue[0];
                            })
                        },
                        yAxis: {
                            splitLine: {
                                show: false
                            }
                        },
                        dataZoom: [{
                            startValue: '2014-06-01'
                        }, {
                            type: 'inside'
                        }],
                        visualMap: {
                            top: 10,
                            right: 10,
                            pieces: [{
                                gt: 0,
                                lte: 10,
                                color: '#096'
                            }, {
                                gt: 10,
                                lte: 20,
                                color: '#ffde33'
                            }, {
                                gt: 20,
                                lte: 30,
                                color: '#ff9933'
                            }, {
                                gt: 30,
                                lte: 80,
                                color: '#cc0033'
                            }, {
                                gt: 80,
                                lte: 100,
                                color: '#660099'
                            }, {
                                gt: 100,
                                color: '#7e0023'
                            }],
                            outOfRange: {
                                color: '#999'
                            }
                        },
                        series: {
                            name: 'Beijing AQI',
                            type: 'line',
                            data: $scope.data.map(function (item) {
                                var itemValue=JSON.parse(item);
                                return itemValue[1];
                            }),
                            markLine: {
                                silent: true,
                                data: [{
                                    yAxis: 50
                                }, {
                                    yAxis: 100
                                }, {
                                    yAxis: 150
                                }, {
                                    yAxis: 200
                                }, {
                                    yAxis: 300
                                }]
                            }
                        }
                    });
                });
            }

});	
