angular.module("theme.services", [])
.service("$global", ["$rootScope", "EnquireService", "$document", function($rootScope, EnquireService, $document) {
    this.settings = {
        fixedHeader: !0,
        headerBarHidden: !0,
        leftbarCollapsed: !1,
        leftbarShown: !1,
        rightbarCollapsed: !1,
        fullscreen: !1,
        layoutHorizontal: !1,
        layoutHorizontalLargeIcons: !1,
        layoutBoxed: !1,
        showSearchCollapsed: !1
    };
    var brandColors = {
        "default": "#ecf0f1",
        inverse: "#95a5a6",
        primary: "#3498db",
        success: "#2ecc71",
        warning: "#f1c40f",
        danger: "#e74c3c",
        info: "#1abcaf",
        brown: "#c0392b",
        indigo: "#9b59b6",
        orange: "#e67e22",
        midnightblue: "#34495e",
        sky: "#82c4e6",
        magenta: "#e73c68",
        purple: "#e044ab",
        green: "#16a085",
        grape: "#7a869c",
        toyo: "#556b8d",
        alizarin: "#e74c3c"
    };
    this.getBrandColor = function(name) {
        return brandColors[name] ? brandColors[name] : brandColors["default"]
    }, $document.ready(function() {
        EnquireService.register("screen and (max-width: 767px)", {
            match: function() {
                $rootScope.$broadcast("globalStyles:maxWidth767", !0)
            },
            unmatch: function() {
                $rootScope.$broadcast("globalStyles:maxWidth767", !1)
            }
        })
    }), this.get = function(key) {
        return this.settings[key]
    }, this.set = function(key, value) {
        this.settings[key] = value, $rootScope.$broadcast("globalStyles:changed", {
            key: key,
            value: this.settings[key]
        }), $rootScope.$broadcast("globalStyles:changed:" + key, this.settings[key])
    }, this.values = function() {
        return this.settings
    }
}]).factory("pinesNotifications", function() {
    return {
        notify: function(args) {
            var notification = $.pnotify(args);
            return notification.notify = notification.pnotify, notification
        },
        defaults: $.pnotify.defaults
    }
}).factory("progressLoader", function() {
    return {
        start: function() {
            $(document).skylo("start")
        },
        set: function(position) {
            $(document).skylo("set", position)
        },
        end: function() {
            $(document).skylo("end")
        },
        get: function() {
            return $(document).skylo("get")
        },
        inch: function(amount) {
            $(document).skylo("show", function() {
                $(document).skylo("inch", amount)
            })
        }
    }
}).factory("EnquireService", ['$window', function($window) {
    return $window.enquire;

}])
.factory("$bootbox", ["$modal", function($modal) {
    return void 0 == $.fn.modal && ($.fn.modal = function(directive) {
        var that = this;
        if ("hide" == directive) return void(this.data("bs.modal") && (this.data("bs.modal").close(), $(that).remove()));
        if ("show" != directive) {
            var modalInstance = $modal.open({
                template: $(this).find(".modal-content").html()
            });
            this.data("bs.modal", modalInstance), setTimeout(function() {
                $(".modal.ng-isolate-scope").remove(), $(that).css({
                    opacity: 1,
                    display: "block"
                }).addClass("in")
            }, 100)
        }
    }), bootbox
}]).service("lazyLoad", ["$q", "$timeout", function($q, $t) {
    var deferred = $q.defer(),
        promise = deferred.promise;
    this.load = function(files) {
        return angular.forEach(files, function(file) {
            file.indexOf(".js") > -1 && ! function(d, script) {
                var fDeferred = $q.defer();
                script = d.createElement("script"), script.type = "text/javascript", script.async = !0, script.onload = function() {
                    $t(function() {
                        fDeferred.resolve()
                    })
                }, script.onerror = function() {
                    $t(function() {
                        fDeferred.reject()
                    })
                }, promise = promise.then(function() {
                    return script.src = file, d.getElementsByTagName("head")[0].appendChild(script), fDeferred.promise
                })
            }(document)
        }), deferred.resolve(), promise
    }
}]).filter("safe_html", ["$sce", function($sce) {
    return function(val) {
        return $sce.trustAsHtml(val)
    }
}]);

