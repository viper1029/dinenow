
angular.module("theme.directives", []).directive("disableAnimation", ["$animate", function($animate) {
    return {
        restrict: "A",
        link: function($scope, $element, $attrs) {
            $attrs.$observe("disableAnimation", function(value) {
                $animate.enabled(!value, $element)
            })
        }
    }
}]).directive("slideOut", function() {
    return {
        restrict: "A",
        scope: {
            show: "=slideOut"
        },
        link: function(scope, element) {
            element.hide(), scope.$watch("show", function(newVal, oldVal) {
                newVal !== oldVal && element.slideToggle({
                    complete: function() {
                        scope.$apply()
                    }
                })
            })
        }
    }
}).directive("slideOutNav", ["$timeout", function($t) {
    return {
        restrict: "A",
        scope: {
            show: "=slideOutNav"
        },
        link: function(scope, element) {
            scope.$watch("show", function(newVal) {
                return $("body").hasClass("collapse-leftbar") ? void(1 == newVal ? element.css("display", "block") : element.css("display", "none")) : void(1 == newVal ? element.slideDown({
                    complete: function() {
                        $t(function() {
                            scope.$apply()
                        })
                    }
                }) : 0 == newVal && element.slideUp({
                    complete: function() {
                        $t(function() {
                            scope.$apply()
                        })
                    }
                }))
            })
        }
    }
}]).directive("panel", function() {
    return {
        restrict: "E",
        transclude: !0,
        scope: {
            panelClass: "@",
            heading: "@",
            panelIcon: "@"
        },
        templateUrl: "templates/panel.html"
    }
}).directive("pulsate", function() {
    return {
        scope: {
            pulsate: "="
        },
        link: function(scope, element) {
            $(element).pulsate(scope.pulsate)
        }
    }
}).directive("prettyprint", function() {
    return {
        restrict: "C",
        link: function(scope, element) {
            element.html(prettyPrintOne(element.html(), "", !0))
        }
    }
}).directive("passwordVerify", function() {
    return {
        require: "ngModel",
        scope: {
            passwordVerify: "="
        },
        link: function(scope, element, attrs, ctrl) {
            scope.$watch(function() {
                var combined;
                return (scope.passwordVerify || ctrl.$viewValue) && (combined = scope.passwordVerify + "_" + ctrl.$viewValue), combined
            }, function(value) {
                value && ctrl.$parsers.unshift(function(viewValue) {
                    var origin = scope.passwordVerify;
                    return origin !== viewValue ? void ctrl.$setValidity("passwordVerify", !1) : (ctrl.$setValidity("passwordVerify", !0), viewValue)
                })
            })
        }
    }
}).directive("backgroundSwitcher", function() {
    return {
        restrict: "EA",
        link: function(scope, element) {
            $(element).click(function() {
                $("body").css("background", $(element).css("background"))
            })
        }
    }
}).directive("panelControls", [function() {
    return {
        restrict: "E",
        require: "?^tabset",
        link: function(scope, element) {
            var panel = $(element).closest(".panel");
            0 == panel.hasClass(".ng-isolate-scope") && $(element).appendTo(panel.find(".options"))
        }
    }
}]).directive("panelControlCollapse", function() {
    return {
        restrict: "EAC",
        link: function(scope, element) {
            return element.bind("click", function() {
                $(element).toggleClass("fa-chevron-down fa-chevron-up"), $(element).closest(".panel").find(".panel-body").slideToggle({
                    duration: 200
                }), $(element).closest(".panel-heading").toggleClass("rounded-bottom")
            }), !1
        }
    }
}).directive("icheck", function($timeout) {
    return {
        require: "?ngModel",
        link: function($scope, element, $attrs, ngModel) {
            return $timeout(function() {
                var parentLabel = element.parent("label");
                parentLabel.length && parentLabel.addClass("icheck-label");
                var value;
                return value = $attrs.value, $scope.$watch($attrs.ngModel, function() {
                    $(element).iCheck("update")
                }), $(element).iCheck({
                    checkboxClass: "icheckbox_minimal-blue",
                    radioClass: "iradio_minimal-blue"
                }).on("ifChanged", function(event) {
                    return "checkbox" === $(element).attr("type") && $attrs.ngModel && $scope.$apply(function() {
                        return ngModel.$setViewValue(event.target.checked)
                    }), "radio" === $(element).attr("type") && $attrs.ngModel ? $scope.$apply(function() {
                        return ngModel.$setViewValue(value)
                    }) : void 0
                })
            })
        }
    }
}).directive("knob", function() {
    return {
        restrict: "EA",
        template: '<input class="dial" type="text"/>',
        scope: {
            options: "="
        },
        replace: !0,
        link: function(scope, element) {
            $(element).knob(scope.options)
        }
    }
}).directive("uiBsSlider", ["$timeout", function($timeout) {
    return {
        link: function(scope, element) {
            $timeout(function() {
                element.slider()
            })
        }
    }
}]).directive("tileLarge", function() {
    return {
        restrict: "E",
        scope: {
            item: "=data"
        },
        templateUrl: "templates/tile-large.html",
        transclude: !0
    }
}).directive("tileMini", function() {
    return {
        restrict: "E",
        scope: {
            item: "=data"
        },
        templateUrl: "templates/tile-mini.html"
    }
}).directive("tile", function() {
    return {
        restrict: "E",
        scope: {
            heading: "@",
            type: "@"
        },
        transclude: !0,
        templateUrl: "templates/tile-generic.html",
        link: function(scope, element) {
            var heading = element.find("tile-heading");
            heading.length && heading.appendTo(element.find(".tiles-heading"))
        },
        replace: !0
    }
}).directive("jscrollpane", ["$timeout", function($timeout) {
    return {
        restrict: "A",
        scope: {
            options: "=jscrollpane"
        },
        link: function(scope, element) {
            $timeout(function() {
                element.jScrollPane(-1 != navigator.appVersion.indexOf("Win") ? $.extend({
                    mouseWheelSpeed: 20
                }, scope.options) : scope.options), element.on("click", ".jspVerticalBar", function(event) {
                    event.preventDefault(), event.stopPropagation()
                }), element.bind("mousewheel", function(e) {
                    e.preventDefault()
                })
            })
        }
    }
}]).directive("stickyScroll", function() {
    return {
        restrict: "A",
        link: function(scope, element, attr) {
            function stickyTop() {
                var topMax = parseInt(attr.stickyScroll),
                    headerHeight = $("header").height();
                if (headerHeight > topMax && (topMax = headerHeight), 0 == $("body").hasClass("static-header")) return element.css("top", topMax + "px"); {
                    var window_top = $(window).scrollTop();
                    element.offset().top
                }
                topMax > window_top ? element.css("top", topMax - window_top + "px") : element.css("top", "0px")
            }
            $(function() {
                $(window).scroll(stickyTop), stickyTop()
            })
        }
    }
}).directive("rightbarRightPosition", function() {
    return {
        restrict: "A",
        scope: {
            isFixedLayout: "=rightbarRightPosition"
        },
        link: function(scope) {
            scope.$watch("isFixedLayout", function(newVal, oldVal) {
                newVal != oldVal && setTimeout(function() {
                    var $pc = $("#page-content"),
                        ending_right = $(window).width() - ($pc.offset().left + $pc.outerWidth());
                    0 > ending_right && (ending_right = 0), $("#page-rightbar").css("right", ending_right)
                }, 100)
            })
        }
    }
}).directive("fitHeight", ["$window", "$timeout", "$location", function($window, $timeout) {
    return {
        restrict: "A",
        scope: !0,
        link: function(scope, element) {
            scope.docHeight = $(document).height();
            var setHeight = function(newVal) {
                var diff = $("header").height();
                $("body").hasClass("layout-horizontal") && (diff += 112), newVal - diff > element.outerHeight() ? element.css("min-height", newVal - diff + "px") : element.css("min-height", $(window).height() - diff)
            };
            scope.$watch("docHeight", function(newVal) {
                setHeight(newVal)
            }), $(window).on("resize", function() {
                setHeight($(document).height())
            });
            var resetHeight = function() {
                scope.docHeight = $(document).height(), $timeout(resetHeight, 1e3)
            };
            $timeout(resetHeight, 1e3)
        }
    }
}]).directive("jscrollpaneOn", ["$timeout", function($timeout) {
    return {
        restrict: "A",
        scope: {
            applyon: "=jscrollpaneOn"
        },
        link: function(scope, element) {
            scope.$watch("applyon", function(newVal) {
                if (0 == newVal) {
                    var api = element.data("jsp");
                    return void(api && api.destroy())
                }
                $timeout(function() {
                    element.jScrollPane({
                        autoReinitialise: !0
                    })
                })
            })
        }
    }
}]).directive("backToTop", function() {
    return {
        restrict: "AE",
        link: function(scope, element) {
            element.click(function() {
                $("body").scrollTop(0)
            })
        }
    }
}).directive("maskinput", function() {
    return {
        restrict: "A",
        link: function(scope, element) {
            element.inputmask()
        }
    }
});
/*
angular.module("theme.google_maps", [])
.directive("gmap", ["$timeout", "GMaps", function($timeout, GMaps) {
    console.log("gmap");
    return {
        restrict: "A",
        scope: {
            options: "=",
            instance: "@"
        },
        link: function(scope, element, attr) {
            attr.id || (attr.id = Math.random().toString(36).substring(7), 
                element.attr("id", attr.id)), 
            scope.options.el = "#" + attr.id, void 0 !== attr.panorama ? GMaps.newPanorama(scope.options, scope.instance) : GMaps["new"](scope.options, scope.instance)
        }
    }
}]);
*/
angular.module("common.directives", ["app.owner.orders.services"])
.directive("daterangepicker", function(ordersResource) {
    return {
        restrict: "A",
        scope: {
            options: "=daterangepicker",
            start: "=dateBegin",
            end: "=dateEnd",
            renderStart: "=renderDateBegin",
            renderEnd: "=renderDateEnd",
            data: "=fetchData",
            id:"@"
        },
        link: function(scope, element, attrs) {
            element.daterangepicker(scope.options, function(start, end) {
                console.log(scope.options);
              
                console.log(scope.id);
                if (scope.start && (scope.start = start.format("YYYY-MM-DD HH:mm:ss")), 
                    scope.end && (scope.end = end.format("YYYY-MM-DD HH:mm:ss")), 
                    scope.renderStart && (scope.renderStart = start.format("YYYY-MM-DD")),
                     scope.renderEnd && (scope.renderEnd = end.format("YYYY-MM-DD")), 
                     scope.data) {
                    var params = "?from=" + scope.start + "&to=" + scope.end;
                if(scope.id==""){
                     ordersResource.getForAll(params).success(function(payload) {

                        scope.data = payload.data.orders
                    })
                }
                else{
                    ordersResource.get(scope.id,params).success(function(payload) {

                        scope.data = payload.data.orders
                    })
                }
                    
                }
                scope.$apply()
            })
        }
    }
}).directive("whenScrollEnds", function() {
    return {
        restrict: "A",
        scope: {
            pages: "=nextPage",
            isScrollEnd: "=isScrollEnd"
        },
        link: function(scope, element, attrs) {
            var visibleHeight = element.height(),
                threshold = 1e3;
            element.scroll(function() {
                var scrollableHeight = element.prop("scrollHeight"),
                    hiddenContentHeight = scrollableHeight - visibleHeight;
                hiddenContentHeight - element.scrollTop() <= threshold && (scope.pages++, scope.isScrollEnd = !0, scope.$apply(), scope.$apply(attrs.whenScrollEnds))
            })
        }
    }
}).directive("strictEmail", function() {
    var EMAIL_REGEXP = /^[_a-z0-9]+(\.[_a-z0-9]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
    return {
        require: "ngModel",
        restrict: "",
        link: function(scope, elm, attrs, ctrl) {
            ctrl && ctrl.$validators.email && (ctrl.$validators.email = function(modelValue) {
                return ctrl.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue)
            })
        }
    }
});

angular.module("app.admin.restaurant.directive", [])
.directive("googlePlaces", function() {
    return {
        restrict: "E",
        replace: !0,
        scope: {
            location: "=",
            address: "=",
            attributes: "="
        },
        template: '<input id="google_places" type="text" class="form-control"/>',
        required: "ngModel",
        link: function($scope, elm) {
            elm[0].placeholder = $scope.attributes.placeholder, elm[0].required = $scope.attributes.required, elm[0].ng_maxlength = $scope.attributes.ngMaxlength;
            var autocomplete = new google.maps.places.Autocomplete($("#google_places")[0], {});
            google.maps.event.addListener(autocomplete, "place_changed", function() {
                var place = autocomplete.getPlace();
                $scope.location = {
                    lat: place.geometry.location.lat(),
                    lng: place.geometry.location.lng()
                }, $scope.address = place.formatted_address, $scope.$apply()
            })
        }
    }
});


