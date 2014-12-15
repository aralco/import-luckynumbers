
'use strict';

/* App Module */
var httpHeaders;


var luckynumbersApp = angular.module('luckynumbersApp', ['ngRoute', 'route-segment', 'view-segment', 'http-auth-interceptor', 'ngResource', 'ngCookies', 'angular-loading-bar', 'ui.bootstrap.datetimepicker', 'luckynumbersAppUtils']);


luckynumbersApp.config(function ($routeSegmentProvider, $httpProvider, USER_ROLES) {
    $routeSegmentProvider.options.autoLoadTemplates = true;
 
    $routeSegmentProvider
        .when('/', 'mprogramaciones')
        .when('/mprogramaciones', 'mprogramaciones')
        .when('/nprogramacion', 'nprogramacion')
        .when('/login', 'login')
        .when('/logout', 'logout')
        .when('/error', 'error')
        .when('/invalido', 'invalido')
        .when('/intro', 'mintro')
        .when('/configuracion', 'mconfiguracion')
        .when('/usuarios', 'musuarios')
        .when('/contactos', 'mcontactos')
        .when('/ciudades', 'mciudades')

        .segment('mintro', {
            templateUrl: 'templates/intro.html',
            controller: "IntroController"
        })
 
        .segment('mprogramaciones',{
            default: true,
            templateUrl: 'templates/misprogramaciones.html',
            controller: 'MisProgramacionesController',
        })
        .segment('nprogramacion', {
            templateUrl: 'templates/nuevaprogramacion.html',
            controller: 'NuevaProgramacionController',
        })
        .segment('modal', {
            templateUrl: 'templates/modal.html',
            controller: 'DirectivesController',
        })
        .segment('login', {
            templateUrl: 'templates/login.html',
            controller: 'LoginController',
        })
        .segment('error', {
            templateUrl: 'templates/error.html',
        })
        .segment('invalido', {
            templateUrl: 'templates/invalidate.html',
        })
        .segment('logout', {
            templateUrl: 'templates/login.html',
            controller: 'LogoutController',
        })
        .segment('mconfiguracion', {
            templateUrl: 'templates/config.html',
            controller: 'ConfigController',
        })
        .segment('musuarios', {
            templateUrl: 'templates/usuarios.html',
            controller: 'UsuariosController',
        })
        .segment('mcontactos', {
            templateUrl: 'templates/contactos.html',
            controller: 'ContactosController',
        })
        .segment('mciudades', {
            templateUrl: 'templates/ciudades.html',
            controller: 'CiudadesController',
        })

httpHeaders = $httpProvider.defaults.headers;

})

.run(function($rootScope, $location, $http, AuthenticationSharedService, Session, USER_ROLES) {

                var pagesA = {};
                pagesA["mprogramaciones"] = {
                        authorizedRoles: [USER_ROLES.admin,USER_ROLES.user]
                    };
                pagesA["nprogramacion"] = {
                        authorizedRoles: [USER_ROLES.admin,USER_ROLES.user]
                    };
                pagesA["login"] = {
                        authorizedRoles: [USER_ROLES.all]
                    };
                pagesA["logout"] = {
                        authorizedRoles: [USER_ROLES.all]
                    };
                pagesA["error"] = {
                        authorizedRoles: [USER_ROLES.all]
                    };
                 pagesA["invalido"] = {
                        authorizedRoles: [USER_ROLES.all]
                    };
                pagesA["mintro"] = {
                        authorizedRoles: [USER_ROLES.admin,USER_ROLES.user]
                    };
                pagesA["mconfiguracion"] = {
                        authorizedRoles: [USER_ROLES.admin]
                    };
                pagesA["musuarios"] = {
                        authorizedRoles: [USER_ROLES.admin]
                    };
                pagesA["mcontactos"] = {
                        authorizedRoles: [USER_ROLES.admin]
                    };
                pagesA["mciudades"] = {
                        authorizedRoles: [USER_ROLES.admin]
                    };

                $rootScope.authenticated = false;
                $rootScope.loggedUsername = "";
                $rootScope.$on('$routeChangeStart', function (event, next) {
                    $rootScope.isAuthorized = AuthenticationSharedService.isAuthorized;
                    $rootScope.userRoles = USER_ROLES;

                    if (typeof next !== "undefined") {
                        if (typeof next.segment !== "undefined"){
                            AuthenticationSharedService.valid(pagesA[next.segment].authorizedRoles);
                        } else {
                           // AuthenticationSharedService.valid(pagesA[next.segment].authorizedRoles);
                        }
                    }

                });

                // Call when the the client is confirmed
                $rootScope.$on('event:auth-loginConfirmed', function(data) {
                    $rootScope.authenticated = true;
                    if ($location.path() === "/login") {
                        var search = $location.search();
                        if (search.redirect !== undefined) {
                             //$location.path('/mprogramaciones').replace();
                            $location.path(search.redirect).search('redirect', null).replace();
                        } else {
                            $location.path('/mprogramaciones').replace();
                        }
                    }
                    if ($location.path() === "/intro") {
                        $location.path('/mprogramaciones').replace();
                    }
                });

                // Call when the 401 response is returned by the server
                $rootScope.$on('event:auth-loginRequired', function(rejection) {
                    console.log("Invalidate!");
                    Session.invalidate();
                    $rootScope.authenticated = false;
                    if ($location.path() !== "" && $location.path() !== "/register" &&
                            $location.path() !== "/activate" && $location.path() !== "/login") {
                        var redirect = $location.path();
                        $location.path('/login').search('redirect', redirect).replace();
                    }
                });

                // Call when the 403 response is returned by the server
                $rootScope.$on('event:auth-notAuthorized', function(rejection) {
                    $rootScope.errorMessage = true;
                    $location.path('/invalido').replace();
                });

                // Call when the user logs out
                $rootScope.$on('event:auth-loginCancelled', function() {
                    $location.path('');
                });
        });
