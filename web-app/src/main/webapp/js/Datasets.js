var myApp = angular.module('dataFilter', ['restangular', 'ngResource', 'ui.bootstrap', 'ngRoute'])

    .config(function ($httpProvider, RestangularProvider) {

        $httpProvider.defaults.headers.common['Authorization'] = "Basic Y3BzMTVfYWRtaW46c2VjcmV0";
        RestangularProvider.setBaseUrl('/API');
        RestangularProvider.setDefaultHeaders({Authorization: "Basic Y3BzMTVfYWRtaW46c2VjcmV0"});

    })

    .controller('DataFilterCtrl', function ($scope, $window, $q, $interval, Restangular) {

        $scope.datasets = Restangular.all('dataset').getList().$object

        $scope.sortType = 'endDate';
        $scope.sortReverse = false;

        $scope.datasetForm = {
            show: false,
            dataset: {}
        };

        $scope.toggleDatasetForm = function () {
            $scope.datasetForm.show = !$scope.datasetForm.show;
        };

        $scope.clearForm = function () {
            $scope.datasetForm.dataset = {}
        };


        $scope.refreshTable = function () {
            $scope.datasets = Restangular.all('dataset').getList().$object
        };

        $interval($scope.refreshTable, 10000);

        $scope.addDataset = function (dataset) {

            if (dataset != undefined) {
                dataset.tags = dataset.tags.split(/[ ,]+/);

                console.log(dataset);

                Restangular.all('dataset').post(dataset).then(function(){
                    console.log("Dataset requested");
                }, function() {
                    console.log("Error requesting dataset");
                });
            }

        };

        $scope.startDataset = function (dataset) {

            Restangular.one("dataset",dataset.id).customPUT({'status': 'ORDERED'},"status").then(function(){
                console.log("Dataset request start");
            }, function() {
                console.log("Error requesting dataset start");
            });

            $scope.refreshTable();

        };

        $scope.deleteDataset = function (dataset) {

            Restangular.one("dataset", dataset.id).remove().then(function(){
                $scope.refreshTable();
            })
        };

        $scope.stopDataset = function (dataset) {


            Restangular.one("dataset",dataset.id).customPUT({'status': 'STOPPED'},"status").then(function(){
                console.log("Dataset request stop");
            }, function() {
                console.log("Error requesting dataset stop");
            });

            $scope.refreshTable();
        }

    });