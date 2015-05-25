'use strict';

angular.module('sgaSimulator.controllers')

.controller('HomeCtrl', ['$scope', '$state', function($scope, $state) {
    $scope.snapshot = [
      {
        title: 'LUN',
        matriculationSuccess: 8,
        matriculationConflict: 1,
        matriculationFail: 2
      },
      {
        title: 'MAR',
        matriculationSuccess: 5,
        matriculationConflict: 3,
        matriculationFail: 0
      },
      {
        title: 'MIE',
        matriculationSuccess: 1,
        matriculationConflict: 3,
        matriculationFail: 2
      },
      {
        title: 'JUE',
        matriculationSuccess: 4,
        matriculationConflict: 3,
        matriculationFail: 2
      },
      {
        title: 'VIE',
        matriculationSuccess: 4,
        matriculationConflict: 3,
        matriculationFail: 2
      },
      {
        title: 'SAB',
        matriculationSuccess: 4,
        matriculationConflict: 3,
        matriculationFail: 2
      },
      {
        title: 'DOM',
        matriculationSuccess: 4,
        matriculationConflict: 3,
        matriculationFail: 2
      }
    ];

    $scope.selected = 'LUN';
}]);
