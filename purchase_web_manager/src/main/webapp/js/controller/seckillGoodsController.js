//控制层
app.controller('seckillGoodsController' ,function($scope,$controller,$location,seckillGoodsService,$interval){
    // AngularJS中的继承:伪继承
    $controller('baseController',{$scope:$scope});
	//读取列表数据绑定到表单中
	$scope.findList=function(){
		seckillGoodsService.findList().success(
			function(response){
				$scope.list=response;
			}
		);
	}
	//查询审核列表
    $scope.findCheckList=function(){
        seckillGoodsService.findCheckList().success(
            function(response){
                $scope.list=response;
            }
        );
    }
	
	//查询商品
	$scope.findOne=function(){
		//接收参数ID
		var id= $location.search()['id'];
		seckillGoodsService.findOne(id).success(
			function(response){
				$scope.entity=response;
				
				//倒计时开始
				//获取从结束时间到当前日期的秒数
				allsecond=  Math.floor( (new Date($scope.entity.endTime).getTime()- new Date().getTime())/1000 );
				 
				time= $interval(function(){
					allsecond=allsecond-1;
					$scope.timeString= convertTimeString(allsecond);
					
					if(allsecond<=0){
						$interval.cancel(time);
					}		
					
				},1000 );
				
			}		
		);		
	}
	
	
	//转换秒为   天小时分钟秒格式  XXX天 10:22:33
	convertTimeString=function(allsecond){
		var days= Math.floor( allsecond/(60*60*24));//天数
		var hours= Math.floor( (allsecond-days*60*60*24)/(60*60) );//小数数
		var minutes= Math.floor(  (allsecond -days*60*60*24 - hours*60*60)/60    );//分钟数
		var seconds= allsecond -days*60*60*24 - hours*60*60 -minutes*60; //秒数
		var timeString="";
		if(days>0){
			timeString=days+"天 ";
		}
		return timeString+hours+":"+minutes+":"+seconds;
		
	}
	
	//提交订单 
	$scope.submitOrder=function(){
		seckillGoodsService.submitOrder( $scope.entity.id ).success(
			function(response){
				if(response.flag){//如果下单成功
					alert("抢购成功，请在5分钟之内完成支付");
					location.href="pay.html";//跳转到支付页面				
				}else{
					alert(response.message);
				}				
			}
		);
		
	}

    //修改状态
    $scope.updateStatus=function(status){
        seckillGoodsService.updateStatus( $scope.selectIds,status).success(
            function(response){
                if(response.flag){//如果下单成功
                    alert(response.message);
                    $scope.findCheckList();//刷新列表
                    $scope.selectIds = [];
                }else{
                    alert(response.message);
                }
            }
        );

    }
	
});