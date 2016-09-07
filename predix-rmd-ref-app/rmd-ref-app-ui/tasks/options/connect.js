var auth = require('../helpers/auth');
var proxy = require('../helpers/proxy');

var config = {
  /**
   * --------- ADD YOUR UAA CONFIGURATION HERE ---------
   * If you have run the installRefApp.py script, then you can copy values from the manifest.yml into this file for local development.
   * This uaa helper object simulates NGINX uaa integration using Grunt allowing secure cloudfoundry service integration in local development without deploying your application to cloudfoundry.
   * Please update the following uaa configuration for your solution
   */
    uaa: {

        clientId: 'app_client_id',
        serverUrl: 'https://6e13f008-7e1e-49b7-8699-21cf6ff03506.predix-uaa.run.aws-usw02-pr.ice.predix.io',
        defaultClientRoute: '/about',
        base64ClientCredential: 'YXBwX2NsaWVudF9pZDpzZWNyZXQ='

    },
  /**
   * --------- ADD YOUR SECURE ROUTES HERE ------------
   *
   * Please update the following object add your secure routes
   */

    proxy: {
        '/api/asset': {
            url: 'https://predix-asset.run.aws-usw02-pr.ice.predix.io',
            instanceId: '4155369a-0abe-4311-afdd-09b066e23caa',
            pathRewrite: { '^/api/': '/'}
        },
        '/api/group': {
            url: 'https://predix-asset.run.aws-usw02-pr.ice.predix.io',
            instanceId: '4155369a-0abe-4311-afdd-09b066e23caa',
            pathRewrite: { '^/api/': '/'}
        },
        '/api/v1/datapoints': {
            url: 'https://time-series-store-predix.run.aws-usw02-pr.ice.predix.io/v1/datapoints',
            instanceId: '7afa2d5d-94a1-4085-ad77-c41e9106a432',
            pathRewrite: { '^/api/v1/datapoints': ''}
        },
        '/api/datagrid': {
            url: 'https://tcs-smart-welding-rmd-datasource.run.aws-usw02-pr.ice.predix.io',
            instanceId: null,
            pathRewrite: { '^/api/': '/services/experience/datasource/'}
        }
    }

};

// a middleware function to simulate a path that returns an nginx environment variable:
var environment = function(req, res, next) {
    if (req.originalUrl.indexOf('getWsUrl') >= 0) {
        res.setHeader('Content-Type', 'application/json');
        res.end(JSON.stringify({wsUrl: 'wss://tcs-smart-welding-websocket-server.run.aws-usw02-pr.ice.predix.io'}));
    } else {
        next();
    }
};

module.exports = {
    server: {
        options: {
            port: 9000,
            base: 'public',
            open: true,
            hostname: 'localhost',
            middleware: function (connect, options) {
                var middlewares = [];

                //add predix services proxy middlewares
                middlewares = middlewares.concat(proxy.init(config.proxy));

                //add predix uaa authentication middlewaress
                middlewares = middlewares.concat(auth.init(config.uaa));

                middlewares = middlewares.concat(environment);

                if (!Array.isArray(options.base)) {
                    options.base = [options.base];
                }

                var directory = options.directory || options.base[options.base.length - 1];
                options.base.forEach(function (base) {
                    // Serve static files.
                    middlewares.push(connect.static(base));
                });

                // Make directory browse-able.
                middlewares.push(connect.directory(directory));

                return middlewares;
            }
        }
    }
};
