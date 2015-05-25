'use strict';

angular.module('sgaSimulator.controllers', []);
angular.module('sgaSimulator.services', []);
angular.module('sgaSimulator.directives', []);

angular.module('sgaSimulator', [
  'ui.router',
  'ngResource',
  'sgaSimulator.controllers',
  'sgaSimulator.services',
  'sgaSimulator.directives',
  'ui.bootstrap'
]);

angular.module('sgaSimulator').config(function ($urlRouterProvider, $stateProvider) {
  $urlRouterProvider.when('', '/home');

  $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: 'home/home.html',
            controller: 'HomeCtrl'
          });
})

.run(function() {

});
