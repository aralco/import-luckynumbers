luckynumbersApp.controller('MisProgramacionesController', function ($scope, $filter, $location, Jobs, DeleteJob, ModifyJob, GetCities, NewTask, DeleteTask, UpdateTask, GetTaskFileIn, GetTaskFileOut) {

        $scope.success = null;
        $scope.error = null;

        var orderBy = $filter('orderBy');

        var programaciones = Jobs.get();

        $scope.programaciones = programaciones;

        $scope.noIniciados  = 0;
        $scope.enProceso = 0;
        $scope.conError = 0;
        $scope.finalizados = 0;
        $scope.showDetails = false;
        $scope.filtroactual = 'ALL';
        $scope.totalJobs  = 0;
        $scope.tipoNumeros = "FROZEN";
        $scope.ciudad = 1;


        GetCities.get(function(data) {
           $scope.ciudades = data;
           $scope.friendlyCities = {};
           angular.forEach(data, function(ciudad) {
                      $scope.friendlyCities[ciudad.code] = ciudad.name;
                   });
         });

        Jobs.get(function(data) {
           programaciones = data;
           $scope.seleccionado =  data[0];
           $scope.totalJobs = data.length;
           angular.forEach(data, function(job) {
                       if (job.state == "NOT_STARTED") {
                           $scope.noIniciados++;
                       }

                       if (job.state == "IN_PROGRESS") {
                           $scope.enProceso++;
                       }

                       if (job.state == "CRITERIA_ACCEPTANCE") {
                           $scope.conError++;
                       }

                       if (job.state == "DONE") {
                           $scope.finalizados++;
                       }
                   });
           $scope.order('-scheduleDate',false);
         });

        $scope.order = function(predicate, reverse) {
            $scope.programaciones = orderBy($scope.programaciones, predicate, reverse);
          };

        $scope.modal = {
        	"title":  "Confirmar",
        	"message": "¿Esta seguro que desea eliminar la programación?",
        	"button1": "Aceptar",
        	"button2": "Cancelar"

        }

        $scope.isModalOpen = false;


        $scope.friendlyState = function (state){

             if (state == "NOT_STARTED") {
                           return "Agendado";
                       }

             if (state == "IN_PROGRESS") {
                           return "En progreso";
                       }

             if (state == "CRITERIA_ACCEPTANCE") {
                           return "Procesado con error";
                       }

              if (state == "DONE") {
                           return "Finalizado";
                       }
        }


        $scope.filterSeleccionado = function(job)
        {
            if ($scope.filtroactual == 'ALL') {
                return true;
            }

            if(job.state == $scope.filtroactual)
            {
                return true; // this will be listed in the results
            }

            return false; // otherwise it won't be within the results
        };

        $scope.changeSeleccionado = function(cual) {
            $( "#" +  $scope.filtroactual).removeClass( "active" );
            $scope.filtroactual = cual;
            $( "#" +  $scope.filtroactual).addClass( "active" );
            $scope.showDetails = false;
            $( "#" +  $scope.previusID).removeClass( "selected-row" );
        }

        $scope.changeFuenteNumeros = function(theCurrent, theOld) {
            $( "#" +  theOld).removeClass( "active" );
            $( "#" +  theCurrent).addClass( "active" );
            $( "#" +  theOld + "S").removeClass( "fui-arrow-right" );
            $( "#" +  theCurrent + "S").addClass( "fui-arrow-right" );
            if (theCurrent == "congelados") {
                $scope.tipoNumeros = "FROZEN";
            }
            if (theCurrent == "libres") {
                $scope.tipoNumeros = "FREE";
            }
        }

        $scope.showDetalles = function(programa){
          if (!$scope.showDetails){
              $scope.seleccionado = programa;
              $scope.jobActivoId = programa.id;
              $scope.showDetails = true;
              $( "#" +  programa.id).addClass( "selected-row" );
              $scope.previusID = programa.id;
          } else {
            $( "#" +  $scope.previusID).removeClass( "selected-row" );
            $scope.showDetails = false;
            if (programa.id != $scope.previusID) {
              $scope.seleccionado = programa;
              $scope.jobActivoId = programa.id;
              $scope.showDetails = true;
              $( "#" +  programa.id).addClass( "selected-row" );
              $scope.previusID = programa.id;
            }
          }

        }

        $scope.removeRow = function(id, deDonde){
                 var index = -1;
                 var comArr = eval( deDonde );
                 for( var i = 0; i < comArr.length; i++ ) {
                       if( comArr[i].id === id ) {
                           index = i;
                           break;
                        }
                 }
                 if( index === -1 ) {
                      //alert( "Error!" );
                 }
                 deDonde.splice( index, 1 );
        };

        $scope.deleteJobByID = function(){
          var params = {id1:$scope.jobActivoId};
          DeleteJob.delete(params);
          $scope.removeRow($scope.jobActivoId,$scope.programaciones);
          $scope.noIniciados--;
          $scope.data = function() { return Jobs.get(); }
          $scope.modal.modalError = false;
            $scope.isModalOpen = false;
        }

        $scope.deleteTaskByID = function(){
          var params = {id1:$scope.taskActivaId};
          DeleteTask.delete(params);
          $scope.removeRow($scope.taskActivaId,$scope.seleccionado.tasks);
          $scope.data = function() { return Jobs.get(); }
          $scope.modal.modalError = false;
            $scope.isModalOpen = false;
        }

        $scope.modifyDateByID = function(){
          var params = {id1:$scope.jobActivoId};

          if ($scope.modal.updateDate < new Date()) {
                $scope.modal.modalError = true;
                $scope.modal.errorMessage = "La fecha no puede ser menor al día de hoy";
                $scope.isModalOpen = true;
                return false;
          } else {
            $scope.modal.modalError = false;
            $scope.isModalOpen = false;
          }


          $scope.serverdate = $filter('date')($scope.modal.updateDate,'yyyy-MM-ddTHH:mm:ss') + "-04:00";
          $scope.jobActivoRecord.scheduledDate = $scope.serverdate;
          ModifyJob.update(params,$scope.jobActivoRecord);
          $scope.data = function() { return Jobs.get(); }
        }

        $scope.modifyNowByID = function(){
          var params = {id1:$scope.jobActivoId};
          $scope.jobActivoRecord.now = true;
          ModifyJob.update(params,$scope.jobActivoRecord);
          $scope.data = function() { return Jobs.get(); }
          $scope.modal.modalError = false;
          $scope.isModalOpen = false;
        }

        $scope.updateTaskByID = function(){
          var params = {Id:$scope.taskActivaId};
          $scope.taskActiva.from = $scope.modal.desde;
          $scope.taskActiva.to = $scope.modal.hasta;
          $scope.taskActiva.executionDate = $filter('date')($scope.taskActiva.executionDate,'yyyy-MM-ddTHH:mm:ss') + "-04:00";
          $scope.taskActiva.createdDate = $filter('date')($scope.taskActiva.createdDate,'yyyy-MM-ddTHH:mm:ss') + "-04:00";
          $scope.taskActiva.lastUpdate = $filter('date')($scope.taskActiva.lastUpdate,'yyyy-MM-ddTHH:mm:ss') + "-04:00";
          UpdateTask.post(params,$scope.taskActiva);
          $scope.data = function() { return Jobs.get(); }
          $scope.modal.modalError = false;
            $scope.isModalOpen = false;
        }

        $scope.createTaskByID = function(){
          NewTask.post({jobId: $scope.jobActivoId}, {"city" : parseInt($scope.modal.ciudad),"type":$scope.tipoNumeros,"from" : $scope.modal.desde,"to": $scope.modal.hasta} );
          $scope.data = function() { return Jobs.get(); }
          $scope.modal.modalError = false;
            $scope.isModalOpen = false;
        }

        $scope.editDate = function(programa) {
          if (!$scope.isModalOpen){
            $scope.modal.title = "Modificar fecha programada";
            $scope.modal.modalError = false;
            $scope.modal.showDate = true;
            $scope.modal.message =  "";
            $scope.isModalOpen = true;
            $scope.modal.showCTask = false;
            $scope.modal.showRangos = false;
            $scope.modal.showSummary = false;
            $scope.modal.updateDate = programa.scheduledDate;
            $scope.jobActivoId = programa.id;
            $scope.jobActivoRecord = programa;
             $scope.modal.modalError = false;
            $scope.funcionBorrar = $scope.modifyDateByID;
          }

        }

        $scope.playNow = function(mensaje, programa){
          if (!$scope.isModalOpen){
            $scope.modal.message = mensaje;
            $scope.modal.title = "Ejecución"
            $scope.modal.showDate = false;
            $scope.isModalOpen = true;
            $scope.modal.showCTask = false;
            $scope.modal.showRangos = false;
            $scope.modal.showSummary = false;
            $scope.jobActivoId = programa.id;
            $scope.jobActivoRecord = programa;
             $scope.modal.modalError = false;
            $scope.funcionBorrar = $scope.modifyNowByID;

          }
        }

        $scope.openModal = function(mensaje, programa){
          if (!$scope.isModalOpen){
           $scope.modal.message = mensaje;
           $scope.modal.modalError = false;
           $scope.modal.title = "Eliminar"
           $scope.modal.showDate = false;
           $scope.modal.showCTask = false;
           $scope.modal.showRangos = false;
           $scope.modal.showSummary = false;
           $scope.isModalOpen = true;
           $scope.jobActivoId = programa.id;
            $scope.modal.modalError = false;
           $scope.funcionBorrar = $scope.deleteJobByID;
         }
       }

        $scope.editRangos = function(mensaje, indice){
          if (!$scope.isModalOpen){
           $scope.modal.message = mensaje;
           $scope.modal.title = "Cambiar Rangos"
           $scope.modal.showDate = false;
           $scope.modal.showCTask = false;
           $scope.modal.showRangos = true;
           $scope.isModalOpen = true;
           $scope.modal.showSummary = false;
           $scope.taskActivaId = $scope.seleccionado.tasks[indice].id;
           $scope.taskActiva = $scope.seleccionado.tasks[indice];
           $scope.modal.desde = $scope.taskActiva.from;
           $scope.modal.hasta = $scope.taskActiva.to;
            $scope.modal.modalError = false;
           $scope.funcionBorrar = $scope.updateTaskByID;
         }
       }

        $scope.openDeleteTask = function(mensaje, indice){
          if (!$scope.isModalOpen){
           $scope.modal.message = mensaje;
           $scope.modal.title = "Eliminar"
           $scope.modal.showDate = false;
           $scope.modal.showCTask = false;
           $scope.modal.showRangos = false;
           $scope.modal.showSummary = false;
           $scope.isModalOpen = true;
            $scope.modal.modalError = false;
           $scope.taskActivaId = $scope.seleccionado.tasks[indice].id;
           $scope.funcionBorrar = $scope.deleteTaskByID;
         }
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
           $scope.modal.modalError = false;
          $scope.funcionBorrar = $scope.createTaskByID;
        }
      }

      $scope.showSummaryLog = function(programa)  {
        if (!$scope.isModalOpen){
          $scope.modal.message = "";
          $scope.modal.title = "Detalle"
          $scope.modal.showDate = false;
          $scope.modal.showCTask = false;
          $scope.modal.showRangos = false;
          $scope.modal.showSummary = true;
          $scope.isModalOpen = true;
          $scope.modal.ciudad ="0";
          $scope.modal.desde = "";
          $scope.modal.hasta = "";
           $scope.modal.modalError = false;
          if (programa.summary != null){
               //$scope.modal.jobSummary = programa.summary.replace("||",String.fromCharCode(13));
               $scope.modal.jobSummary = programa.summary.split("||").join("\n");
          } else {
              $scope.modal.jobSummary = "";
          }
          $scope.funcionBorrar = null;
        }
      }


      $scope.showSummaryTask = function(tarea)  {
        if (!$scope.isModalOpen){
          $scope.modal.message = "";
          $scope.modal.title = "Detalle"
          $scope.modal.showDate = false;
          $scope.modal.showCTask = false;
          $scope.modal.showRangos = false;
          $scope.modal.showSummary = true;
          $scope.isModalOpen = true;
          $scope.modal.ciudad ="0";
          $scope.modal.desde = "";
          $scope.modal.hasta = "";
           $scope.modal.modalError = false;
          if (tarea.summary != null){
               $scope.modal.jobSummary = tarea.summary.split("||").join("\n");
          } else {
              $scope.modal.jobSummary = "";
          }
          $scope.funcionBorrar = null;
        }
      }

      $scope.showFileTask = function(inIs, tarea) {

        if (!$scope.isModalOpen){
          $scope.modal.message = "";
          $scope.modal.title = "File " + inIs;
          $scope.modal.showDate = false;
          $scope.modal.showCTask = false;
          $scope.modal.showRangos = false;
          $scope.modal.showSummary = true;
          $scope.isModalOpen = true;
          $scope.modal.ciudad ="0";
          $scope.modal.desde = "";
          $scope.modal.hasta = "";
          $scope.modal.modalError = false;
          $scope.taskActivaId = tarea.id;
          var params = {Id:$scope.taskActivaId};

          if (inIs == "In") {
          GetTaskFileIn.get(params, function(data) {
             //$scope.modal.jobSummary = data.toString();
             $scope.modal.jobSummary = data.toString().split(",").join("\n");
           });
          } else {
            GetTaskFileOut.get(params, function(data) {
               //$scope.modal.jobSummary = data.toString();
               $scope.modal.jobSummary = data.toString().split(",").join("\n");
             });
          }
          $scope.funcionBorrar = null;
        }
      }


    });
