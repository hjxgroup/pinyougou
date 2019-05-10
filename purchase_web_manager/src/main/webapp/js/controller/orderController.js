app.controller('orderController' ,function($scope,$controller,$location,orderService){
    $controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中  测试
    $scope.findAll=function(){
        orderService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    //分页
    $scope.findPage=function(page,rows){
        orderService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
            }
        );
    }



    //保存
    $scope.save=function(){
        // 再添加之前，获得富文本编辑器中的内容。
        $scope.entity.orderDesc.introduction=editor.html();
        var serviceObject;//服务层对象
        if($scope.entity.order.id!=null){//如果有ID
            serviceObject=orderService.update( $scope.entity ); //修改
        }else{
            serviceObject=orderService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.flag){
                    //重新查询
                    alert(response.message);
                    location.href="order.html";
                }else{
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        orderService.dele( $scope.selectIds ).success(
            function(response){
                if(response.flag){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity={};//定义搜索对象
    //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
    $scope.status=["全部","未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];
    $scope.paymentType=["","在线支付","货到付款"]
    //搜索
    $scope.search=function(page,rows){
        orderService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
            }
        );
    }

    // $scope.entity={order:{},orderDesc:{},itemList:[]}

    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.flag){
                // 获得url
                $scope.image_entity.url =  response.message;
            }else{
                alert(response.message);
            }
        });
    }

    // 获得了image_entity的实体的数据{"color":"褐色","url":"http://192.168.209.132/group1/M00/00/00/wKjRhFn1bH2AZAatAACXQA462ec665.jpg"}
    $scope.entity={order:{},orderDesc:{itemImages:[],specificationItems:[]}};

    $scope.add_image_entity = function(){
        $scope.entity.orderDesc.itemImages.push($scope.image_entity);
    }

    $scope.remove_image_entity = function(index){
        $scope.entity.orderDesc.itemImages.splice(index,1);
    }

    // 创建SKU的信息:
    $scope.createItemList = function(){
        // 初始化基础数据:
        $scope.entity.itemList = [{spec:{},price:0,num:9999,status:'0',isDefault:'0'}];

        var items = $scope.entity.orderDesc.specificationItems;

        for(var i=0;i<items.length;i++){
            //
            $scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
        }
    }
    $scope.findNum=function () {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));



        orderService.findNum($scope.selectYear).success(
            function (response) {
                $scope.data=response;
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
                    toolbox: {//工具栏
                        show: true,
                        feature: {
                            mark: {
                                show: true
                            },
                            dataView: { //数据视图
                                show: true,
                                readOnly: false//是否只读
                            },
                            magicType: {//切换图表
                                show: true,
                                type: ['line', 'bar', 'stack', 'tiled']//图表
                            },
                            restore: {//还原原始图表
                                show: true
                            },
                            saveAsImage: {//保存图片
                                show: true
                            }
                        }
                    },
                    calculable: true,//是否启用拖拽重计算特性
                    xAxis: [{
                        type: 'category',  //坐标轴类型，横轴默认为类目型'category'
                        boundaryGap: false,
                        data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月','十二月']//数据项
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
            }
        )


    }

    addColumn = function(list,columnName,columnValues){
        // 定义一个集合用于保存生成的每行的数据:
        var newList = [];
        // 遍历该集合的数据:
        for(var i=0;i<list.length;i++){
            var oldRow = list[i];
            for(var j=0;j<columnValues.length;j++){
                // 对oldRow数据进行克隆:
                var newRow = JSON.parse( JSON.stringify(oldRow) );
                newRow.spec[columnName]=columnValues[j];
                // 将newRow存入到newList中
                newList.push(newRow);
            }

        }

        return newList;
    }

});