luckynumbersApp.controller('NuevaProgramacionController', function ($scope, $rootScope, $filter, $location, GetCities, NewTask, NewJob, Jobs) {

	$scope.tareas = [];
	$scope.tarea = {};
	$scope.inmediato = false;
	$scope.dateNow  = !$scope.inmediato;
	$scope.tipoNumeros = "FROZEN";
	$scope.ciudad = 1;
	$rootScope.formError = false;

	GetCities.get(function(data) {
	   $scope.ciudades = data;
	   $scope.friendlyCities = {};
	   angular.forEach(data, function(ciudad) {
	              $scope.friendlyCities[ciudad.code] = ciudad.name;
	           });
	 });

	$scope.changeClick = function() {
		$scope.inmediato = !$scope.inmediato;
		$scope.dateNow  = !$scope.inmediato;
	}

	$scope.cancelarFn = function() {
		$location.path('/mprogramaciones').replace();
	}

	$scope.newJobFn = function() {
		$scope.inmediato = document.getElementById("checkbox1").checked;
		$rootScope.formError = false;

		if ($scope.new.date < new Date()) {
			$rootScope.formError = true;
			$rootScope.errorMessage = "La fecha debe ser mayor a la actual";
			return false;
		}

		if (($scope.new.date == null) &  (!$scope.inmediato)) {
			$rootScope.formError = true;
			$rootScope.errorMessage = "La fecha debe ser ingresada";
			return false;
		}

		if (!$scope.inmediato) {
			$scope.serverdate = $filter('date')($scope.new.date,'yyyy-MM-ddTHH:mm:ss') + "-04:00";
		} else {
			$scope.serverdate = "0000-00-00T00:00:00-00:00";
		}
		if ($scope.new.nombre == null) {
			$scope.new.nombre == "";
		}
		NewJob.post({},
			{"name" : $scope.new.nombre,"description": $scope.new.nombre,
			 "now" : $scope.inmediato,"scheduledDate": $scope.serverdate , 'tasks':$scope.tareas} );
		$scope.data = function() { return Jobs.get(); }
		$location.path('/mprogramaciones').replace();
	}

	$scope.modal = {
        	"title":  "Confirmar",
        	"message": "¿Esta seguro que desea eliminar la programación?",
        	"button1": "Aceptar",
        	"button2": "Cancelar"
        }

    $scope.isModalOpen = false;


    $scope.changeFuenteNumerosN = function(theCurrent, theOld) {
        $( "#" +  theOld + "1").removeClass( "active" );
        $( "#" +  theCurrent + "1").addClass( "active" );
        $( "#" +  theOld + "P").removeClass( "fui-arrow-right" );
        $( "#" +  theCurrent + "P").addClass( "fui-arrow-right" );
        if (theCurrent == "congelados") {
            $scope.tipoNumeros = "FROZEN";
        }
        if (theCurrent == "libres") {
            $scope.tipoNumeros = "FREE";
        }
    }


	$scope.createTaskByID = function(){
         $scope.tareas.push({ 'type':$scope.modal.tipoNumeros, 'city': $scope.modal.ciudad, 'from':$scope.modal.desde,  'to':$scope.modal.hasta});
        }


	 $scope.createTask = function() {
	  if (!$scope.isModalOpen){
	    $scope.modal.message = "Ingrese los rangos de números para la tarea";
	    $scope.modal.title = "Crear nueva tarea"
	    $scope.modal.showDate = false;
	    $scope.modal.showCTask = true;
	    $scope.isModalOpen = true;
	    $scope.modal.showSummary = false;
	    $scope.modal.ciudad ="0";
	    $scope.modal.desde = "";
	    $scope.modal.hasta = "";
	    $scope.funcionBorrar = $scope.createTaskByID;
	  }
	}

});
