

var myApp = angular.module('analytics', ['restangular','ngResource', 'ui.bootstrap','ngRoute'])

    .config(function(RestangularProvider) {

    RestangularProvider.setBaseUrl('/API');
    RestangularProvider.setDefaultHeaders({Authorization:  "Basic Y3BzMTVfYWRtaW46c2VjcmV0"});

    })

    .controller('AnalyticsCtrl',function($scope, $window, $q, $interval, Restangular) {

    $scope.datasets = Restangular.all('dataset').getList({status:"READY_FOR_ANALYTICS"}).$object

    $scope.sortType = 'endDate';
    $scope.sortReverse = false;

    $scope.analyticsForm = {
        show: false,
        order: {},
        dataset:{}
    };

    $scope.refreshSubTable = function(){
        $scope.datasets.forEach(function(e) {
            e.analytics = $scope.getAnalytics(e)
        })
    }

    $scope.refreshTable = function(){
        $scope.datasets = Restangular.all('dataset').getList({status:"READY_FOR_ANALYTICS"}).$object
    }

    $interval($scope.refreshSubTable, 10000);

    $scope.getAnalytics = function(dataset) {
        return Restangular.one("dataset",dataset.id).getList('analytics').$object
    }

    $scope.cancel = function () {
        $scope.analyticsForm.order = {};
        $scope.analyticsForm.show = false;
    };

    $scope.orderAnalytics = function(dataset) {

        $scope.analyticsForm.order.id = dataset.id;
        $scope.analyticsForm.show = true;
        $scope.analyticsForm.dataset = dataset;
    };

    $scope.newAnalytics = function(order) {

        Restangular.one("dataset",order.id).post("analytics",order)
        $scope.analyticsForm.dataset.analytics = $scope.getAnalytics($scope.analyticsForm.dataset)
        $scope.analyticsForm.dataset = {}
        $scope.analyticsForm.order = {};
        $scope.analyticsForm.show = false;
    };

    $scope.selectGraph = function(){

        $scope.analyticsForm.order.nodeLimit = $scope.analyticsForm.dataset.filterSize;
        $scope.analyticsForm.order.filterGiant = true;
    }


    $scope.download = function(analytics) {

        Restangular.one("dataset",analytics.datasetId).one("analytics",analytics.id).customGET("data").then(function(res) {
            var file = new Blob([res],{type:'text/plain'});
            saveAs(file,analytics.dataPath)
        })

    }

    $scope.delete = function(analytics,dataset) {
        Restangular.one("dataset",analytics.datasetId).one('analytics',analytics.id).remove().then(function(){
            dataset.analytics = $scope.getAnalytics(dataset)
        });
    };

    $scope.print = function() {
        console.log($scope.analyticsForm.order)
    }
    
});