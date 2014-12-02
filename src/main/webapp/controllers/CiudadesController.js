luckynumbersApp.controller('CiudadesController', function ($scope, $filter, GetCities, NewCity, UpdateCity) {


	$scope.loading = true;
	$scope.addMode = false;
	$scope.editMode = false;
	$scope.formError = false;
	$scope.newCity = {};


	GetCities.get(function(data) {
		$scope.ciudades = data;
	});

	$scope.toggleEdit = function (city) {  
		$scope.editMode = !$scope.editMode; 
	};  
	$scope.toggleAdd = function () {  
		$scope.addMode = !$scope.addMode;  
	}; 

	$scope.add = function () { 

		var addToArray=true;
		for(var i=0;i<$scope.ciudades.length;i++){
			if($scope.ciudades[i].code==parseInt($scope.newCity.code)){
				addToArray=false;
			}
		}

		if (addToArray) {
			NewCity.post({}, $scope.newCity, function(data) {
				$scope.data = function() { return GetCities.get(function(data) {
					$scope.ciudades = data;
				}); }
				$scope.addMode = false;
				$scope.ciudades.push($scope.newCity);
				$scope.newCity = {};
			},
			function(error) {
				$scope.formError = true;
				$scope.errorMessage = error.statusText;

			});
		}  else {
			$scope.formError = true;
			$scope.errorMessage = "Codigo duplicado";
		}

	}


	$scope.save = function(activo) {
		var params = {Id:activo.id};
		UpdateCity.post(params,activo);
		$scope.data = function() { return GetCities.get(); }
		$scope.editMode = !$scope.editMode; 
	}
});