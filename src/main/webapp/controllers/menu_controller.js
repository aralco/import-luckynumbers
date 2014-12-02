angular.module('luckynumbersApp').controller('MenuController',
    ['$scope', '$routeSegment', function ($scope, $routeSegment) {

        $scope.$routeSegment = $routeSegment;
    }]);