'use strict';

/* Services */

luckynumbersApp.factory('NewJob', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/scheduler/job', {}, {
            'post': { method: 'POST'}
        });
    });

luckynumbersApp.factory('NewTask', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/scheduler/task/:jobId', {jobId:'@jobId'}, {
            'post': { method: 'POST'}
        });
    });

luckynumbersApp.factory('UpdateTask', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/scheduler/task/:Id', {Id:'@jId'}, {
            'post': { method: 'PUT'}
        });
    });

luckynumbersApp.factory('GetTaskFileIn', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/monitor/audit/in/:Id', {Id:'@jId'}, {
            'get': { method: 'GET', isArray: true, cache: false}
        });
    });


luckynumbersApp.factory('GetTaskFileOut', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/monitor/audit/out/:Id', {Id:'@jId'}, {
            'get': { method: 'GET', isArray: true, cache: false}
        });
    });



luckynumbersApp.factory('GetCities', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/city', {}, {
             'get': { method: 'GET', params: {}, isArray: true}
        });
    });

luckynumbersApp.factory('NewCity', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/city', {}, {
            'post': { method: 'POST'}
        });
    });

luckynumbersApp.factory('UpdateCity', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/city/:Id', {Id:'@jId'}, {
            'post': { method: 'POST'}
        });
    });



luckynumbersApp.factory('GetUsers', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/user', {}, {
             'get': { method: 'GET', params: {}, isArray: true}
        });
    });

luckynumbersApp.factory('NewUser', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/user', {}, {
            'post': { method: 'POST'}
        });
    });

luckynumbersApp.factory('UpdateUser', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/user/:Id', {Id:'@jId'}, {
            'post': { method: 'POST'}
        });
    });



luckynumbersApp.factory('GetContacts', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/contact', {}, {
             'get': { method: 'GET', params: {}, isArray: true}
        });
    });

luckynumbersApp.factory('NewContact', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/contact', {}, {
            'post': { method: 'POST'}
        });
    });

luckynumbersApp.factory('UpdateContact', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/contact/:Id', {Id:'@jId'}, {
            'post': { method: 'POST'}
        });
    });




luckynumbersApp.factory('DeleteJob', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/scheduler/job/:id1', {id1:'@id'}, {
            'delete': { method: 'DELETE'}
        });
    });

luckynumbersApp.factory('DeleteTask', function ($resource) {
                return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/scheduler/task/:id1', {id1:'@id'}, {
                    'delete': { method: 'DELETE'}
                });
            });

luckynumbersApp.factory('ModifyJob', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/scheduler/job/:id1',
            {id1:'@id'}, {
            'update': { method: 'POST'}
        });
    });

luckynumbersApp.factory('Account', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/sysutils/loggeduser', {}, {
        });
    });

luckynumbersApp.factory('Jobs', function ($resource) {
        return $resource(URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/monitor', {}, {
            'get': { method: 'GET', isArray: true}
        });
    });

luckynumbersApp.factory('MetricsService',function ($http) {
            return {
            get: function() {
                var promise = $http.get('metrics/metrics').then(function(response){
                    return response.data;
                });
                return promise;
            }
        };
    });

luckynumbersApp.factory('ThreadDumpService', function ($http) {
        return {
            dump: function() {
                var promise = $http.get('dump').then(function(response){
                    return response.data;
                });
                return promise;
            }
        };
    });

luckynumbersApp.factory('HealthCheckService', function ($rootScope, $http) {
        return {
            check: function() {
                var promise = $http.get('health').then(function(response){
                    return response.data;
                });
                return promise;
            }
        };
    });

luckynumbersApp.factory('LogsService', function ($resource) {
        return $resource('app/rest/logs', {}, {
            'findAll': { method: 'GET', isArray: true},
            'changeLevel':  { method: 'PUT'}
        });
    });

luckynumbersApp.factory('AuditsService', function ($http) {
        return {
            findAll: function() {
                var promise = $http.get('app/rest/audits/all').then(function (response) {
                    return response.data;
                });
                return promise;
            },
            findByDates: function(fromDate, toDate) {
                var promise = $http.get('app/rest/audits/byDates', {params: {fromDate: fromDate, toDate: toDate}}).then(function (response) {
                    return response.data;
                });
                return promise;
            }
        }
    });

