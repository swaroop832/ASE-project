// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
var lab7 = angular.module('app', ['ionic', 'app.controllers', 'app.routes', 'app.directives','app.services','firebase','ngCordova'])


lab7.config(function($ionicConfigProvider, $sceDelegateProvider){
  

  $sceDelegateProvider.resourceUrlWhitelist([ 'self','*://www.youtube.com/**', '*://player.vimeo.com/video/**']);

})

lab7.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }
  });
})

/*
  This directive is used to disable the "drag to open" functionality of the Side-Menu
  when you are dragging a Slider component.
*/
lab7.directive('disableSideMenuDrag', ['$ionicSideMenuDelegate', '$rootScope', function($ionicSideMenuDelegate, $rootScope) {
    return {
        restrict: "A",  
        controller: ['$scope', '$element', '$attrs', function ($scope, $element, $attrs) {

            function stopDrag(){
              $ionicSideMenuDelegate.canDragContent(false);
            }

            function allowDrag(){
              $ionicSideMenuDelegate.canDragContent(true);
            }

            $rootScope.$on('$ionicSlides.slideChangeEnd', allowDrag);
            $element.on('touchstart', stopDrag);
            $element.on('touchend', allowDrag);
            $element.on('mousedown', stopDrag);
            $element.on('mouseup', allowDrag);

        }]
    };
}])

/*
  This directive is used to open regular and dynamic href links inside of inappbrowser.
*/
lab7.directive('hrefInappbrowser', function() {
  return {
    restrict: 'A',
    replace: false,
    transclude: false,
    link: function(scope, element, attrs) {
      var href = attrs['hrefInappbrowser'];

      attrs.$observe('hrefInappbrowser', function(val){
        href = val;
      });
      
      element.bind('click', function (event) {

        window.open(href, '_system', 'location=yes');

        event.preventDefault();
        event.stopPropagation();

      });
    }
  };
});

lab7.controller('alarm-contro',function($scope,$ionicModal,$filter,$interval,$ionicPopup){

 $scope.alarms = [];
 $scope.alarm = {};

  $ionicModal.fromTemplateUrl('add-alarm.html',function(modal){
    $scope.setalarm = modal;
  },{
    scope : $scope,
    animation : 'slide-in-up'
  });

  $scope.newalarm = function (){
    $scope.setalarm.show();
  };

  $scope.closesetalarm = function(){
    $scope.setalarm.hide();
  };

  $scope.createalarm = function (alarm1) {
    var time = alarm1.hour+":"+alarm1.min+" "+alarm1.pos;
     $scope.alarms.push({
       time  : time , on : true
     });
     localStorage.setItem('alarms', JSON.stringify($scope.alarms));
     $scope.alarm = {};
     $scope.setalarm.hide();
  };
  //localStorage.clear();
  $scope.getalarms = function (){
    $scope.alarms = (localStorage.getItem('alarms')!==null) ? JSON.parse(localStorage.getItem('alarms')) : [];
    $scope.Time = $filter('date')(new Date(), 'hh:mm a');
    $interval(function() {
       $scope.alarmcheck();
   }, 60000);

   $interval(function() {
      $scope.Time = $filter('date')(new Date(), 'hh:mm a');
   }, 60000);
  };

  $scope.offalarm = function(index){
    if (index !== -1) {
      if($scope.alarms[index].on){
        $scope.alarms[index].on = true;}
      else{
        $scope.alarms[index].on = false;}
    }
    localStorage.setItem('alarms', JSON.stringify($scope.alarms));
  };

  $scope.removealarm = function(index){
    $scope.alarms.splice(index,1);
    localStorage.setItem('alarms', JSON.stringify($scope.alarms));
  };

 $scope.alarmcheck = function (){
       var input = $scope.alarms, time = $scope.Time;
       var i=0, len=input.length;
       for (; i<len; i++) {
         if (input[i].time.trim() == time.trim() && input[i].on) {
           $ionicPopup.alert({
              title: 'Alarm',
              template: 'Wake up'
            });
         }
     }
   };

})

lab7.controller("FirebaseController", function($scope, $state, $firebaseAuth) {

    var fbAuth = $firebaseAuth();

    $scope.login = function(username, password) {
        fbAuth.$signInWithEmailAndPassword(username,password).then(function(authData) {
            $state.go("menu.home");
            alert("you have logged in successfully");
        }).catch(function(error) {
            console.error("ERROR: " + error);
            alert("please enter correct details or please signup ");
        });
    }

    $scope.register = function(username, password) {
        fbAuth.$createUserWithEmailAndPassword(username,password).then(function(userData) {
            return fbAuth.$signInWithEmailAndPassword(username,
                password);
        }).then(function(authData) {
            $state.go("login");
            alert("you have signedup successfully, please enter details to login");
        }).catch(function(error) {
            console.error("ERROR: " + error);
            alert("there is error in signingup, please make sure your password has 6 characters and enter valid email address");
        });
    }

});

lab7.factory('ClockSrv', function($interval) {
  'use strict';
  var service = {
    clock: addClock,
    cancelClock: removeClock
  };

  var clockElts = [];
  var clockTimer = null;
  var cpt = 0;

  function addClock(fn) {
    var elt = {
      id: cpt++,
      fn: fn
    };
    clockElts.push(elt);
    if (clockElts.length === 1) {
      startClock();
    }
    return elt.id;
  }

  function removeClock(id) {
    for (var i in clockElts) {
      if (clockElts[i].id === id) {
        clockElts.splice(i, 1);
      }
    }
    if (clockElts.length === 0) {
      stopClock();
    }
  }

  function startClock() {
    if (clockTimer === null) {
      clockTimer = $interval(function() {
        for (var i in clockElts) {
          clockElts[i].fn();
        }
      }, 1000);
    }
  }

  function stopClock() {
    if (clockTimer !== null) {
      $interval.cancel(clockTimer);
      clockTimer = null;
    }
  }

  return service;
})

lab7.run(function($rootScope, $filter, ClockSrv) {
  ClockSrv.clock(function() {
    // console.log($filter('date')(Date.now(), 'yyyy-MM-dd HH:mm:ss')); 
    $rootScope.clock = $filter('date')(Date.now(), 'MM/dd/yyyy  ');
      $rootScope.clock1 = $filter('date')(Date.now(), 'HH:mm:ss'); 
    
  });
                 
})

lab7.controller('menuCtrl', function($scope, $timeout, $filter, ClockSrv) {
  $scope.myTitle = 'Clock';
  // $scope.clock = ClockSrv.clock();

  /*$scope.data = {
    tgl: "Memuat jam..."
  }
  var tickInterval = 1000; //ms

  var tick = function() {
    $scope.data.tgl = $filter('date')(Date.now(), 'yyyy-MM-dd HH:mm:ss'); // get the current time
    $timeout(tick, tickInterval); // reset the timer
  }

  // Start the timer
  $timeout(tick, tickInterval);*/

});



