//控制层
app.controller('orderSticController', function ($scope, orderSticService) {
    var myChart = echarts.init(document.getElementById('main'));
    var getNewTime = function () {
        var date = new Date();
        var fullYear = date.getFullYear();
        var month = date.getMonth() + 1;
        var ms = month < 10 ? "-" + "0" + month : "-" + month;
        var rs = fullYear + ms;
        return rs;
    }
    $scope.search={};
    $scope.showData = function (flag) {
        if (flag == 0) {
            var newDate = getNewTime();
            var year = parseInt(newDate.substr(0, 4));
            $scope.year = parseInt(newDate.substr(0, 4));
            $scope.search.goodsName="";
        } else {
            $scope.year = $scope.search.year;
        }
        orderSticService.showData($scope.year, $scope.search.goodsName).success(function (response) {
            $scope.data = response;
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '订单统计图', //标题
                    subtext: '数量' //子标题
                },
                tooltip: {//提示框，鼠标悬浮交互时的信息提示
                    trigger: 'axis'//值为axis显示该列下所有坐标轴对应数据，值为item时只显示该点数据
                },
                legend: { //图例，每个图表最多仅有一个图例
                    data: ['数量']
                },
                calculable: true,//是否启用拖拽重计算特性
                xAxis: [{
                    type: 'category',  //坐标轴类型，横轴默认为类目型'category'
                    boundaryGap: false,
                    data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']//数据项
                }],
                yAxis: [{
                    type: 'value', //坐标轴类型，纵轴默认为数值型'value'
                    axisLabel: {
                        formatter: '{value} 笔' //加上单位
                    }
                }],
                series: [{//设置图表数据
                    name: '数量', //系列名称，如果启用legend，该值将被legend.data索引相关
                    type: 'line',//图表类型
                    data: $scope.data,
                    markPoint: {  //系列中的数据标注内容
                        data: [{
                            type: 'max',
                            name: '最大值'
                        },
                            {
                                type: 'min',
                                name: '最小值'
                            }]
                    },
                    markLine: {//系列中的数据标线内容
                        data: [{
                            type: 'average',
                            name: '平均值'
                        }]
                    }
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        })
    }
});
