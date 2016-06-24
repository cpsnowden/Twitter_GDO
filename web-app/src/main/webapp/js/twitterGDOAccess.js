var twitterGDOAccessApp = angular.module('twitterGDOAccess',['ngRoute','dataFilter']);

    twitterGDOAccessApp.config(function($routeProvider) {
        $routeProvider

            .when('/',{
                templateUrl:'partials/Welcome.html',
            })

            .when('/dataFilters',{
                templateUrl:'partials/DataFilters.html',
            });
    });

    twitterGDOAccessApp.controller('mainController', function($scope) {
        $scope.info = "Heloooooo"
    });