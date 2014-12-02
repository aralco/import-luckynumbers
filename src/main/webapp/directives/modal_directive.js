angular.module('luckynumbersApp')
    .directive('modal', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/directive_templates/modal_template.html',
            replace: true,
            transclude: true,
            scope: {
                open: '='
            },
            link: function ($scope, element, attrs) {
                $scope.header = attrs.header;
                $scope.content = attrs.content;

                $scope.$watch('open', function (value) {
                    if (value == true) {
                        element.modal('show');
                    } else if (value == false) {
                        element.modal('hide');
                    }
                });

                element.on('hidden.bs.modal', function (e) {
                    $scope.$apply(function () {
                        $scope.open = false;
                    })
                });

            }
        }
    });