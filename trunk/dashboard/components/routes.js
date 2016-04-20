'use strict';

angular.module("common.config", [])
.constant("ROOT", "http://localhost:30505/api/v1/")
.constant("API_IMAGE_UPLOAD", "http://ec2-54-213-249-95.us-west-2.compute.amazonaws.com:30505/api/v1/images/");
angular.module('app.controllers',["app.security.service", "app.security.login", "app.security.signup",
    "app.admin.orders", "app.admin.restaurant", "app.admin.user", "app.admin.plan",
    "app.admin.user.services", "app.admin.user.manage", 
     "app.admin.restaurant.manage.menu.service", 
   "app.admin.restaurant.manage.menu.categories", 
   "app.admin.restaurant.manage.menu.items",
 "app.admin.restaurant.manage.menu.menus",
 "app.admin.restaurant.manage.menu.sizes", 
 "app.admin.restaurant.manage.menu.addon", 
 "app.admin.restaurant.manage.menu.modifiers","app.admin.orders.services"]);


angular
  .module('app', [
    'ui.bootstrap',
    'ui.tree',
    'ui.select2',
    'theme.services',
    'theme.directives',
    'theme.google_maps','angular-toArrayFilter',
  /*  'ngMask',*/
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    // 'ngAnimate',
    'angular-loading-bar',
    'angular-ladda',
  'angularPayments',
   'angularMoment',
    
    'daterangepicker',
    'ngTable',
    'ui.router',
    'ui.select',

    'common.service',
    'common.directives',
    'common.filters',
    'common.config',
    
    'app.owner',
    'app.admin',
   "app.owner",'app.controllers','dndLists'


    
  ])
  .value('TOKEN', "sk_test_xoxQuwIkxhIXgVLNOJ8E5Kpl")
  .constant('TABLE_OPTIONS', {
    page: 1, 
    count: 10, 
    counts: [10, 25, 50, 100], 
    defaultSort: 'asc'
  })
  // The center be used for initilaize the center of the zone map
  .constant('CENTER', {
    lat: 10.804276,
    lng: 106.637399
  })
  // Config loading bar
  .config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.latencyThreshold = 100; // at least 100 ms
  }])
  // Config button loading
  .config(function (laddaProvider) {
    laddaProvider.setOption({
      style: 'slide-right'
    });
  })
  .config(["flowFactoryProvider", "API_IMAGE_UPLOAD", function(flowFactoryProvider, API_IMAGE_UPLOAD) {
    flowFactoryProvider.defaults = {
        target: API_IMAGE_UPLOAD + "upload"
    }
}])
  .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $sceDelegateProvider) {

     $httpProvider.defaults.headers.common = {};
  $httpProvider.defaults.headers.post = {};
  $httpProvider.defaults.headers.put = {};
  $httpProvider.defaults.headers.patch = {};

   
 $httpProvider.defaults.useXDomain = true;
delete $httpProvider.defaults.headers.common['X-Requested-With'];

    $httpProvider.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
    $httpProvider.defaults.headers.post["Content-Type"] = "application/json";
    $httpProvider.defaults.headers.put["Content-Type"] = "application/json";



      $urlRouterProvider.otherwise('/');
    $urlRouterProvider.when("/", "/login");
           
    /*  $httpProvider.defaults.useXDomain = true;

        delete $httpProvider.defaults.headers.common['X-Requested-With'];*/

    $stateProvider
      .state("app", {
        abtract: true,  
        url: "/",
        templateUrl: "scripts/common/views/layout-blank.html",
        controller:"MainController"
      })

      .state("app.security", {
        abtract: !0,
        url: "",
        templateUrl: "scripts/common/security/views/security-index.html"
    }).state("app.security.login", {
        url: "login",
        templateUrl: "scripts/common/security/views/login.html",
        controller: "LoginController"
    }).state("app.security.signup", {
        url: "signup",
        templateUrl: "scripts/common/security/views/signup.html",
        controller: "SignupController"
    }).state("app.admin", {
        url: "admin",
        abtract: !0,
        templateUrl: "scripts/common/views/layout-admin.html",
        controller: "AdminController"
    })

    .state("app.admin.restaurant", {
        abstract: !0,
        url: "/restaurant",
        templateUrl: "scripts/admin/restaurant/views/restaurant.html"
    }).state("app.admin.restaurant.list", {
        url: "/",
        templateUrl: "scripts/admin/restaurant/views/restaurant-list.html",
        controller: "RestaurantController"
    }).state("app.admin.restaurant.manage", {
        url: "/manage/{restaurantId}",
        templateUrl: "scripts/admin/restaurant/manage/views/manage.html",
        controller: "RestaurantManageController"
    })

    .state("app.admin.user", {
        abtract: !0,
        url: "/user",
        templateUrl: "scripts/admin/user/views/user.html"
    }).state("app.admin.user.list", {
        url: "/",
        templateUrl: "scripts/admin/user/views/user-list.html",
        controller: "UserController"
    }).state("app.admin.user.manage", {
        url: "/manage/{userId}",
        templateUrl: "scripts/admin/user/manage/views/manage.html",
        controller: "UserManageController"
    })


    .state("app.admin.restaurant.manage.menu", {
        abtract: !0,
        url: "",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/menus-index.html"
    }).state("app.admin.restaurant.manage.menu.items", {
        url: "/items",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/items.html",
        controller: "AdminItemsController"
    }).state("app.admin.restaurant.manage.menu.categories", {
        url: "/categories",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/categories.html",
        controller: "AdminCategoriesController"
    }).state("app.admin.restaurant.manage.menu.menus", {
        url: "/menus",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/menus.html",
        controller: "AdminMenusController"
    }).state("app.admin.restaurant.manage.menu.menus.list", {
        url: "/",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/menus-list.html"

    }).state("app.admin.restaurant.manage.menu.menus.create", {
        url: "/add",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/menus-create.html"
    }).state("app.admin.restaurant.manage.menu.menus.edit", {
        url: "/edit",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/menus-create.html"
    }).state("app.admin.restaurant.manage.menu.modifiers", {
        url: "/modifiers",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/modifiers.html",
        controller: "AdminModifiersController"
    }).state("app.admin.restaurant.manage.menu.sizes", {
        url: "/sizes",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/sizes.html",
        controller: "AdminSizesController"
    }).state("app.admin.restaurant.manage.menu.addon", {
        url: "/addon",
        templateUrl: "scripts/admin/restaurant/manage/menu/views/addon.html",
        controller: "AdminAddonController"
    })
    
    .state("app.admin.restaurant.manage.restaurant", {
        abtract: !0,
        url: "",
        templateUrl: "scripts/admin/restaurant/manage/restaurant/views/restaurant-index.html"
    }).state("app.admin.restaurant.manage.restaurant.zones", {
        url: "/zones",
        templateUrl: "scripts/admin/restaurant/manage/restaurant/views/zone.html",
        controller: "AdminZoneController"
    }).state("app.admin.restaurant.manage.restaurant.notifications", {
        url: "/notifications",
        templateUrl: "scripts/admin/restaurant/manage/restaurant/views/notification.html"
    }).state("app.admin.restaurant.manage.restaurant.ordertype", {
        url: "/ordertype",
        templateUrl: "scripts/admin/restaurant/manage/restaurant/views/order.html",
        controller: "AdminOrderTypeController"
    }).state("app.admin.restaurant.manage.restaurant.info", {
        url: "/info",
        templateUrl: "scripts/admin/restaurant/manage/restaurant/views/info.html",
        controller: "AdminInfoController"
    }).state("app.admin.restaurant.manage.restaurant.taxes", {
        url: "/taxes",
        templateUrl: "scripts/admin/restaurant/manage/restaurant/views/taxes.html",
        controller: "AdminTaxesController"
    }).state("app.admin.restaurant.manage.restaurant.subscriptions", {
        url: "/subscriptions",
        templateUrl: "scripts/admin/restaurant/manage/restaurant/views/subscriptions.html",
        controller: "AdminSubscriptionsController"
    })

    .state("app.admin.plan", {
        abtract: !0,
        url: "/plan",
        templateUrl: "scripts/admin/plan/views/plan.html"
    }).state("app.admin.plan.list", {
        url: "/",
        templateUrl: "scripts/admin/plan/views/plan-list.html",
        controller: "AdminPlanController"
    })

    .state("app.admin.orders", {
        url: "/orders",
        templateUrl: "scripts/admin/order/views/order.html",
        controller: "AdminOrderController"
    })

    .state("app.admin.restaurant.manage.orders", {
        url: "/orders",
        templateUrl: "scripts/admin/restaurant/manage/order/views/order.html",
        controller: "AdminOrdersController"
    })

    .state("app.owner", {
        url: "owner",
        abtract: !0,
        templateUrl: "scripts/common/views/layout-owner.html",
        controller: "OwnerController"
    })
    
    .state("app.owner.menu", {
        abtract: !0,
        url: "/menu",
        templateUrl: "scripts/owner/menu/views/menus-index.html",
        controller: "MenuController"
    }).state("app.owner.menu.items", {
        url: "/items",
        templateUrl: "scripts/owner/menu/views/items.html",
        controller: "ItemsController"
    }).state("app.owner.menu.categories", {
        url: "/categories",
        templateUrl: "scripts/owner/menu/views/categories.html",
        controller: "CategoriesController"
    }).state("app.owner.menu.menus", {
        url: "/menus",
        templateUrl: "scripts/owner/menu/views/menus.html",
        controller: "MenusController"
    }).state("app.owner.menu.menus.list", {
        url: "/",
        templateUrl: "scripts/owner/menu/views/menus-list.html"
    }).state("app.owner.menu.menus.create", {
        url: "/add",
        templateUrl: "scripts/owner/menu/views/menus-create.html"
    }).state("app.owner.menu.menus.edit", {
        url: "/edit",
        templateUrl: "scripts/owner/menu/views/menus-create.html"
    }).state("app.owner.menu.modifiers", {
        url: "/modifiers",
        templateUrl: "scripts/owner/menu/views/modifiers.html",
        controller: "ModifiersController"
    }).state("app.owner.menu.sizes", {
        url: "/sizes",
        templateUrl: "scripts/owner/menu/views/sizes.html",
        controller: "SizesController"
    }).state("app.owner.menu.addon", {
        url: "/addon",
        templateUrl: "scripts/owner/menu/views/addon.html",
        controller: "AddonController"
    }).state("app.owner.orders", {
        url: "/orders",
        templateUrl: "scripts/owner/order/views/order.html",
        controller: "OrdersController"
    })
    // Interceptor
     $httpProvider.interceptors.push("interceptor");

  })
  .run(function ($http, $cookieStore, $rootScope, $window){
    $window.Stripe.setPublishableKey('pk_test_PI6Y4sKtZu5c1IncCLW7jSv6');
    // Set authorization by the token
    $http.defaults.headers.common.Authorization = "Bearer " + $cookieStore.get("authToken");
    // Set restaurant ID use for access API
    $rootScope.restaurantID = $cookieStore.get("authRestaurantID");
    // Set the first name to show on page's header
    $rootScope.globalUser = $cookieStore.get("authUsername");
    // Set the restaurant's location on map
    $rootScope.globalRestaurantLocation = $cookieStore.get("restaurantLocation");;
    
  })
  .controller('MainController', 
    ['$scope', '$global', '$timeout', 'progressLoader', '$location', 
    function ($scope, $global, $timeout, progressLoader, $location) {

    // init alertify global defaults
    alertify.defaults.movable = false;
    alertify.defaults.glossary.title = "<i class='fa fa-warning'></i> Warning";

  }]);
