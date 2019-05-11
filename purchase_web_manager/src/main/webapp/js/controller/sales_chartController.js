// 定义控制器:
app.controller("salesChartController",function($scope,$controller,$http,salesService){
    myChart = echarts.init(document.getElementById('main'));

    var data =  {
        //legendData: legendData,
        seriesData:  [{"name":"dubin","value":"503"}],
        selected: null
    };

    var option = {
        title : {
            text: '同名数量统计',
            subtext: '纯属虚构',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            type: 'scroll',
            orient: 'vertical',
            right: 10,
            top: 20,
            bottom: 20,
            data: data.legendData,

            selected: data.selected
        },
        series : [
            {
                name: '姓名',
                type: 'pie',
                radius : '55%',
                center: ['40%', '50%'],
                data: data.seriesData,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    var getNewTime = function () {
        var date = new Date();
        var fullYear = date.getFullYear();
        var month = date.getMonth() + 1;
        var ms = month < 10 ? "-" + "0" + month : "-" + month;
        var rs = fullYear + ms;
        return rs;
    }
    // 向后台发送请求:
    $scope.showData = function(flag){
        if(flag==0){
            var newDate=getNewTime()+"-01";
            var month=parseInt(newDate.substr(5,2));
            var year=parseInt(newDate.substr(0,4));
            year=year-1;
            $scope.start = year+"-"+month+"-01";
            $scope.end=newDate;
        }
        salesService.salesChart($scope.start,$scope.end).success(function(response){
            var data = response;
            var option = {
                title : {
                    text: '同名数量统计',
                    subtext: '纯属虚构',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    orient: 'vertical',
                    right: 10,
                    top: 20,
                    bottom: 20,
                    data: data.legendData,

                    selected: data.selected
                },
                series : [
                    {
                        name: '姓名',
                        type: 'pie',
                        radius : '55%',
                        center: ['40%', '50%'],
                        data: data.seriesData,
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };
            myChart.setOption(option);
        });
    }
});
