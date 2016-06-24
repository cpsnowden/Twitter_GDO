/**
 * Author: Per Spilling, per@kodemaker.no
 */


var myApp = angular.module('dataFilter', ['ngResource', 'ui.bootstrap','ngRoute'], function ($dialogProvider) {
    $dialogProvider.options({backdropClick: false, dialogFade: true})
    }).config(function($httpProvider) {
        $httpProvider.defaults.headers.common['Authorization'] = "Basic Y3BzMTVfYWRtaW46c2VjcmV0";
}).controller('DataFilterCtrl',function($scope, DataFilterResource, DataFiltersResource, DataFilterStatusResource, $window, $q, $dialog, $interval) {
    /**
     * Define an object that will hold data for the form. The persons list will be pre-loaded with the list of
     * persons from the server. The dataFilterForm.person object is bound to the person form in the HTML via the
     * ng-model directive.
     */



    $scope.sortType = 'endDate';
    $scope.sortReverse = false;

    $scope.dataFilterForm = {
        show: false,
        person: {}
    };

    $scope.persons = DataFiltersResource.query();

    $scope.togglePersonForm = function () {
        $scope.dataFilterForm.show = !$scope.dataFilterForm.show;
    };

    $scope.refreshTable = function() {
        $scope.persons = DataFiltersResource.query();
    };

    $interval($scope.refreshTable, 10000)

    $scope.clearForm = function () {
        $scope.dataFilterForm.person = {}
    };

    $scope.savePerson = function (person) {
        if (person != undefined) {

            console.log(person.tags);
            var tempTags = person.tags;
            console.log(tempTags);
            person.tags = tempTags.split(" ,");
            console.log(person.tags);
            DataFilterResource.save(person).$promise.then(function() {
                $scope.persons = DataFiltersResource.query();
                $scope.dataFilterForm.person = {};  // clear the form
            });
        }
    };

    $scope.startStream = function (p) {
        DataFilterStatusResource.update({id: p.id}, {'status':'ORDERED'}).$promise.then(function() {
            $scope.persons = DataFiltersResource.query();
        })
    };

    $scope.deleteStream = function(person) {
        var msgBox = $window.confirm("Are your sure");

            if (msgBox) {
                // remove from the server and reload the person list from the server after the delete
                DataFilterResource.delete({id: person.id}).$promise.then(function() {
                    $scope.persons = DataFiltersResource.query();
                })
            }



    }

    //$scope.deleteStream = function(person) {
    //    DataFilterResource.delete({id: person.id}).$promise.then(function() {
    //        $scope.persons = DataFiltersResource.query();
    //    });
    //};

    $scope.stopStream = function (p) {
        DataFilterStatusResource.update({id: p.id}, {'status':'STOPPED'}).$promise.then(function () {
            $scope.persons = DataFiltersResource.query();
        })
    }

});


myApp.factory('DataFiltersResource', function ($resource) {

    return $resource('/api/dataCollections/dataFilter', {}, {});
});

myApp.factory('DataFilterResource', function ($resource) {
    return $resource('/api/dataCollections/dataFilter/:id', {}, {});
});

myApp.factory('DataFilterStatusResource', function ($resource) {
    return $resource('/api/dataCollections/dataFilter/:id/status', {}, {
        'update':{method:'PUT'}
    });
});
