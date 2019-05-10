app.controller('orderController' ,function($scope,$controller,$location,orderService){
    $controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中
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