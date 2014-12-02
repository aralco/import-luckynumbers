luckynumbersApp.controller('UsuariosController', function ($scope, $filter, GetCities, GetUsers, NewUser, UpdateUser) {


	$scope.loading = true;  
    $scope.addMode = false; 
    $scope.ElUsuario = false;
    $scope.newUser = {};


	GetUsers.get(function(data) {
	   $scope.usuarios = data;
	   $scope.loading = false;
	 });

	$scope.toggleEdit = function (usuario) {  
	        $scope.ElUsuario = !$scope.ElUsuario; 
	    };  
     $scope.toggleAdd = function () {  
	        $scope.addMode = !$scope.addMode;  
	    }; 

	 $scope.add = function () { 
	 		NewUser.post({},
	 		$scope.newUser );
	 		$scope.data = function() { return GetUsers.get(); }
	 		$scope.addMode = !$scope.addMode;  
	 }

	 $scope.save = function(activo) {
	 	var params = {Id:activo.id};
	 	UpdateUser.post(params,activo);
	 	$scope.data = function() { return GetUsers.get(); }
	 	$scope.ElUsuario = !$scope.ElUsuario; 
	 }
	});