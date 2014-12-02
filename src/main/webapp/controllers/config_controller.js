

luckynumbersApp.controller('ConfigController', function ($scope, $filter, GetCities, GetUsers) {
	$scope.previusTab = "0";
	$scope.actualTab = "1";

	GetUsers.get(function(data) {
	   $scope.usuarios = data;
	 });

	$scope.changeTab = function(cual) {
            $( "#" +  $scope.previusTab).removeClass( "active" );
            $( "#" +  cual).addClass( "active" );
            $scope.previusTab= cual;
        }

	});