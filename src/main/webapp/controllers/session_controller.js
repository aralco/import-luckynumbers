angular.module('luckynumbersApp').controller('LoginController', function ($scope, $location, AuthenticationSharedService) {
        $scope.rememberMe = true;
        $scope.login = function () {
            AuthenticationSharedService.login({
                username: $scope.username,
                password: $scope.password
            });
        }
    });

angular.module('luckynumbersApp').controller('LogoutController', function ($location, AuthenticationSharedService) {
        AuthenticationSharedService.logout();
    });


angular.module('luckynumbersApp').controller('IntroController', function ($location, AuthenticationSharedService) {
        if ($scope.authenticated) {
            $location.path('/mprogramaciones').replace();
        }
    });
