var twitterGDOAccessApp = angular.module('twitterGDOAccess',['ngRoute','dataFilter','analytics']);

    twitterGDOAccessApp.config(function($routeProvider) {
        $routeProvider

            .when('/',{
                templateUrl:'partials/Welcome.html'
            })

            .when('/datasets',{
                templateUrl:'partials/Datasets.html'
            })

            .when('/analytics', {
                templateUrl:'partials/Analytics.html'
            });
    });

    twitterGDOAccessApp.controller('mainController', function($scope) {
        $scope.info = "Heloooooo"
    });