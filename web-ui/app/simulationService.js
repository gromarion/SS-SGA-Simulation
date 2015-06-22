'use strict';

angular.module('sgaSimulator.services')

    .factory('simulationService', ['$q', 'simulationStarter', 'simulationStats', 'simulationConfig', function ($q, simulationStarter, simulationStats, simulationConfig) {

        var DAYS_AMOUNT = 7,
            stats = {
                totalStudents: 0,
                alumnsMatriculated: 0,
                currentDay: 0,
                currentHour: 0,
                totalsServerTimeouts: 0,
                days: []
            };

        function initStats() {
            for(var i = 0; i < DAYS_AMOUNT; i++) {
                stats.days.push({
                    dayNumber: i,
                    matriculationSuccess: 0,
                    matriculationConflict: 0,
                    matriculationFail: 0
                });
            }
        }

        function updateStats(newStats) {
            var day = _.findWhere(stats.days, {dayNumber: newStats.dayOfWeek});
            day.matriculationSuccess += (newStats.alumnsMatriculated - stats.alumnsMatriculated);

            stats.totalStudents = newStats.totalStudents;
            stats.alumnsMatriculated = newStats.alumnsMatriculated;
            stats.currentDay = newStats.dayOfWeek;
            stats.currentHour = newStats.hourOfDay;
            stats.totalsServerTimeouts = newStats.totalsServerTimeouts;
            stats.matriculatedAlumnsByPeningCourses = newStats.matriculatedAlumnsByPeningCourses;
            stats.unmatriculatedAlumnsByPendingCourses = newStats.unmatriculatedAlumnsByPendingCourses;

            return stats;
        }

        return {
            startSimulation: function() {
                return simulationStarter.save().$promise;
            },

            fetchStats: function() {
                var deferred = $q.defer();
                simulationStats.get().$promise.then(function(newStats) {
                    deferred.resolve(updateStats(newStats));
                });
                return deferred.promise;
            },

            getStats: function() {
                return stats;
            },

            simulationHasFinished: function() {
                return stats.totalStudents > 0 && stats.alumnsMatriculated == stats.totalStudents;
            },

            getConfiguration: function() {
                return simulationConfig.get().$promise;
            },

            setConfiguration: function(config) {
                return simulationConfig.save(config).$promise;
            },

            initStats: initStats
        };
    }]);