'use strict';

var SIMULATION_API_BASE_URI = 'http://localhost:8080/';

angular.module('sgaSimulator.services')

    .factory('simulationStarter', function ($resource) {
        return $resource(SIMULATION_API_BASE_URI + 'start');
    })

    .factory('simulationStats', function ($resource) {
        return $resource(SIMULATION_API_BASE_URI + 'stats');
    })

    .factory('simulationConfig', function ($resource) {
        return $resource(SIMULATION_API_BASE_URI + 'config');
    });
