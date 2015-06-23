'use strict';

angular.module('sgaSimulator.controllers')

.controller('HomeCtrl', ['$scope', '$state', '$timeout', '$interval', '$modal', 'simulationService', function($scope, $state, $timeout, $interval, $modal, simulationService) {

    var maxPx = jQuery(document.getElementsByClassName('students-graph')[0]).height(),
        POLLER_INTERVAL_MILLIS = 1000,
        studentsUnmatriculatedGraph,
        studentsMatriculatedGraph;

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
                    $scope.updateDetailsStudentsGraph();
                    if (simulationService.simulationHasFinished()) {
                        $interval.cancel(promise);
                    }
                });
            }, POLLER_INTERVAL_MILLIS);
        });
    };

    $scope.openConfigModal = function() {
        $modal.open({
            templateUrl: 'configuration/configuration.html',
            controller: 'ConfigurationCtrl'
        });
    };

    $scope.updateDetailsStudentsGraph = function() {
        if (!studentsMatriculatedGraph) {
            createStudentsMatriculatedGraph();
        }

        if (!studentsUnmatriculatedGraph) {
            createStudentsUnmatriculatedGraph();
        }

        studentsMatriculatedGraph.highcharts().get('matriculated').setData(formatSerie($scope.stats.matriculatedAlumnsByPeningCourses));
        studentsUnmatriculatedGraph.highcharts().get('unmatriculated').setData(formatSerie($scope.stats.unmatriculatedAlumnsByPendingCourses));
    };

    function createStudentsUnmatriculatedGraph() {
        studentsUnmatriculatedGraph = $('#studentsUnmatriculatedGraph').highcharts({
            chart: {
                backgroundColor: '#FAFAFA'
            },
            title: {
                text: 'Alumnos sin matricular'
            },
            xAxis: {
                title: {
                    text: 'Cant. de materias pendientes'
                }
            },
            yAxis: {
                title: {
                    text: 'Cant. de alumnos'
                },
                min : 0,
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            series: [{
                showInLegend: false,
                id: 'unmatriculated',
                data: []
            }]
        });
    }

    function createStudentsMatriculatedGraph() {
        studentsMatriculatedGraph = $('#studentsMatriculatedGraph').highcharts({
            chart: {
                backgroundColor: '#FAFAFA'
            },
            title: {
                text: 'Alumnos matriculados'
            },
            xAxis: {
                title: {
                    text: 'Cant. de materias pendientes'
                }
            },
            yAxis: {
                title: {
                    text: 'Cant. de alumnos'
                },
                min : 0,
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            series: [{
                showInLegend: false,
                id: 'matriculated',
                data: []
            }]
        });
    }

    function formatSerie(serie) {
        return _.map(serie, function(students, credits) { return [credits, students]; });
    }

    $timeout(function() {
        $scope.updateMatriculationGraph();
    }, 500);
}]);
