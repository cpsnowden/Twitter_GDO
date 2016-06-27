/**
 * Author: Per Spilling, per@kodemaker.no
 */


var myApp = angular.module('dataFilter', ['ngResource', 'ui.bootstrap','ngRoute']).config(function($httpProvider) {
        $httpProvider.defaults.headers.common['Authorization'] = "Basic Y3BzMTVfYWRtaW46c2VjcmV0";
}).controller('DataFilterCtrl',function($scope, DataFilterResource, DataFiltersResource, DataFilterStatusResource, $window, $q, $interval) {

    $scope.sortType = 'endDate';
    $scope.sortReverse = false;

    $scope.dataFilterForm = {
        show: false,
        person: {}
    };

    $scope.datasets = DataFiltersResource.query();

    $scope.togglePersonForm = function () {
        $scope.dataFilterForm.show = !$scope.dataFilterForm.show;
    };

    $scope.refreshTable = function() {
        $scope.datasets = DataFiltersResource.query();
    };

    $interval($scope.refreshTable, 10000);

    $scope.clearForm = function () {
        $scope.dataFilterForm.person = {}
    };

    $scope.savePerson = function (person) {
        if (person != undefined) {
            person.tags = person.tags.split(/[ ,]+/);
            console.log(person.tags);
            DataFilterResource.save(person).$promise.then(function() {
                $scope.datasets = DataFiltersResource.query();
                $scope.dataFilterForm.person = {};  // clear the form
            });
        }
    };

    $scope.startStream = function (p) {
        DataFilterStatusResource.update({id: p.id}, {'status':'ORDERED'}).$promise.then(function() {
            $scope.datasets = DataFiltersResource.query();
        })
    };

    $scope.deleteStream = function(person) {
        var msgBox = $window.confirm("Are your sure");

            if (msgBox) {
                // remove from the server and reload the person list from the server after the delete
                DataFilterResource.delete({id: person.id}).$promise.then(function() {
                    $scope.datasets = DataFiltersResource.query();
                })
            }



    };

    $scope.stopStream = function (p) {
        DataFilterStatusResource.update({id: p.id}, {'status':'STOPPED'}).$promise.then(function () {
            $scope.datasets = DataFiltersResource.query();
        })
    }

});


myApp.factory('DataFiltersResource', function ($resource) {

    return $resource('/API/dataset', {}, {});
});

myApp.factory('DataFilterResource', function ($resource) {
    return $resource('/API/dataset/:id', {}, {});
});

myApp.factory('DataFilterStatusResource', function ($resource) {
    return $resource('/API/dataset/:id/status', {}, {
        'update':{method:'PUT'}
    });
});
