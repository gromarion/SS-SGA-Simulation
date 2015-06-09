'use strict';

angular.module('sgaSimulator.controllers')

.controller('HomeCtrl', ['$scope', '$state', '$timeout', '$interval', 'simulationService', function($scope, $state, $timeout, $interval, simulationService) {

    var maxPx = jQuery(document.getElementsByClassName('students-graph')[0]).height(),
        POLLER_INTERVAL_MILLIS = 1000;

    simulationService.initStats();
    $scope.stats = simulationService.getStats();

    function calcHeight(studentsValue, maxStudents) {
        return studentsValue * maxPx / maxStudents;
    }

    function updateElemHeight(section, maxStudents) {
        section.elem.style.height = calcHeight(section.value, maxStudents) + 'px';
    }

    $scope.getDayTitle = function(dayNumber) {
        switch (dayNumber) {
            case 0: return 'LUN';
            case 1: return 'MAR';
            case 2: return 'MIE';
            case 3: return 'JUE';
            case 4: return 'VIE';
            case 5: return 'SAB';
            case 6: return 'DOM';
        }
    };

    $scope.updateMatriculationGraph = function() {
        $scope.stats.days.forEach(function(day) {
            var barSections = [
                {
                    elem: document.getElementById('#' + $scope.getDayTitle(day.dayNumber) + '-success'),
                    value: day.matriculationSuccess
                },{
                    elem: document.getElementById('#' + $scope.getDayTitle(day.dayNumber) + '-fail'),
                    value: day.matriculationFail
                },{
                    elem: document.getElementById('#' + $scope.getDayTitle(day.dayNumber) + '-conflict'),
                    value: day.matriculationConflict
                }
            ];

            _.map(barSections, function(section) {
                updateElemHeight(section, $scope.stats.totalStudents);
            });
        });
    };

    $scope.startSimulation = function() {
        simulationService.startSimulation().then(function() {
            var promise = $interval(function() {
                simulationService.fetchStats().then(function() {
                    $scope.stats = simulationService.getStats();
                    $scope.updateMatriculationGraph();
                    if (simulationService.simulationHasFinished()) {
                        $interval.cancel(promise);
                    }
                });
            }, POLLER_INTERVAL_MILLIS);
        });
    };

    $timeout(function() {
        $scope.updateMatriculationGraph();
    }, 500);
}]);
