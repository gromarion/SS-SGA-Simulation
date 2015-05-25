'use strict';

angular.module('sgaSimulator.services')

    .factory('myAPI', function ($resource) {
        return $resource('myAPIUrl');
    });
