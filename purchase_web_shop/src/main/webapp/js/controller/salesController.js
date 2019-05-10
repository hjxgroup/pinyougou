//控制层
app.controller('salesController', function ($scope, salesService) {

    $scope.morrisPram = {
        element: 'graph',
        //data: $scope.month_data,
        xkey: 'period',
        ykeys: ['licensed'],
        labels: ['销售金额']
    }

    $scope.getNewTime = function () {
        var date = new Date();
        var fullYear = date.getFullYear();
        var month = date.getMonth() + 1;
        var ms = month < 10 ? "-" + "0" + month : "-" + month;
        var rs = fullYear + ms;
        return rs;
    }

    $scope.data = {
        start: "2019-06-01",
        end: "2019-06-01"
    };
    //读取列表数据绑定到表单中
    $scope.sales_pre = function (flag) {
        if (flag == 1) {
            var dataItem = $('#daterange-btn span').html().split("-");
            if (dataItem.length > 1) {
                $scope.data.start = dataItem[0].substr(0,4)+"-"+dataItem[0].substr(5,2)+"-01";
                $scope.data.end = dataItem[1].substr(0,4)+"-"+dataItem[1].substr(5,2)+"-01";
            } else {
                var year=parseInt(dataItem[0].substr(0,4));
                var month=parseInt(dataItem[0].substr(5,2));
                if(month==12){
                    month=1;
                    year=year+1;
                    $scope.data.end = year+"-"+month+"-01";
                }else {
                    month=month+1;
                    $scope.data.end = year+"-"+month+"-01";
                }
                $scope.data.start = dataItem[0].substr(0,4)+"-"+dataItem[0].substr(5,2)+"-01";
            }
        }else {
            var newData=$scope.getNewTime()+"-01";
            var month=parseInt(newData.substr(5,2));
            var year=parseInt(newData.substr(0,4));
            year=year-1;
            $scope.data.start = year+"-"+month+"-01";
            $scope.data.end=newData;
        }
        salesService.sales_pre($scope.data).success(
            function (response) {
                $scope.morrisPram.data = response;
                $("#graph").html("");
                Morris.Line($scope.morrisPram);
            }
        );
    }
});