// angular.module("theme.google_maps", [])
// .service("GMaps", ["$rootScope", function($rootScope) {
//     this["new"] = function(options, instance) {
//         console.log(options);
//         console.log(instance);
//         var gmaps = new GMaps(options);
//         $rootScope.$broadcast("GMaps:created", {
//             key: instance,
//             map: gmaps
//         })
//     }, this.newPanorama = function(options, instance) {
//         var gmaps = GMaps.createPanorama(options);
//         $rootScope.$broadcast("GMaps:created", {
//             key: instance,
//             map: gmaps
//         })
//     }
// }]);


angular.module("common.service", ["ngTable"]).factory("ngTableConfig", function($filter, ngTableParams, TABLE_OPTIONS) {
    return {
        config: function(data) {
            var tableParams;
            return tableParams = new ngTableParams({
                page: TABLE_OPTIONS.page,
                count: TABLE_OPTIONS.count
            }, {
                total: data.length,
                getData: function($defer, params) {
                    var orderedData = params.sorting() ? $filter("orderBy")(data, params.orderBy()) : data;
                    $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                }
            })
        }
    }
}).factory("globalHandleUI", function() {
    return {
        handleToggleLeftBar: function() {
            angular.element("#menu-toggle").off("click"), angular.element("#menu-toggle").on("click", function(e) {
                e.preventDefault(), e.stopPropagation(), angular.element("#wrapper").toggleClass("toggled")
            })
        },
        handleAccordionMenu: function() {
            angular.element("#cssmenu li.active").addClass("open").children("ul").show(), angular.element("#cssmenu li.has-sub>a").on("click", function() {
                angular.element(this).removeAttr("href");
                var element = angular.element(this).parent("li");
                element.hasClass("open") ? (element.removeClass("open"), element.find("li").removeClass("open"), element.find("ul").slideUp(200)) : (element.addClass("open"), element.children("ul").slideDown(200), element.siblings("li").children("ul").slideUp(200), element.siblings("li").removeClass("open"), element.siblings("li").find("li").removeClass("open"), element.siblings("li").find("ul").slideUp(200))
            })
        },
        handleShowHideMenu: function() {
            angular.element("#handle-showing").on("click", function(e) {
                e.preventDefault(), angular.element("#menu-showing").toggleClass("showing")
            })
        },
        handleShowHideInfoUserBox: function() {
            angular.element("#info-name").on("click", function(e) {
                e.preventDefault(), angular.element(".box-user").toggleClass("showing")
            }), angular.element(document).on("click", function(event) {
                angular.element(event.target).closest("#info-name").length || angular.element(".box-user").removeClass("showing")
            })
        },
        handleShowHideBoxStatus: function() {
            angular.element(".handle-status").on("click", function(e) {
                e.preventDefault(), angular.element(".box-status").toggleClass("showing")
            })
        },
        handleHighlightFitler: function(param) {
            console.log(param);
            var ele;
            ele = angular.element(param ? "#" + param : "#all");
            var groupEle = angular.element(".filter");
            groupEle.css("background-color", "#fff"), 
            groupEle.css("font-weight", "400"), 
            ele.css("background-color", "#E9ECF0"), 
            ele.css("font-weight", "900")
        }
    }
}).factory("sharedProperties", function() {
    var objectValue = {};
    return {
        setObject: function(value) {
            objectValue = value
        },
        getObject: function() {
            return objectValue
        }
    }
}).factory("handleCalendarHours", function() {
    return {
        formatDatetime: {
            startTimes: ["06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"],
            endTimes: [],
            days: [{
                label: "MON",
                value: 1
            }, {
                label: "TUE",
                value: 2
            }, {
                label: "WED",
                value: 3
            }, {
                label: "THU",
                value: 4
            }, {
                label: "FRI",
                value: 5
            }, {
                label: "SAT",
                value: 6
            }, {
                label: "SUN",
                value: 7
            }]
        },
        getDateByIndex: function(dayIndex) {
            var date = moment().startOf("week").add("days", dayIndex);
            return date.format("YYYY-MM-DD")
        },
        getDateByISO8601: function(date, time) {
            return date + "T" + time + ":00Z"
        },
        getEndTimes: function(startTime) {
            for (var strings = startTime.split(":"), startAt = 1 * strings[0], endTime = [], tempStr = "", i = startAt; 23 > i; i++) startAt++, tempStr = startAt > 9 ? startAt + ":00" : "0" + startAt + ":00", endTime.push(tempStr);
            return endTime
        },
        getUniqueID: function() {
            return "_" + Math.random().toString(36).substr(2, 9)
        },
        synOjectWithCalendar: function(hours) {
            for (var events = [], i = 0; i < hours.length; i++) {
                var objEvent = {
                    id: hours[i].id,
                    start: "",
                    end: "",
                    color: "#066EB0",
                    textColor: "white"
                };
                objEvent.start = moment(hours[i].fromTime).format(), objEvent.end = moment(hours[i].toTime).format(), events.push(objEvent)
            }
            return events
        },
        synObjectOnAPI: function(events) {
            for (var arrayTimes = {
                    dineInHours: [],
                    acceptDeliveryHours: [],
                    acceptTakeOutHours: []
                }, i = 0; i < events.length; i++) "Dine-In" === events[i].title && arrayTimes.dineInHours.push({
                id: events[i].id,
                weekDayType: events[i].dayInWeek,
                fromTime: events[i].start,
                toTime: events[i].end
            }), "Delivery" === events[i].title && arrayTimes.acceptDeliveryHours.push({
                id: events[i].id,
                weekDayType: events[i].dayInWeek,
                fromTime: events[i].start,
                toTime: events[i].end
            }), "TakeOut" === events[i].title && arrayTimes.acceptTakeOutHours.push({
                id: events[i].id,
                weekDayType: events[i].dayInWeek,
                fromTime: events[i].start,
                toTime: events[i].end
            });
            return arrayTimes
        },
        checkDateInWeek: function(date) {
            var monday = moment().startOf("week").add("days", 0).format("YYYY-MM-DD"),
                sunday = moment().startOf("week").add("days", 7).format("YYYY-MM-DD"),
                isInWeek = moment(date).isBefore(sunday) && moment(date).isAfter(monday);
            return isInWeek
        },
        checkIsSameDate: function(date, events) {
            for (var dateAdd = moment(date).format("YYYY-MM-DD"), i = 0; i < events.length; i++) {
                var eventDate = moment(events[i].start).format("YYYY-MM-DD"),
                    isSameDate = moment(dateAdd).isSame(eventDate);
                if (isSameDate) return !0
            }
            return !1
        }
    }
});