luckynumbersApp.factory('Session', function () {
        this.create = function (login, userId, userRoles) {
            this.login = login;
            this.userId = userId;
            this.userRoles = userRoles;
        };
        this.invalidate = function () {
            this.login = null;
            this.userId = null;
            this.userRoles = null;
        };
        this.getRole = function() {
            return this.userRoles;
        };
        return this;
    });

luckynumbersApp.factory('AuthenticationSharedService', function ($rootScope, $http, authService, Session, Account, Base64Service, AccessToken) {
        return {
            login: function (param) {
                var data = "username=" + param.username + "&password=" + param.password + "&grant_type=password&scope=app&client_secret=botigo&client_id=tigobo";
                $http.post(URL.host + ':'+URL.port+'/import-luckynumbers/oauth/token', data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "Accept": "application/json",
                        "access_type" : "offline"
                    },
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    httpHeaders.common['Authorization'] = 'Bearer ' + data.access_token;
                    AccessToken.set(data);

                    Account.get(function(data) {
                       Session.create(data.name, data.id, data.role);
                       $rootScope.account = Session;
                       $rootScope.loggedUsername = Session.login;
                        authService.loginConfirmed(data);
                    });
                }).error(function (data, status, headers, config) {
                    $rootScope.authenticated = false;
                    Session.invalidate();
                    AccessToken.remove();
                    delete httpHeaders.common['Authorization'];

                    if (typeof data.error !== "undefined"){
                        $rootScope.errorMessage=data.error_description;
                    } else {
                       $rootScope.errorMessage="Error de autenticación"
                    }
                    $rootScope.authenticationError = true;
                    $rootScope.$broadcast('event:auth-loginRequired', data);

                });
            },
            valid: function (authorizedRoles) {
                if(AccessToken.get() !== null) {
                    httpHeaders.common['Authorization'] = 'Bearer ' + AccessToken.get();
                }

                $http({
                        method: 'GET',
                        url: URL.host + ':'+URL.port+'/import-luckynumbers/luckynumbers/config/city',
                        data: {'key1':'value1', 'key2':'value2'},
                        headers: {"Content-Type": "application/json"},
                        ignoreAuthModule: 'ignoreAuthModule'

                }).success(function (data, status, headers, config) {
                    if (!Session.login || AccessToken.get() != undefined) {
                        if (AccessToken.get() == undefined || AccessToken.expired()) {
                            $rootScope.$broadcast("event:auth-loginRequired");
                            return;
                        }
                        Account.get(function(data) {
                            Session.create(data.name, data.id, data.role);
                            $rootScope.account = Session;
                            $rootScope.loggedUsername = Session.login;
                            if (!$rootScope.isAuthorized(authorizedRoles)) {
                                // user is not allowed
                               $rootScope.$broadcast("event:auth-notAuthorized");
                            } else {
                                $rootScope.$broadcast("event:auth-loginConfirmed");
                            }
                        });
                    }else{
                        if (!$rootScope.isAuthorized(authorizedRoles)) {
                                // user is not allowed
                                $rootScope.$broadcast("event:auth-notAuthorized");
                        } else {
                                $rootScope.$broadcast("event:auth-loginConfirmed");
                        }
                    }
                }).error(function (data, status, headers, config) {
                    if (!$rootScope.isAuthorized(authorizedRoles)) {
                        $rootScope.$broadcast('event:auth-loginRequired', data);
                    }
                });
            },
            isAuthorized: function (authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    if (authorizedRoles == '*') {
                        return true;
                    }

                    authorizedRoles = [authorizedRoles];
                }

                var isAuthorized = false;
                angular.forEach(authorizedRoles, function(authorizedRole) {
                    var authorized = (!!Session.login &&
                        Session.userRoles.indexOf(authorizedRole) !== -1);

                    if (authorized || authorizedRole == '*') {
                        isAuthorized = true;
                    }
                });

                return isAuthorized;
            },
            logout: function () {
                $rootScope.authenticationError = false;
                $rootScope.authenticated = false;
                $rootScope.account = null;
                AccessToken.remove();

                $http.get(URL.host + ':'+URL.port+'/import-luckynumbers/logout');
                Session.invalidate();
                delete httpHeaders.common['Authorization'];
                authService.loginCancelled();
                $rootScope.loggedUsername="";
            }
        };
    });