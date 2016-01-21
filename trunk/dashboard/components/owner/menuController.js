angular.module("app.owner.menu.menus", [])
    .controller("MenusController", function($rootScope, $scope, $filter, $state, $modal, ngTableConfig, submenusResource, menusResource, globalHandleUI) {
        globalHandleUI.handleToggleLeftBar(), $scope.isCollapsed = !1, $scope.menuList = [], $scope.tempSubmenuTree = [], $scope.submenuTree = [], $scope.menuTree = {
            menuName: "",
            menuDescription: "",
            submenus: []
        }, $scope.addTipText = function() {
            var emptyTree = document.getElementsByClassName("angular-ui-tree-empty");
            if (emptyTree) {
                var p = document.createElement("p"),
                    textNode = document.createTextNode("Drag and Drop from Submenu to here");
                p.appendChild(textNode), emptyTree[0].appendChild(p), emptyTree[0].className += " empty-tree"
            }
        }, $scope.getMenuList = function() {
            menusResource.get().success(function(payload) {
                $scope.menuList = payload.data.menus, $scope.tableParams = ngTableConfig.config($scope.menuList)
            }).error(function() {})
        }, $scope.getSubMenuList = function() {

            submenusResource.get().success(function(payload) {
                $scope.submenuTree = payload.data.submenus, $scope.tempSubmenuTree = angular.copy($scope.submenuTree)
            }).error(function() {})
        }, $scope.getSubMenuByID = function(param) {
            $scope.submenuTree = [], setTimeout(function() {
                submenusResource.getByID(param).success(function(payload) {
                    $scope.submenuTree.push(payload.data).submenus, $scope.tempSubmenuTree = angular.copy($scope.submenuTree)
                })
            }, 250)
        }, $scope.init = function() {
            setTimeout(function() {
                $scope.getMenuList()
            }, 100), setTimeout(function() {
                $scope.getSubMenuList()
            }, 200)
        }, $scope.init(), $scope.setHourMenu = function(item) {
            var modalInstance = $modal.open({
                templateUrl: "scripts/owner/menu/views/modal/menu-hours.html",
                resolve: {
                    itemToProcess: function() {
                        return item
                    }
                },
                size: "lg",
                controller: function($scope, $q, $modalInstance, itemToProcess, menusResource) {
                    var getHours = function(itemToProcess) {
                        var deferred = $q.defer();
                        return setTimeout(function() {
                            var data = angular.copy(itemToProcess) || {};
                            data && deferred.resolve(data)
                        }, 300), deferred.promise
                    };
                    $scope.init = function(data) {
                        $scope.events = [], $scope.itemToProcess = data, $scope.datetimes = {
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
                        }, $scope.commonMethods = function() {
                            return {
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
                                        objEvent.start = moment.utc(hours[i].fromTime).format(), objEvent.end = moment.utc(hours[i].toTime).format(), events.push(objEvent)
                                    }
                                    return events
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
                        }, $scope.initEndTime = function(startTime) {
                            $scope.datetimes.endTimes = $scope.commonMethods().getEndTimes(startTime)
                        }, $scope.addEvents = function(params) {
                            var date = $scope.commonMethods().getDateByIndex(params.weekDayType),
                                startTime = $scope.commonMethods().getDateByISO8601(date, params.fromTime),
                                endTime = $scope.commonMethods().getDateByISO8601(date, params.toTime),
                                objEvent = {
                                    id: $scope.commonMethods().getUniqueID(),
                                    title: params.menuName,
                                    start: startTime,
                                    end: endTime,
                                    color: "#066EB0",
                                    textColor: "white"
                                },
                                tempObject = {
                                    //   id: objEvent.id,
                                    weekDayType: $scope.datetimes.days[params.weekDayType - 1].label,
                                    fromTime: startTime,
                                    toTime: endTime
                                };
                            params.weekDayType = "", params.fromTime = "", params.toTime = "";
                            var isSameDate = $scope.commonMethods().checkIsSameDate(startTime, $scope.events);
                            isSameDate ? alertify.error("This day was set hours! Please set the hour on another day of weeks") : ($scope.itemToProcess.hours.push(tempObject), $scope.events.push(objEvent))
                        }, $scope.onClearEvents = function() {
                            alertify.confirm("Are you sure want to delete all time ?", function(e) {
                                if (e) {
                                    $scope.itemToProcess.hours = [];
                                    for (var i = $scope.events.length - 1; i >= 0; i--) $scope.events.splice(i, 1)
                                }
                            })
                        }, $scope.deleteEvents = function(index) {
                            $scope.itemToProcess.hours.splice(index, 1);
                            for (var i = 0; i < $scope.events.length; i++)
                                for (var j = 0; j < $scope.itemToProcess.hours.length; j++)
                                    if ($scope.events[i].id == $scope.itemToProcess.hours[j].id) {
                                        $scope.events.splice(i, 1);
                                        break
                                    }
                        }, $scope.filterDateInWeek = function() {
                            if ($scope.itemToProcess.hours.length > 0) {
                                for (var tempHours = angular.copy($scope.itemToProcess.hours), i = 0; i < tempHours.length; i++) {
                                    var startTime = moment(tempHours[i].fromTime).format("YYYY-MM-DD"),
                                        isDateInWeek = $scope.commonMethods().checkDateInWeek(startTime);
                                    isDateInWeek || tempHours.splice(i, 1)
                                }
                                $scope.itemToProcess.hours = tempHours
                            }
                        }, $scope.filterDateInWeek()
                    }, $scope.initCalendar = function() {
                        $scope.isLoadedCalendar = !0,
                            $scope.getEvents = function(start, end, timezone, callback) {
                                return $scope.events ? callback($scope.events) : void 0
                            }, $scope.uiConfig = {
                            calendar: {
                                header: {
                                    left: "",
                                    center: "",
                                    right: "today prev,next"
                                },
                                events: $scope.getEvents,
                                timeFormat: "H(:mm)",
                                defaultView: "agendaWeek",
                                editable: !1,
                                eventLimit: !0,
                                firstDay: 1,
                                allDaySlot: !1,
                                fixedWeekCount: !1,
                                eventClick: function(calEvent) {
                                    alertify.confirm("Are you sure want to delete this time ?", function(e) {
                                        if (e)
                                            for (var i = 0; i < $scope.events.length; i++)
                                                if ($scope.events[i].id === calEvent.id) {
                                                    $scope.events.splice(i, 1), $scope.itemToProcess.hours.splice(i, 1);
                                                    break
                                                }
                                    })
                                }
                            }
                        };
                        var timeStampHours = $scope.itemToProcess.hours,
                            events = $scope.commonMethods().synOjectWithCalendar(timeStampHours);
                        $scope.events = events || [], $scope.eventSources = [$scope.events]
                    }, $scope.save = function() {
                        menusResource.addOrUpdate($scope.itemToProcess).success(function() {
                            $modalInstance.close($scope.itemToProcess)
                        }).error(function() {
                            alertify.error("Oops! Please try again")
                        })
                    }, $scope.cancel = function() {
                        $modalInstance.dismiss("cancel")
                    };
                    var promise = getHours(itemToProcess);
                    promise.then(function(data) {
                        $scope.init(data), $scope.initCalendar()
                    }, function(error) {})
                }
            });
            modalInstance.result.then(function(data) {
                data && (alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                    reload: !0
                }))
            })
        }, $scope.getActiveItem = function(item) {
            return item.id == $scope.menuTree.id ? "item-active" : void 0
        }, $scope["delete"] = function(node, value) {
            node.remove();
            for (var isMatchId = !1, i = 0; i < $scope.submenuTree.length; i++)
                if ($scope.submenuTree[i].id == value.id) {
                    isMatchId = !0;
                    break
                }
            isMatchId || $scope.submenuTree.push(value)
        }, $scope.resetForm = function() {
            $scope.menuTree = {
                menuName: "",
                menuDescription: "",
                submenus: []
            }, $scope.submenuTree = angular.copy($scope.tempSubmenuTree)
        }, $scope.refreshView = function(data) {
            var isMatchId = !1;
            if (data) {
                for (var i = 0; i < $scope.menuList.length; i++)
                    if ($scope.menuList[i].id == data.id) {
                        isMatchId = !0, $scope.menuList[i] = data, alertify.success("Updated Successfully");
                        break
                    }
                isMatchId || ($scope.menuList = [], $scope.resetForm(), alertify.success("Created Successfully")), $scope.getMenuList()
            }
        }, $scope.syncObject = function(obj) {
            for (var tempObj = angular.copy(obj), temp = {
                submenus: [],
                category: {
                    id: "",
                    items: []
                }
            }, i = 0; i < tempObj.submenus.length; i++) {
                var id = tempObj.submenus[i].id;
                temp.subMenus.push(id)
            }
            return tempObj.submenus = temp.submenus, tempObj.restaurantId = $rootScope.restaurantID || "", tempObj
        }, $scope.save = function(item) {
            var obj = $scope.syncObject(item);
            menusResource.addOrUpdate(obj).success(function() {
                $state.go("app.owner.menu.menus.list", {}, {
                    reload: !0
                }), alertify.success("Successfully")
            }).error(function() {
                alertify.error("Oops! Please try again")
            })
        }, $scope.createMenu = function() {
            $state.go("app.owner.menu.menus.create"), setTimeout(function() {
                $scope.addTipText()
            }, 100), $scope.resetForm()
        }, $scope.editMenu = function(item) {
            item.editLoading = !0, setTimeout(function() {
                $state.go("app.owner.menu.menus.edit"), menusResource.getByID(item).success(function(payload) {
                    if ($scope.menuTree = payload.data, $scope.submenuTree = angular.copy($scope.tempSubmenuTree), $scope.menuTree.submenus.length > 0)
                        for (var i = 0; i < $scope.menuTree.submenus.length; i++)
                            for (var j = 0; j < $scope.submenuTree.length; j++) $scope.submenuTree[j].id == $scope.menuTree.submenus[i].id && $scope.submenuTree.splice(j, 1)
                }).error(function() {
                    alertify.error("Oops! Please try again")
                }), item.editLoading = !1
            }, 500)
        }, $scope.deleteMenu = function(item) {
            alertify.confirm("Are you sure you want to delete?", function(e) {
                e && menusResource["delete"](item).success(function() {
                    $state.go($state.current, {}, {
                        reload: !0
                    }), alertify.success("Deleted successfully")
                }).error(function() {
                    alertify.error("Oops! Please try again")
                })
            })
        };
        var toUTCDate = function(date) {
                var _utc = new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(), date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds());
                return _utc
            },
            millisToUTCDate = function(millis) {
                return toUTCDate(new Date(millis))
            };
        $scope.toUTCDate = toUTCDate, $scope.millisToUTCDate = millisToUTCDate
    });