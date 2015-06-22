'use strict';

angular.module('sgaSimulator.controllers')

    .controller('ConfigurationCtrl', ['$scope', '$modalInstance', 'simulationService', function($scope, $modalInstance, simulationService) {

        $scope.formatCriteria = function(criteria) {
            switch(criteria) {
                case 'older': return 'Alumnos más jóvenes último';
                case 'younger': return 'Alumnos más jóvenes primero';
                case 'onschedule': return 'Alumnos al día primero';
                case 'delayed': return 'Alumnos al día último';
            }
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        simulationService.getConfiguration().then(function(config) {
            $scope.config = config;
        });

        $scope.submit = function() {
            simulationService.setConfiguration($scope.config);
            $scope.cancel();
        };
    }]);
