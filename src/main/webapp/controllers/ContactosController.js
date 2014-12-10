luckynumbersApp.controller('ContactosController', function ($scope, $filter, GetCities, GetContacts, NewContact, UpdateContact) {

	$scope.loading = true;
    $scope.addMode = false;
    $scope.editMode = false;
    $scope.newContact = {};
    $scope.formError = false;
    $scope.rowActual = 0;


	GetContacts.get(function(data) {
	   $scope.contactos = data;
	 });

	$scope.toggleEdit = function (usuario, $index) {  
	        $scope.editMode = !$scope.editMode;
	        $scope.rowActual = index;
	    };

	$scope.mostrarContacto = function (index) {
	            if (($scope.editMode) & (index == $scope.rowActual)) {
	            	return true;
	            }
	            else {
	            	return false;
	            }
	        };

     $scope.toggleAdd = function () {  
	        $scope.addMode = !$scope.addMode;  
	    };

	 $scope.add = function () {


	 		var addToArray=true;
	 		for(var i=0;i<$scope.contactos.length;i++){
	 			if($scope.contactos[i].email1==$scope.newContact.email1){
	 				addToArray=false;
	 			}
	 		}

	 		if (addToArray) {
	 			NewContact.post({}, $scope.newContact, function(data) {
	 				$scope.data = function() { return GetContacts.get(function(data) {
	 					$scope.contactos = data;
	 				}); }
	 				$scope.addMode = false;
	 				$scope.contactos.push($scope.newContact);
	 				$scope.newContact = {};
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
	 	UpdateContact.post(params,activo);
	 	$scope.data = function() { return GetContacts.get(); }
	 	$scope.editMode = false;
	 }
	});