angular.module("common.filters", []).filter("dateformated", function() {
    return function(dt) {
        if (dt) {
            var now = moment().format("MMM D, YYYY"),
                _dt = dt.split(", "),
                t = _dt[1].split(" ");
            return now !== _dt[0] + ", " + t[0] ? _dt[0] + ", " + t[0] + "  at " + t[1] + " " + t[2] : t[1] + " " + t[2]
        }
    }
});

angular.module("app.security.service", []).
factory("authResource", ["$http","$cookieStore", "ROOT", "Base64", function($http, $cookieStore, ROOT, Base64) {
    console.log("Basic d2VudHdvcnRobWFuOkNoYW5nZV9tZQ==");
    return {
       /*  login: function(params) {
            $cookieStore.remove("authToken");
            $cookieStore.put("authToken", Base64.encode(params.email+":"+params.password));
            console.log($cookieStore.get("authToken")+"ssssssssssssssssss");
            return $http.post(ROOT + "auth/login", params)
        }, */
        
        login: function(params) {
   
           $cookieStore.remove("authToken");
           $cookieStore.put("authToken", Base64.encode(params.email+":"+params.password));
           $http.defaults.headers.common.Authentication = 'Basic '+ $cookieStore.get("authToken");
           $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
            $http.defaults.headers.post["Content-Type"] = "application/json";
   
            return $http.post(ROOT + "auth/login", params)
        },
        
        logout: function() {
            return $http.post(ROOT + "auth/logout")
        },
        getUserById: function(action, id) {
            return $http.get(ROOT + "user/" + id)
        },
        signup: function(params) {
            return $http.post(ROOT + "auth/register", params)
        }
    }
}])
.service('Base64', function() {
 
    var Base64 = {

        keyStr: 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=',

        encode: function (input) {
            console.log(input);
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                    this.keyStr.charAt(enc1) +
                    this.keyStr.charAt(enc2) +
                    this.keyStr.charAt(enc3) +
                    this.keyStr.charAt(enc4);
                    console.log(output);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);
            return output;
        },

        decode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                window.alert("There were invalid base64 characters in the input text.\n" +
                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                    "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            do {
                enc1 = this.keyStr.indexOf(input.charAt(i++));
                enc2 = this.keyStr.indexOf(input.charAt(i++));
                enc3 = this.keyStr.indexOf(input.charAt(i++));
                enc4 = this.keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";

            } while (i < input.length);

            return output;
        }
    };
