app.service("excelService", function ($http) {

    //导入品牌数据
    this.importBrandExcel = function (formData) {
        return $http({
            method: 'post',
            url: '/excel/importBrandExcel.do',
            data: formData,
            headers: {'Content-Type': undefined},
            transformRequest: angular.identity
        });
    }

    this.importSpecificationExcel=function (formData) {


        return $http({
            method:'post',
            url:'/excel/importSpecificationExcel.do',
            data: formData,
            headers: {'Content-Type': undefined},
            transformRequest: angular.identity
        })

    }

})