//angular.module("app.admin", ["app.admin.orders", "app.admin.restaurant", "app.admin.user", "app.admin.plan"]);
angular.module("app.admin.orders", ["app.admin.orders.services"])


angular.module("app.owner.menu", ["app.owner.menu.service", "app.owner.menu.categories", "app.owner.menu.items", "app.owner.menu.menus", "app.owner.menu.modifiers", "app.owner.menu.sizes", "app.owner.menu.addon"]).config(function($stateProvider) {
    $stateProvider.state("app.owner.menu", {
        abtract: !0,
        url: "/menu",
        templateUrl: "scripts/owner/menu/views/menus-index.html",
        controller: "MenuController"
    }).state("app.owner.menu.items", {
        url: "/items",
        templateUrl: "scripts/owner/menu/views/items.html",
        controller: "ItemsController"
    }).state("app.owner.menu.categories", {
        url: "/categories",
        templateUrl: "scripts/owner/menu/views/categories.html",
        controller: "CategoriesController"
    }).state("app.owner.menu.menus", {
        url: "/menus",
        templateUrl: "scripts/owner/menu/views/menus.html",
        controller: "MenusController"
    }).state("app.owner.menu.menus.list", {
        url: "/",
        templateUrl: "scripts/owner/menu/views/menus-list.html"
    }).state("app.owner.menu.menus.create", {
        url: "/add",
        templateUrl: "scripts/owner/menu/views/menus-create.html"
    }).state("app.owner.menu.menus.edit", {
        url: "/edit",
        templateUrl: "scripts/owner/menu/views/menus-create.html"
    }).state("app.owner.menu.modifiers", {
        url: "/modifiers",
        templateUrl: "scripts/owner/menu/views/modifiers.html",
        controller: "ModifiersController"
    }).state("app.owner.menu.sizes", {
        url: "/sizes",
        templateUrl: "scripts/owner/menu/views/sizes.html",
        controller: "SizesController"
    }).state("app.owner.menu.addon", {
        url: "/addon",
        templateUrl: "scripts/owner/menu/views/addon.html",
        controller: "AddonController"
    })
});