return Base64;
})
.factory("responseStatus", function($q, $location, $cookieStore) {
    return function(promise) {
        return promise.then(function(response) {
            return response
        }, function(response) {
            return 401 === response.status ? ($location.path("/login"), $cookieStore.remove("authToken"), response) : $q.reject(response)
        })
    }
}).factory("interceptor", function($q, $location, $cookieStore) {
    return {
        request: function(config) {
            return $cookieStore.get("authToken") ? config : ($location.path("/login"), config)
        },
        response: function(response) {
            return response
        },
        responseError: function(err) {
            return 401 === err.status ? ($location.path("/login"), $cookieStore.remove("authToken"), err) : $q.reject(err)
        }
    }
});

angular.module("app.owner.menu.service", []).factory("categoriesResource", ["$http", "$rootScope", "ROOT", function($http, $rootScope, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "categories/" + params.id, params)) : $http[action](ROOT + "categories", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/categories")
        },
        getByID: function(params) {
            return $http.get(ROOT + "categories/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "categories/" + params.id)
        }
    }
}]).factory("itemsResource", ["$http", "$rootScope", "ROOT", function($http, $rootScope, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "items/" + params.id, params)) : $http[action](ROOT + "items", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/items")
        },
        getByID: function(params) {
            return $http.get(ROOT + "items/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "items/" + params.id)
        }
    }
}]).factory("submenusResource", ["$http", "$rootScope", "ROOT", function($http, $rootScope, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "submenus/" + params.id, params)) : $http[action](ROOT + "submenus", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/submenus")
        },
        getByID: function(params) {
            return $http.get(ROOT + "submenus/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "submenus/" + params.id)
        }
    }
}]).factory("menusResource", ["$http", "$rootScope", "ROOT", function($http, $rootScope, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "menus/" + params.id, params)) : $http[action](ROOT + "menus", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/menus")
        },
        getByID: function(params) {
            return $http.get(ROOT + "menus/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "menus/" + params.id)
        },
        postHour: function(params) {
            return $http.post(ROOT + "menus/" + params.id + "/time", params)
        }
    }
}]).factory("sizesResource", ["$http", "$rootScope", "ROOT", function($http, $rootScope, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "sizes/" + params.id, params)) : $http[action](ROOT + "sizes", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/sizes")
        },
        getByID: function(params) {
            return $http.get(ROOT + "sizes/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "sizes/" + params.id)
        }
    }
}]).factory("addonResource", ["$http", "$rootScope", "ROOT", function($http, $rootScope, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "addons/" + params.id, params)) : $http[action](ROOT + "addons", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/addons")
        },
        getByID: function(params) {
            return $http.get(ROOT + "addons/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "addons/" + params.id)
        }
    }
}]).factory("modifiersResource", ["$http", "$rootScope", "ROOT", function($http, $rootScope, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "modifiers/" + params.id, params)) : $http[action](ROOT + "modifiers", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/modifiers")
        },
        getByID: function(params) {
            return $http.get(ROOT + "modifiers/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "modifiers/" + params.id)
        }
    }
}]);

