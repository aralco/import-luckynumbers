luckynumbersApp.controller('ContactosController', function ($scope, $filter, GetCities, GetContacts, NewContact, UpdateContact) {

	$scope.loading = true;
    $scope.addMode = false;
    $scope.editMode = false;
    $scope.newContact = {};
    $scope.formError = false;


	GetContacts.get(function(data) {
	   $scope.contactos = data;
	 });

	$scope.toggleEdit = function (usuario) {  
	        $scope.editMode = !$scope.editMode; 
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