angular.module("app.owner.restaurant", ["app.owner.restaurant.service", 
    "app.owner.restaurant.zones", "app.owner.restaurant.notifications", 
    "app.owner.restaurant.ordertype", "app.owner.restaurant.info", 
    "app.owner.restaurant.subscriptions"]).config(function($stateProvider) {
    $stateProvider.state("app.owner.restaurant", {
        abtract: !0,
        url: "/restaurant",
        templateUrl: "scripts/owner/restaurant/views/restaurant-index.html"
    }).state("app.owner.restaurant.zones", {
        url: "/zones",
        templateUrl: "scripts/owner/restaurant/views/zone.html",
        controller: "ZoneController"
    }).state("app.owner.restaurant.notifications", {
        url: "/notifications",
        templateUrl: "scripts/owner/restaurant/views/notification.html",
        controller: "NotificationsController"
    }).state("app.owner.restaurant.ordertype", {
        url: "/ordertype",
        templateUrl: "scripts/owner/restaurant/views/order.html",
        controller: "OrderTypeController"
    }).state("app.owner.restaurant.info", {
        parent: "app.owner.restaurant",
        url: "/info",
        templateUrl: "scripts/owner/restaurant/views/info.html",
        controller: "InfoController"
    }).state("app.owner.restaurant.subscriptions", {
        url: "/subscriptions",
        templateUrl: "scripts/owner/restaurant/views/subscriptions.html",
        controller: "SubscriptionsController"
    })
});

