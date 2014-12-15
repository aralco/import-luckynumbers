luckynumbersApp.controller('UsuariosController', function ($scope, $filter, GetCities, GetUsers, NewUser, UpdateUser) {


	$scope.loading = true;
	$scope.addMode = false
	$scope.ElUsuario = false;
	$scope.newUser = {};
	$scope.formError = false;
	$scope.rowActual = 0;


	GetUsers.get(function(data) {
		$scope.usuarios = data;
		$scope.loading = false;
	});

	$scope.roles = [
	{name:'Administrador', value:'ROLE_ADMIN'},
	{name:'Parametrizador', value:'ROLE_USER'}
	];

	$scope.toggleEdit = function (usuario, index) {
		$scope.ElUsuario = !$scope.ElUsuario;
		$scope.rowActual = index;
	};

	$scope.mostrarUsuario = function (index) {
		if (($scope.ElUsuario) & (index == $scope.rowActual)) {
			return true;
		}
		else {
			return false;
		}
	};


	$scope.toggleAdd = function () {
		$scope.addMode = !$scope.addMode;
	}

	$scope.add = function () {


		var addToArray=true;
		for(var i=0;i<$scope.usuarios.length;i++){
			if($scope.usuarios[i].username==$scope.newUser.username){
				addToArray=false;
			}
		}

		if (addToArray) {
			NewUser.post({}, $scope.newUser, function(data) {
				$scope.data = function() { return GetUsers.get(function(data) {
					$scope.usuarios = data;
				}); }
				$scope.addMode = false;
				$scope.usuarios.push($scope.newUser);
				$scope.newUser = {};
			},
			function(error) {
				$scope.formError = true;
				$scope.errorMessage = error.statusText;

			});
		}  else {
			$scope.formError = true;
			$scope.errorMessage = "Datos Duplicados";
		}

	}


	$scope.save = function(activo) {
		var params = {Id:activo.id};
		UpdateUser.post(params,activo);
		$scope.data = function() { return GetUsers.get(); }
		$scope.ElUsuario = !$scope.ElUsuario;
	}
});