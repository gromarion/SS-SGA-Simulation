'use strict';

angular.module('sgaSimulator.controllers')

.controller('HomeCtrl', ['$scope', '$state', '$timeout', function($scope, $state, $timeout) {

    var maxPx = jQuery(document.getElementsByClassName('students-graph')[0]).height();

    function calcHeight(studentsValue, maxStudents) {
        return studentsValue * maxPx / maxStudents;
    }

    function updateElemHeight(section, maxStudents) {
      debugger
        section.elem.style.height = calcHeight(section.value, maxStudents) + 'px';
    };

    $scope.updateMatriculationGraph = function(snapshot) {
        snapshot.days.forEach(function(day) {
            var barSections = [
              {
                elem: document.getElementById('#' + day.title + '-success'),
                value: day.matriculationSuccess
              },{
                elem: document.getElementById('#' + day.title + '-fail'),
                value: day.matriculationFail
              },{
                elem: document.getElementById('#' + day.title + '-conflict'),
                value: day.matriculationConflict
              }
            ];

            _.map(barSections, function(section) {
              updateElemHeight(section, snapshot.maxStudents);
            });
        });
    };

    $scope.snapshot = {
      maxStudents: 10,
      days: [
      {
        title: 'MON',
        matriculationSuccess: 1,
        matriculationConflict: 4,
        matriculationFail: 2
      },
      {
        title: 'TUE',
        matriculationSuccess: 5,
        matriculationConflict: 3,
        matriculationFail: 0
      },
      {
        title: 'WED',
        matriculationSuccess: 1,
        matriculationConflict: 3,
        matriculationFail: 2
      },
      {
        title: 'THU',
        matriculationSuccess: 3,
        matriculationConflict: 3,
        matriculationFail: 0
      },
      {
        title: 'FRI',
        matriculationSuccess: 1,
        matriculationConflict: 1,
        matriculationFail: 0
      },
      {
        title: 'SAT',
        matriculationSuccess: 0,
        matriculationConflict: 0,
        matriculationFail: 0
      },
      {
        title: 'SUN',
        matriculationSuccess: 0,
        matriculationConflict: 0,
        matriculationFail: 0
      }]
    };

    $scope.selected = 'FRI';

    $timeout(function() {
      $scope.updateMatriculationGraph($scope.snapshot);
    }, 2000);
}]);