angular.module("app.owner.orders", ["common.directives", "app.owner.orders.services"])
.config(function($stateProvider) {
    $stateProvider.state("app.owner.orders", {
        url: "/orders",
        templateUrl: "scripts/owner/order/views/order.html",
        controller: "OrdersController"
    })
});

angular.module("app.owner", ["app.owner.menu", "app.owner.restaurant", "app.owner.orders"]).config(function($stateProvider) {
    $stateProvider.state("app.owner", {
        url: "owner",
        abtract: !0,
        templateUrl: "scripts/common/views/layout-owner.html",
        controller: "OwnerController"
    })
});

angular.module("app.admin.restaurant.manage.menu", ["app.admin.restaurant.manage.menu.service", "app.admin.restaurant.manage.menu.categories", "app.admin.restaurant.manage.menu.items",
 "app.admin.restaurant.manage.menu.menus",
 "app.admin.restaurant.manage.menu.sizes", "app.admin.restaurant.manage.menu.addon", 
 "app.admin.restaurant.manage.menu.modifiers"]);

/*.config(["flowFactoryProvider", "API_IMAGE_UPLOAD", function(flowFactoryProvider, API_IMAGE_UPLOAD) {
    flowFactoryProvider.defaults = {
        target: API_IMAGE_UPLOAD + "upload"
    }
}]);*/

