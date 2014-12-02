luckynumbersApp.controller('ContactosController', function ($scope, $filter, GetCities, GetContacts, NewContact, UpdateContact) {

	$scope.loading = true;
    $scope.addMode = false;
    $scope.editMode = false;
    $scope.newContact = {};


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
	 		NewContact.post({},
	 		$scope.newContact );
	 		$scope.data = function() { return GetContacts.get(); }
	 		 $scope.addMode = false;
	 }

	 $scope.save = function(activo) {
	 	var params = {Id:activo.id};
	 	UpdateContact.post(params,activo);
	 	$scope.data = function() { return GetContacts.get(); }
	 	$scope.editMode = false;
	 }
	});