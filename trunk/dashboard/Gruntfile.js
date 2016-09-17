module.exports = function(grunt) {

  dirsConfig = {
    app: "/",
    dist: "public"
  };

  filesConfig = {
    vendor: [
      'vendor/modernizr/modernizr.js',
      'vendor/jquery/dist/jquery.js',
      'vendor/angular/angular.js',
      'vendor/angular-resource/angular-resource.js',
      'vendor/angular-cookies/angular-cookies.js',
      'vendor/angular-sanitize/angular-sanitize.js',
      'vendor/angular-route/angular-route.js',
      'vendor/angular-animate/angular-animate.js',
      'vendor/bootstrap/dist/js/bootstrap.js',
      'vendor/angular-bootstrap/ui-bootstrap-tpls.js',
      'vendor/angular-ui-router/release/angular-ui-router.min.js',
      'vendor/angular-ui-calendar/src/calendar.js',
      'vendor/angular-ui-tree/dist/angular-ui-tree.js',
      'vendor/angular-ui-select2/src/select2.js',
      'vendor/select2/select2.js',
      'vendor/icheck/icheck.min.js',
      'vendor/gmaps/gmaps.js',
      'vendor/momentjs/moment.js',
      'vendor/flow.js/dist/flow.js',
      'vendor/ng-flow/dist/ng-flow.js',
      'vendor/enquire/dist/enquire.js',
      'vendor/ui-select/dist/select.js',
      'vendor/jquery.inputmask/dist/jquery.inputmask.bundle.js',
      'vendor/alertify/alertify.min.js',
      'vendor/fullcalendar-2.2.7/fullcalendar.js',
      'vendor/bootstrap-daterangepicker/moment.js',
      'vendor/bootstrap-daterangepicker/daterangepicker.js',
      'vendor/ng-table-0.4.3/ng-table.js',
      'vendor/angular-daterangepicker/angular-daterangepicker.js',
      'vendor/angular-loading-bar/loading-bar.js',
      'vendor/ladda/spin.min.js',
      'vendor/ladda/ladda.min.js',
      'vendor/angular-ladda/angular-ladda.js',
      'vendor/angular-payments/lib/angular-payments.js',
      'vendor/angular-moment/angular-payments.js',
      'vendor/angular-moment/angular-moment.js'
    ],
    main: [
      'scripts/common/theme/shared/Services.js',
      'scripts/common/theme/shared/Directives.js',
      'scripts/common/theme/maps/GoogleMaps.js',
      'scripts/common/services/services.js',
      'scripts/common/directives/directives.js',
      'scripts/common/filters/filters.js',
      'scripts/common/config/config.js',
      'scripts/common/security/service/security-service.js',
      'scripts/common/security/controllers/login.js',
      'scripts/common/security/controllers/signup.js',
      'scripts/common/security/security-router.js',

      'scripts/owner/menu/services/menus-service.js',
      'scripts/owner/menu/controllers/categories.js',
      'scripts/owner/menu/controllers/items.js',
      'scripts/owner/menu/controllers/menus.js',
      'scripts/owner/menu/controllers/submenu.js',
      'scripts/owner/menu/controllers/modifiers.js',
      'scripts/owner/menu/controllers/sizes.js',
      'scripts/owner/menu/controllers/addon.js',
      'scripts/owner/menu/menu-router.js',
      'scripts/owner/restaurant/services/restaurant-service.js',
      'scripts/owner/restaurant/controllers/zone.js',
      'scripts/owner/restaurant/controllers/notifications.js',
      'scripts/owner/restaurant/controllers/order.js',
      'scripts/owner/restaurant/controllers/info.js',
      'scripts/owner/restaurant/controllers/subscriptions.js',
      'scripts/owner/restaurant/restaurant.js',
      'scripts/owner/order/services/order-service.js',
      'scripts/owner/order/filters/order-filter.js',
      'scripts/owner/order/order.js',
      'scripts/owner/owner.js',

      'scripts/admin/restaurant/manage/menu/menu.js',
      'scripts/admin/restaurant/manage/menu/services/menus-service.js',
      'scripts/admin/restaurant/manage/menu/controllers/items.js',
      'scripts/admin/restaurant/manage/menu/controllers/categories.js',
      'scripts/admin/restaurant/manage/menu/controllers/menus.js',
      'scripts/admin/restaurant/manage/menu/controllers/submenu.js',
      'scripts/admin/restaurant/manage/menu/controllers/sizes.js',
      'scripts/admin/restaurant/manage/menu/controllers/addon.js',
      'scripts/admin/restaurant/manage/menu/controllers/modifiers.js',
      'scripts/admin/restaurant/manage/restaurant/restaurant.js',
      'scripts/admin/restaurant/manage/restaurant/services/restaurant-service.js',
      'scripts/admin/restaurant/manage/restaurant/controllers/zone.js',
      'scripts/admin/restaurant/manage/restaurant/controllers/info.js',
      'scripts/admin/restaurant/manage/restaurant/controllers/taxes.js',
      'scripts/admin/restaurant/manage/restaurant/controllers/subscriptions.js',
      'scripts/admin/restaurant/manage/restaurant/controllers/order.js',
      'scripts/admin/restaurant/manage/order/services/order-service.js',
      'scripts/admin/restaurant/manage/order/order.js',
      'scripts/admin/restaurant/manage/manage.js',
      'scripts/admin/restaurant/services/restaurant-service.js',
      'scripts/admin/restaurant/directives/restaurant-directive.js',
      'scripts/admin/restaurant/restaurant.js',
      'scripts/admin/user/manage/manage.js',
      'scripts/admin/user/services/user-service.js',
      'scripts/admin/user/user.js',
      'scripts/admin/plan/services/plan-service.js',
      'scripts/admin/plan/controllers/plan.js',
      'scripts/admin/plan/plan.js',
      'scripts/admin/order/services/order-service.js',
      'scripts/admin/order/order.js',
      'scripts/admin/admin.js',
      'scripts/app.js'
    ]
  };

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    yeoman: dirsConfig,
    js_files: filesConfig,
    less: {
      development: {
        options: {
          paths: ["assets/css/"]
        },
        files: {
          "assets/css/main.css": "assets/less/styles.less"
        }
      },
    },
    cssmin: {
      options: {
        shorthandCompacting: false,
        roundingPrecision: -1,
        keepSpecialComments: 0
      },
      target: {
        files: [
          // 'assets/css/main.min.css': ['assets/css/main.css']
          // '<%= yeoman.dist %>/css/styles.min.css': ['<%= yeoman.dist %>/css/styles.css']
          // 'select2.min.css': ['select2.css']
          // 'vendor/alertify/alertify.min.css': ['vendor/alertify/alertify.css']
          {
            expand: true,
            flatten: true,
            dest: "<%= yeoman.dist %>/assets/css",
            src: ["<%= yeoman.dist %>/assets/css/styles.css"],
            ext: ".min.css"
          }, {
            expand: true,
            flatten: true,
            dest: "<%= yeoman.dist %>/assets/css",
            src: ["<%= yeoman.dist %>/assets/css/main.css"],
            ext: ".min.css"
          }
        ]
      }
    },
    clean: {
      dist: {
        files: [
          {
            dot: true,
            src: ["<%= yeoman.dist %>/*", "!<%= yeoman.dist %>/.git*"]
          }
        ]
      }
    },
    copy: {
      dist: {
        files: [
          {
            expand: true,
            flatten: true,
            dest: "<%= yeoman.dist %>",
            src: ["favicon.png", "build/index.html"]
          }, {
            expand: true,
            dest: "<%= yeoman.dist %>",
            src: ["assets/fonts/**/*"]
          }, {
            expand: true,
            dest: "<%= yeoman.dist %>",
            src: "assets/images/*"
          }, {
            expand: true,
            dest: "<%= yeoman.dist %>",
            src: ["scripts/**/*.html", "!scripts/**/*.js"]
          }, {
            expand: true,
            dest: "<%= yeoman.dist %>",
            src: ["assets/css/**/*"]
          }
        ]
      }
    },
    concat: {
      dist: {
        files: [
          {
            src: '<%= js_files.vendor %>',
            dest: '<%= yeoman.dist %>/js/vendor.js',
          }, {
            src: '<%= js_files.main %>',
            dest: '<%= yeoman.dist %>/js/main.js',
          },
        ]
      },
      styles: {
        files: [
          {
            src: [
              'vendor/**/*.min.css',
              'vendor/icheck/**/*.css',
              'vendor/bootstrap-daterangepicker/daterangepicker-bs3.css',
              'assets/styles/*.css',
            ],
            dest: '<%= yeoman.dist %>/assets/css/styles.css'
          }
        ]
      }
    },
    uglify: {
      options: {
        report: 'min',
        mangle: false,
        compress: {
          drop_console: true
        }
      },
      dist: {
        files: [
          {
            dest: "<%= yeoman.dist %>/js/main.min.js",
            src: "<%= yeoman.dist %>/js/main.js"
          },
          {
            dest: "<%= yeoman.dist %>/js/vendor.min.js",
            src: "<%= yeoman.dist %>/js/vendor.js"
          }
        ]
      }
    },

    // Grunt express - our webserver
    // https://github.com/blai/grunt-express
    express: {
        all: {
            options: {
                bases: ['/home/minh/Projects/dinenow-latest'],
                port: 8080,
                hostname: "0.0.0.0",
                livereload: true
            }
        }
    },

    // grunt-watch will monitor the projects files
    // https://github.com/gruntjs/grunt-contrib-watch
    watch: {
        all: {
                files: ['scripts/**/*.html', 'scripts/**/*.js', 'assets/css/*.css'],
                options: {
                    livereload: true
            }
        }
    },

    // grunt-open will open your browser at the project's URL
    // https://www.npmjs.org/package/grunt-open
    open: {
        all: {
            path: 'http://localhost:<%= express.all.options.port %>'
        }
    },

    jshint: {
      all: ['Gruntfile.js', 'scripts/owner/menu/services/*.js'],
      options: {
        curly: true,
        immed: true,
        newcap: true,
        noarg: true,
        sub: true,
        boss: true,
        eqnull: true
      },
      globals: {}
    }
  });

  // Load the plugin that provides the "uglify" task.
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-express');
  grunt.loadNpmTasks('grunt-open');
  grunt.loadNpmTasks('grunt-contrib-jshint');

  // production
  grunt.registerTask('build', [
    // 'jshint',
    'clean:dist',
    'copy',
    'concat',
    'uglify',
    'cssmin'
  ]);

  // live load
  grunt.registerTask('server', [
    // 'jshint',
    'express',
    'open',
    'watch'
  ]);

  // Default task(s).
  grunt.registerTask('default', ['less', 'watch']);

};