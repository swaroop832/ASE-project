angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
    
  

      .state('menu.home', {
    url: '/page1',
    views: {
      'side-menu21': {
        templateUrl: 'templates/home.html',
        controller: 'homeCtrl'
      }
    }
  })

  .state('menu.remind', {
    url: '/Remind',
    views: {
      'side-menu21': {
        templateUrl: 'templates/remind.html',
        controller: 'remindCtrl'
      }
    }
  })

  .state('menu.alarm', {
    url: '/Alarm',
    views: {
      'side-menu21': {
        templateUrl: 'templates/alarm.html',
        controller: 'alarm-contro'
      }
    }
  })

  .state('menu.notes', {
    url: '/notes',
    views: {
      'side-menu21': {
        templateUrl: 'templates/notes.html',
        controller: 'notesCtrl'
      }
    }
  })

  .state('menu.about', {
    url: '/about',
    views: {
      'side-menu21': {
        templateUrl: 'templates/about.html',
        controller: 'aboutCtrl'
      }
    }
  })

  .state('menu', {
    url: '/side-menu21',
    templateUrl: 'templates/menu.html',
    controller: 'menuCtrl'
  })

  .state('login', {
    url: '/login',
    templateUrl: 'templates/login.html',
    controller: 'loginCtrl'
  })

  .state('signup', {
    url: '/signup',
    templateUrl: 'templates/signup.html',
    controller: 'signupCtrl'
  })

$urlRouterProvider.otherwise('/login')

  

});