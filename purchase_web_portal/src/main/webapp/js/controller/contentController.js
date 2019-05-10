app.controller("contentController",function($scope,contentService){
	$scope.contentList = [];
    $scope.contentList1 = [];
    $scope.contentList2 = [];
	// 根据分类ID查询广告的方法:
	$scope.findByCategoryId = function(categoryId){
		contentService.findByCategoryId(categoryId).success(function(response){
			$scope.contentList[categoryId] = response;
		});
	}
	
	//搜索  （传递参数）
	$scope.search=function(){
		location.href="http://localhost:9103/search.html#?keywords="+$scope.keywords;
	}
	//获取手机通信的楼层轮播图片
    $scope.findByCategoryId1 = function(categoryId){
        contentService.findByCategoryId(categoryId).success(function(response){
            $scope.contentList1[categoryId] = response;
        });
    }
    //获取家用电器的楼层轮播图片
    $scope.findByCategoryId2 = function(categoryId){
        contentService.findByCategoryId(categoryId).success(function(response){
            $scope.contentList2[categoryId] = response;
        });
    }
	
});