angular.module("app.owner.restaurant.service", []).factory("deliveryZoneResource", function($http, $rootScope, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "deliver_zones/" + params.id, params)) : $http[action](ROOT + "deliver_zones", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/deliver_zones")
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "deliver_zones/" + params.id)
        }
    }
}).factory("infoResource", function($http, $rootScope, ROOT) {
    return {
        get: function() {
            return $http.get(ROOT + "restaurants/" + $rootScope.restaurantID + "/info")
        },
        update: function(params) {
            return $http.put(ROOT + "restaurants/" + params.id, params)
        }
    }
}).factory("timeResource", function($http, $rootScope, ROOT) {
    return {
        putTime: function(params) {
            return $http.put(ROOT + "restaurants/" + $rootScope.restaurantID + "/time", params)
        }
    }
}).factory("subscriptionsResource", function($http, $rootScope, ROOT, TOKEN) {
    return {
        getCard: function(customerStripe) {
            var stripeUrl = "https://api.stripe.com/v1/customers/",
                stripeToken = "Bearer " + TOKEN;
            return $http({
                url: stripeUrl + customerStripe + "/sources",
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: stripeToken
                }
            })
        },
        addCard: function(userID, params) {
            return $http.post(ROOT + "stripe/addCardToCustomer/" + userID, params)
        },
        deleteCard: function(userID, cardID) {
            return $http["delete"](ROOT + "stripe/deleteCard/" + userID + "/" + cardID)
        },
        deleteToken: function(params) {
            return $http["delete"](ROOT + "stripe/deleteToken", params)
        },
        getPlan: function() {
            var stripeToken = "Bearer " + TOKEN;
            return $http({
                url: "https://api.stripe.com/v1/plans",
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: stripeToken
                }
            })
        },
        getPlanAssigned: function() {
            return $http.get(ROOT + "stripe/plan/" + $rootScope.restaurantID)
        },
        registerPlan: function(params) {
            return $http.post(ROOT + "stripe/registerPlan/" + $rootScope.restaurantID, params)
        },
        getSubscription: function() {
            return $http.get(ROOT + "stripe/getSubscriptions/" + $rootScope.restaurantID)
        },
        cancelSubscription: function(subscriptionID) {
            return $http["delete"](ROOT + "stripe/cancelSubscription/" + $rootScope.restaurantID + "/" + subscriptionID)
        }
    }
}).factory("demoPolygonResource", function($http, ROOT) {
    return {
        getPolygon: function() {
            return $http.get(ROOT + "restaurants/polygon")
        },
        searchByLocation: function(params) {
            return $http.get(ROOT + "restaurants/searchByLocation/", {
                params: params
            })
        }
    }
});

angular.module("app.owner.orders.services", [])
.factory("ordersResource", ["$rootScope", "$http", "ROOT", function($rootScope, $http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id && (action = "put"), $http[action](ROOT + "customerOrder", params)
        },
        get: function(restaurantid,params) {
            console.log(params);
            return $http.get(ROOT +"restaurants/"+restaurantid+"/order_details" + params)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "orders/" + params.id)
        },
        getForAll :function(params){
            console.log(params);
            return $http.get(ROOT +"order_details" + params)
        }
    }
}]);

