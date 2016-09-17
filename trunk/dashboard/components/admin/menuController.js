angular.module("app.admin.restaurant.manage.menu.menus", [])
    .controller("AdminMenusController",
        function ($scope, $filter, $state, $stateParams, $modal,
                  ngTableConfig, adminMenusResource, adminCategoriesResource,
                  adminItemsResource, globalHandleUI) {

            globalHandleUI.handleToggleLeftBar();
            $scope.isCollapsed = !1;
            $scope.menuList = [];

            $scope.templeTree = {
                categories: [],
                items: []
            };
            $scope.itemsTree = [];
            $scope.categoriesTree = [];
            $scope.menu = {
                name: "",
                description: "",
                categoryItem: []
            };

            $scope.restaurantID = $stateParams.restaurantId;

            $scope.addTipText = function () {
                var emptyTree = document.getElementsByClassName("angular-ui-tree-empty");
                if (emptyTree) {
                    var p = document.createElement("p");
                    textNode = document.createTextNode("Drag and drop categories and item here.");
                    p.appendChild(textNode);
                    emptyTree[0].appendChild(p);
                    emptyTree[0].className += " empty-tree"
                }
            };

            var getMenuList = function () {
                var restaurantID = $stateParams.restaurantId;
                adminMenusResource.get(restaurantID).success(function (payload) {
                    $scope.menuList = payload.data.menus;
                    $scope.tableParams = ngTableConfig.config($scope.menuList)
                }).error(function () {
                })
            };

            var getCategoryList = function () {
                adminCategoriesResource.getAll($scope.restaurantID).success(function (payload) {
                    $scope.categoriesTree = payload.data.categories;
                    $scope.containers = payload.data.categories;
                    $scope.templeTree.categories = angular.copy(payload.data.categories)
                })
            };

            var getItemList = function () {
                adminItemsResource.get($scope.restaurantID).success(function (payload) {
                    $scope.itemsTree = payload.data.items;
                    $scope.models = payload.data.items;
                    $scope.templeTree.items = angular.copy(payload.data.items)
                })
            };


            $scope.init = function () {
                $scope.isLoadingList = !0;
                setTimeout(function () {
                    getMenuList();
                    $scope.isLoadingList = !1
                }, 300);
            };

            $scope.init();

            $scope.getActiveItem = function (item) {
                return item.id == $scope.menuTree.id ? "item-active" : void 0
            };

                      $scope.resetForm = function () {
                $scope.menuTree = {
                    menuName: "",
                    menuDescription: "",

                };

            };

            $scope.refreshView = function (data) {
                var isMatchId = !1;
                if (data) {
                    for (var i = 0; i < $scope.menuList.length; i++)
                        if ($scope.menuList[i].id == data.id) {
                            isMatchId = !0, $scope.menuList[i] = data, alertify.success("Updated Successfully");
                            break
                        }
                    isMatchId || ($scope.menuList = [], $scope.resetForm(), alertify.success("Created Successfully")), getMenuList()
                }
            };


            $scope.syncObject = function (obj) {
                var temp = {
                    categoryItem: [],
                    category: {
                        id: "",
                        items: []
                    },
                    item: {
                        id: "",
                        price: 0
                    }
                };
                for (var tempObj = angular.copy(obj), i = 0; i < tempObj.categoryItem.length; i++) {
                    temp.category = {
                        id: "",
                        items: []
                    };
                    temp.category.id = tempObj.categoryItem[i].id;
                    for (var itemsObj = tempObj.categoryItem[i].items, j = 0; j < itemsObj.length; j++) {
                        temp.item = {
                            id: "",
                            price: 0
                        };
                    }
                    temp.item.id = itemsObj[j].id;
                    temp.item.price = itemsObj[j].price;
                    temp.category.items.push(temp.item);
                    temp.categoryItem.push(temp.category)
                }
                return tempObj.categoryItem = temp.categoryItem, tempObj.restaurantId = $scope.restaurantID || "", tempObj
            };

            $scope.save = function (menu) {
                //var obj = $scope.syncObject(menu);
                $scope.isLoadingSave = !0;
                setTimeout(function () {
                    adminMenusResource.addOrUpdate(menu).success(function () {
                        $scope.isLoadingSave = !1;
                        $state.go("app.admin.restaurant.manage.menu.menu.list", {}, {
                            reload: !0
                        });
                        alertify.success("Successfully")
                    }).error(function () {
                        alertify.error("Oop! Please try again")
                    })
                }, 500)
            };

            $scope.createMenu = function () {
                $state.go("app.admin.restaurant.manage.menu.menus.create"),
                    setTimeout(function () {
                        $scope.addTipText()
                    }, 100);
                $scope.resetForm()
            };

            $scope.editMenu = function (item) {
                getCategoryList();
                getItemList();
                item.editLoading = !0;
                setTimeout(function () {
                    $state.go("app.admin.restaurant.manage.menu.menus.edit");
                    adminMenusResource.getByID(item).success(function (payload) {
                        $scope.menu = payload.data.menu;
                        $scope.itemsTree = angular.copy($scope.templeTree.items);
                        $scope.categoriesTree = angular.copy($scope.templeTree.categories);
                        for (var i = 0; i < $scope.menu.categoryItem.length; i++) {
                            for (var j = 0; j < $scope.categoriesTree.length; j++) {
                                $scope.categoriesTree[j].id == $scope.menu.categoryItem[i].category.id && $scope.categoriesTree.splice(j, 1);
                            }
                            //for (var j = 0; j < $scope.menu.categoryItem[i].itemPrice.length; j++) {
                            //    for (var k = 0; k < $scope.itemsTree.length; k++) {
                            //        $scope.menu.categoryItem[i].itemPrice[j].item.id == $scope.itemsTree[k].id && $scope.itemsTree.splice(k, 1)
                            //    }
                            //}
                        }
                        item.editLoading = !1
                    }).error(function () {
                        alertify.error("Oop! Please try again")
                    })
                }, 500)
            };

            $scope.dragoverCallback = function (event, index, external, type) {
                //$scope.logListEvent('dragged over', event, index, external, type)
                // Disallow dropping in the third row. Could also be done with dnd-disable-if.
                return index < 10
            };

            $scope.dropCallback = function (event, index, item, external, type, allowedType) {
                $scope.logListEvent('dropped at', event, index, external, type)
                if (external) {
                    if (allowedType === 'itemType' && !item.label) return false
                    if (allowedType === 'containerType' && !angular.isArray(item))
                        return false

                }
                return item
            };

            $scope.addCategory = function (event, index, item, external, type, allowedType) {
                if(type === allowedType ) {
                    var newCategory = {
                        category: angular.copy(item),
                        items: []
                    };
                    $scope.menu.categoryItem.splice(index, 0, newCategory);
                    $scope.categoriesTree.splice(index, 1)
                }
                return false;

            };

            $scope.addItem = function (event, index, item, external, type, allowedType, categoryItem) {
                var categoryItemIndex = $scope.menu.categoryItem.indexOf(categoryItem);
                if(categoryItemIndex!==(-1)) {
                    $scope.menu.categoryItem[categoryItemIndex].items.splice(index, 0, angular.copy(item));
                }
                return false;
            };

            $scope.removeCategory = function(index, categoryItem) {
                var categoryItemIndex = $scope.menu.categoryItem.indexOf(categoryItem);
                if(categoryItemIndex!==(-1)) {
                    var numOfItemPrice = $scope.menu.categoryItem[categoryItemIndex].itemPrice.length
                    if(numOfItemPrice === 0) {
                        $scope.menu.categoryItem.splice(categoryItemIndex, 1);
                        $scope.categoriesTree.push(categoryItem.category)
                    }
                    else {
                        alertify.confirm("Are you sure you want to delete this category?<br/><br />" +
                            "<b>Warning:</b> All items in the category will be removed.", function (e) {
                            $scope.categoriesTree.push(categoryItem.category)
                            $scope.menu.categoryItem.splice(index, 1);
                        })
                    }
                }

                //var temp = $scope.menu.categoryItem[categoryIndex].item[itemIndex];

            };

            $scope.removeItemFromCategory = function(categoryItem, item) {
                var categoryItemIndex = $scope.menu.categoryItem.indexOf(categoryItem);
                if(categoryItemIndex!==(-1)) {
                    var itemIndex = $scope.menu.categoryItem[categoryItemIndex].items.indexOf(item);
                    if(itemIndex!==(-1)) {
                        $scope.menu.categoryItem[categoryItemIndex].items.splice(itemIndex, 1);
                        //$scope.itemsTree.push(itemPrice.item);
                    }
                }

                //var temp = $scope.menu.categoryItem[categoryIndex].item[itemIndex];

            };

            $scope.dragoverCategoryItemCallback = function (event, index, external, type) {
                $scope.draggingAtIndex = index;
                return index < 10
            };

            $scope.logEvent = function (message, event) {
                console.log(message, '(triggered by the following', event.type, 'event)')
                console.log(event)
            };



            $scope.logListEvent = function (action, event, index, external, type) {
                var message = external ? 'External ' : ''
                message += type + ' element is ' + action + ' position ' + index
                $scope.logEvent(message, event)
            };


            $scope.deleteMenu = function (item) {
                alertify.confirm("Are you sure you want to delete?", function (e) {
                    e && adminMenusResource["delete"](item).success(function () {
                        $state.go($state.current, {}, {
                            reload: !0
                        }), alertify.success("Deleted successfully")
                    }).error(function () {
                        alertify.error("Oops! Please try again")
                    })
                })
            };

            var toUTCDate = function (date) {
                    var _utc = new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(), date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds());
                    return _utc
                },
                millisToUTCDate = function (millis) {
                    return toUTCDate(new Date(millis))
                };
            $scope.toUTCDate = toUTCDate, $scope.millisToUTCDate = millisToUTCDate

            $scope.initCalendar = function () {
                $scope.uiConfig = {
                    calendar: {
                        header: {
                            left: "",
                            center: "",
                            right: ""
                        },
                        defaultView: "agendaWeek",
                        editable: !1,
                        eventLimit: !0,
                        firstDay: 1,
                        allDaySlot: !1,
                        eventClick: function (calEvent) {
                            alertify.confirm("Delete the event " + calEvent.title, function (e) {
                                if (e) {
                                    for (var i = 0; i < $scope.events.length; i++) $scope.events[i].id === calEvent.id && $scope.events.splice(i, 1);
                                    for (var i = 0; i < $scope.menuList.length; i++)
                                        if ($scope.menuList[i].menuHours)
                                            for (var j = 0; j < $scope.menuList[i].menuHours.length; j++) $scope.menuList[i].menuHours[j].id === calEvent.id && $scope.menuList[i].menuHours.splice(j, 1)
                                }
                            })
                        }
                    }
                };
                $scope.events = [];
                $scope.eventSources = [$scope.events]
            };

            $scope.initCalendar();
            $scope.setHourMenu = function (item) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/admin/restaurant/manage/menu/views/modal/menu-hours.html",
                    resolve: {
                        itemToProcess: function () {
                            return item
                        }
                    },
                    size: "lg",
                    controller: function ($scope, $q, $modalInstance, itemToProcess, adminMenusResource) {
                        var getHours = function (itemToProcess) {
                            var deferred = $q.defer();
                            return setTimeout(function () {
                                var data = angular.copy(itemToProcess) || {};
                                data && deferred.resolve(data)
                            }, 300), deferred.promise
                        };
                        $scope.init = function (data) {
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
                            }, $scope.commonMethods = function () {
                                return {
                                    getDateByIndex: function (dayIndex) {
                                        var date = moment().startOf("week").add("days", dayIndex);
                                        return date.format("YYYY-MM-DD")
                                    },
                                    getDateByISO8601: function (date, time) {
                                        return date + "T" + time + ":00Z"
                                    },
                                    getEndTimes: function (startTime) {
                                        for (var strings = startTime.split(":"), startAt = 1 * strings[0], endTime = [], tempStr = "", i = startAt; 23 > i; i++) startAt++, tempStr = startAt > 9 ? startAt + ":00" : "0" + startAt + ":00", endTime.push(tempStr);
                                        return endTime
                                    },
                                    getUniqueID: function () {
                                        return "_" + Math.random().toString(36).substr(2, 9)
                                    },
                                    synOjectWithCalendar: function (hours) {
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
                                    checkDateInWeek: function (date) {
                                        var monday = moment().startOf("week").add("days", 0).format("YYYY-MM-DD"),
                                            sunday = moment().startOf("week").add("days", 7).format("YYYY-MM-DD"),
                                            isInWeek = moment(date).isBefore(sunday) && moment(date).isAfter(monday);
                                        return isInWeek
                                    },
                                    checkIsSameDate: function (date, events) {
                                        for (var dateAdd = moment(date).format("YYYY-MM-DD"), i = 0; i < events.length; i++) {
                                            var eventDate = moment(events[i].start).format("YYYY-MM-DD"),
                                                isSameDate = moment(dateAdd).isSame(eventDate);
                                            if (isSameDate) return !0
                                        }
                                        return !1
                                    }
                                }
                            }, $scope.initEndTime = function (startTime) {
                                $scope.datetimes.endTimes = $scope.commonMethods().getEndTimes(startTime)
                            }, $scope.addEvents = function (params) {
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
                                        // id: objEvent.id,
                                        weekDayType: $scope.datetimes.days[params.weekDayType - 1].label,
                                        fromTime: startTime,
                                        toTime: endTime
                                    };
                                params.weekDayType = "", params.fromTime = "", params.toTime = "";
                                var isSameDate = $scope.commonMethods().checkIsSameDate(startTime, $scope.events);
                                isSameDate ? alertify.error("This day was set hours! Please set the hour on another day of weeks") : ($scope.itemToProcess.hours.push(tempObject), $scope.events.push(objEvent))
                            }, $scope.onClearEvents = function () {
                                alertify.confirm("Are you sure want to delete all time ?", function (e) {
                                    if (e) {
                                        $scope.itemToProcess.hours = [];
                                        for (var i = $scope.events.length - 1; i >= 0; i--) $scope.events.splice(i, 1)
                                    }
                                })
                            }, $scope.deleteEvents = function (index) {
                                $scope.itemToProcess.hours.splice(index, 1);
                                for (var i = 0; i < $scope.events.length; i++)
                                    for (var j = 0; j < $scope.itemToProcess.hours.length; j++)
                                        if ($scope.events[i].id == $scope.itemToProcess.hours[j].id) {
                                            $scope.events.splice(i, 1);
                                            break
                                        }
                            },
                                $scope.filterDateInWeek = function () {
                                    if ($scope.itemToProcess.hours.length > 0) {
                                        for (var tempHours = angular.copy($scope.itemToProcess.hours), i = 0; i < tempHours.length; i++) {
                                            var startTime = moment(tempHours[i].fromTime).format("YYYY-MM-DD"),
                                                isDateInWeek = $scope.commonMethods().checkDateInWeek(startTime);
                                            isDateInWeek || tempHours.splice(i, 1)
                                        }
                                        $scope.itemToProcess.hours = tempHours
                                    }
                                }, $scope.filterDateInWeek()
                        },
                            $scope.initCalendar = function () {
                                $scope.isLoadedCalendar = !0, $scope.getEvents = function (start, end, timezone, callback) {
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
                                        defaultView: "weekDay",
                                        editable: !1,
                                        eventLimit: !0,
                                        firstDay: 1,
                                        allDaySlot: !1,
                                        fixedWeekCount: !1,
                                        eventClick: function (calEvent) {
                                            alertify.confirm("Are you sure want to delete this time ?", function (e) {
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
                            }, $scope.save = function () {
                            adminMenusResource.addOrUpdate($scope.itemToProcess).success(function () {
                                $modalInstance.close($scope.itemToProcess)
                            }).error(function () {
                                alertify.error("Oops! Please try again")
                            })
                        }, $scope.cancel = function () {
                            $modalInstance.dismiss("cancel")
                        };
                        var promise = getHours(itemToProcess);
                        promise.then(function (data) {
                            $scope.init(data), $scope.initCalendar()
                        }, function (error) {
                        })
                    }
                });
                modalInstance.result.then(function (data) {
                    data && (alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                        reload: !0
                    }))
                })
            };
        });