angular.module("app.admin.restaurant.manage.restaurant", ["app.admin.restaurant.manage.restaurant.service",
 "app.admin.restaurant.manage.restaurant.zones", "app.admin.restaurant.manage.restaurant.info",
  "app.admin.restaurant.manage.restaurant.taxes", "app.admin.restaurant.manage.restaurant.subscriptions",
   "app.admin.restaurant.manage.restaurant.ordertype"]);

angular.module("app.admin.restaurant.manage.orders", ["common.directives",
 "app.admin.restaurant.manage.orders.services"]).config(function($stateProvider) {
    $stateProvider.state("app.admin.restaurant.manage.orders", {
        url: "/orders",
        templateUrl: "scripts/admin/restaurant/manage/order/views/order.html",
        controller: "AdminOrdersController"
    })
});

 angular.module("app.admin.restaurant.manage", ["app.admin.restaurant.manage.menu", 
    "app.admin.restaurant.manage.restaurant", "app.admin.restaurant.manage.orders"])

angular.module("app.admin.user.manage", []).config(function($stateProvider) {
    $stateProvider.state("app.admin.user.manage", {
        url: "/manage/{userId}",
        templateUrl: "scripts/admin/user/manage/views/manage.html",
        controller: "UserManageController"
    })
});

/*angular.module("app.admin.user", ["app.admin.user.services", "app.admin.user.manage"]).config(function($stateProvider) {
    $stateProvider.state("app.admin.user", {
        abtract: !0,
        url: "/user",
        templateUrl: "scripts/admin/user/views/user.html"
    }).state("app.admin.user.list", {
        url: "/",
        templateUrl: "scripts/admin/user/views/user-list.html",
        controller: "UserController"
    })
});*/

angular.module("app.admin.plan", ["app.admin.plan.service", "app.admin.plan.list"]);

/*angular.module("app.admin", ["app.admin.orders", "app.admin.restaurant", "app.admin.user", "app.admin.plan"]);
angular.module("app.admin.orders", ["app.admin.orders.services"]);*//*.config(function($stateProvider) {
    $stateProvider.state("app.admin.orders", {
        url: "/orders",
        templateUrl: "scripts/admin/order/views/order.html",
        controller: "AdminOrdersController"
    })
});*/

/*angular.module("app.admin", ["app.admin.orders", "app.admin.restaurant", "app.admin.user", "app.admin.plan"]).config(function($stateProvider) {
    $stateProvider.state("app.admin", {
        url: "admin",
        abtract: !0,
        templateUrl: "scripts/common/views/layout-admin.html",
        controller: "AdminController"
    })
});
*/