angular.module("app.admin.restaurant.manage.menu.service", [])
.factory("adminCategoriesResource", ["$http", "ROOT", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            return params.id ? ($http["put"](ROOT + "categories/" + params.id, params)) :
                $http["post"](ROOT + "categories", params)
        },
        getAll: function(params) {
            return $http.get(ROOT + "restaurants/" + params + "/categories")
        },
        getByID: function(params) {
            return $http.get(ROOT + "categories/" + params.id)
        },
        delete: function(params) {
            return $http["delete"](ROOT + "categories/" + params.id)
        }
    }
}]).factory("adminItemsResource", ["$http", "ROOT", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "items/" + params.id, params)) : $http[action](ROOT + "items", params)
        },
        get: function(params) {
            return $http.get(ROOT + "restaurants/" + params + "/items")
        },
        getByID: function(params) {
            return $http.get(ROOT + "items/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "items/" + params.id)
        }
    }
}]).factory("adminSubmenusResource", ["$http", "ROOT", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "submenus/" + params.id, params)) : $http[action](ROOT + "submenus", params)
        },
        get: function(params) {
            return $http.get(ROOT + "restaurants/" + params + "/submenus")
        },
        getByID: function(params) {
            return $http.get(ROOT + "submenus/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "submenus/" + params.id)
        }
    }
}]).factory("adminMenusResource", ["$http", "ROOT", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "menus/" + params.id, params)) : $http[action](ROOT + "menus", params)
        },
        get: function(params) {
            console.log(params);
            return $http.get(ROOT + "restaurants/" + params + "/menus")
        },
        getByID: function(params) {
            return $http.get(ROOT + "menus/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "menus/" + params.id)
        }
    }
}]).factory("adminSizesResource", ["$http", "ROOT", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            console.log(params);
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "sizes/" + params.id, params)) : $http[action](ROOT + "sizes", params)
        },
        getAll: function(params) {
            return $http.get(ROOT + "restaurants/" + params + "/sizes")
        },
        getByID: function(params) {
            return $http.get(ROOT + "size/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "size/" + params.id)
        }
    }
}]).factory("adminAddonResource", ["$http", "ROOT", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "addons/" + params.id, params)) : $http[action](ROOT + "addons", params)
        },
        getAll: function(params) {
            return $http.get(ROOT + "restaurants/" + params + "/addons")
        },
        getByID: function(params) {
            return $http.get(ROOT + "addons/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "addons/" + params.id)
        }
    }
}]).factory("adminModifiersResource", ["$http", "ROOT", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "modifiers/" + params.id, params)) : $http[action](ROOT + "modifiers", params)
        },
        get: function(params) {
            return $http.get(ROOT + "restaurants/" + params + "/modifiers")
        },
        getByID: function(params) {
            return $http.get(ROOT + "modifiers/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "modifiers/" + params.id)
        }
    }
}]);

angular.module("app.admin.restaurant.manage.restaurant.service", [])
.factory("adminDeliveryZoneResource", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "deliver_zones/" + params.id, params)) : $http[action](ROOT + "deliver_zones", params)
        },
        get: function(params) {
            return $http.get(ROOT + "restaurants/" + params + "/deliver_zones")
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "deliver_zones/" + params.id)
        }
    }
}).factory("adminInfoResource", function($http, ROOT) {
    return {
        get: function(params) {
            return $http.get(ROOT + "restaurants/" + params + "/info")
        },
        update: function(params) {
            return $http.put(ROOT + "restaurants/" + params.id, params)
        }
    }
}).factory("adminTimeResource", function($http, $rootScope, ROOT) {
    return {
        putTime: function(restaurantID, params) {
            return $http.put(ROOT + "restaurants/" + restaurantID + "/time", params)
        }
    }
}).factory("adminTaxesResource", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            console.log(params);
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "taxes/" + params.id, params)) : $http[action](ROOT + "taxes", params)
        },
        get: function(params) {
             return $http.get(ROOT + "restaurants/" + params + "/taxes")
        },
        getByID: function(params) {
           return $http.get(ROOT + "taxes/" + params.id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "taxes/" + params.id)
        }
    }
}).factory("adminSubscriptionResource", function($http, ROOT, TOKEN) {
    return {
        get: function() {
            var stripeToken = "Bearer " + TOKEN;
            return $http({
                url: "https://api.stripe.com/v1/plans",
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: stripeToken
                }
            })
        },
        getPlan: function() {
            return $http.get(ROOT + "stripe/getListPlan")
        },
        assignPlan: function(restaurantID, params) {
            return $http.post(ROOT + "stripe/assignPlan/" + restaurantID, params)
        },
        getPlanAssigned: function(restaurantID) {
            return $http.get(ROOT + "stripe/plan/" + restaurantID)
        },
        getSubscriptions: function(restaurantID) {
            return $http.get(ROOT + "stripe/getSubscriptions/" + restaurantID)
        },
        cancelSubscription: function(restaurantID, subscriptionID) {
            return $http["delete"](ROOT + "stripe/cancelSubcription/" + restaurantID + "/" + subscriptionID)
        }
    }
}).factory("adminDemoPolygonResource", function($http, ROOT) {
    return {
        getPolygon: function() {
            return $http.get(ROOT + "restaurants/polygon")
        },
        searchByLocation: function(params) {
            return $http.get(ROOT + "restaurants/searchByLocation/", {
                params: params
            })
        }
    }
});


angular.module("app.admin.restaurant.manage.orders.services", [])
.factory("adminOrdersResource", function($http, $stateParams, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id && (action = "put"), $http[action](ROOT + "customerOrder", params)
        },
        get: function(restaurantID, params) {
            console.log(params);
            console.log(restaurantID);
            console.log(ROOT + "restaurants/" + restaurantID + "/order_details" + params);
            return $http.get(ROOT + "restaurants/" + restaurantID + "/order_details" + params)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "orders/" + params.id)
        }
    }
})
.factory("adminOrderResource", ["$rootScope", "$http", "ROOT", function($rootScope, $http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id && (action = "put"), $http[action](ROOT + "order_details", params)
        },
        get: function(params) {
            console.log(params)
            return $http.get(ROOT + "order_details" + params)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "order_details/" + params.id)
        }
    }
}]);

angular.module("app.admin.restaurant.service", [])
.factory("restaurantsResource", ["$http", "ROOT", "TOKEN", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            console.log(params);
            var action = "post";
           // console.log($http["put"](ROOT + "restaurants/" + params.id));
            return params.id ? (action = "put", $http[action](ROOT + "restaurants/" + params.id, params)) : $http[action](ROOT + "restaurants", params)
        },
        get: function() {
            return $http.get(ROOT + "restaurants")
        },
        getById: function(id) {
            return $http.get(ROOT + "restaurants/" + id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "restaurants/" + params.id)
        }
    }
}]);

angular.module("app.admin.user.services", [])
.factory("usersResource", ["$http", "ROOT", "TOKEN", function($http, ROOT) {
    return {
        addOrUpdate: function(params) {
            console.log(params);
            var action = "post";
            return params.id ? (action = "put", $http[action](ROOT + "user/" + params.id, params)) : $http[action](ROOT + "user/", params)
        },
        get: function() {
            console.log("getting data");
            return $http.get(ROOT + "user/")
        },
        getById: function(id) {
            return $http.get(ROOT + "user/" + id)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "user/" + params.id)
        }
    }
}]);


angular.module("app.admin.plan.service", []).factory("adminPlanResource", function($http, ROOT) {
    return {
        get: function() {
            return $http.get(ROOT + "stripe/getListPlan")
        },
        post: function(params) {
            return $http.post(ROOT + "stripe/createPlan", params)
        },
        put: function(params) {
            return $http.put(ROOT + "stripe/updatePlan/" + params.id, params)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "stripe/deletePlan/" + params.id)
        }
    }
});

 angular.module("app.admin.orders.services", [])
 .factory("adminOrdersResource", ["$rootScope", "$http", "ROOT", function($rootScope, $http, ROOT) {
    return {
        addOrUpdate: function(params) {
            var action = "post";
            return params.id && (action = "put"), $http[action](ROOT + "order_details", params)
        },
        get: function(params) {
            console.log(params)
            return $http.get(ROOT + "order_details/" + params)
        },
        "delete": function(params) {
            return $http["delete"](ROOT + "order_details/" + params.id)
        }
    }
}]);








