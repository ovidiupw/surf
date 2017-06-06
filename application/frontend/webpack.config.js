const webpack = require('webpack');
const path = require('path');
const merge = require('webpack-merge');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const PATHS = {
  app: path.join(__dirname, 'src'),
  node_modules: path.join(__dirname, 'node_modules'),
  generated: path.join(__dirname, 'generated'),
  build: path.join(__dirname, 'build'),
  api_gateway_sdk: path.join(__dirname, 'generated/sdks/api-gateway-js-sdk/apiGateway-js-sdk'),
  static: path.join(__dirname, 'static')
};

const common = {
  resolve: {
    modules: [
      path.resolve(PATHS.app),
      path.resolve(PATHS.node_modules),
      path.resolve(PATHS.generated)
    ]
  },
  entry: {
    'app': path.join(PATHS.app, 'app.js')
  },
  output: {
    filename: '[name].js',
    path: PATHS.build
  },
  module: {
    loaders: [
      {
        test: /\.json$/,
        loaders: ['json-loader']
      }, {
        // Test expects a RegExp! Note the slashes!
        test: /\.css$/,
        loaders: ['style-loader', 'css-loader']
      }, {
        test: /\.jsx?$/,
        // Enable caching for improved performance during development
        // It uses default OS directory by default. If you need something
        // more custom, pass a path to it. I.e., babel?cacheDirectory=<path>
        loaders: ['babel-loader?cacheDirectory'],
        // Parse only app files! Without this it will go through entire project.
        // In addition to being slow, that will most likely result in an error.
        include: [PATHS.app, PATHS.generated]
      }, {
        test: /\.(woff|woff2)$/,
        loader: "url-loader?limit=10000&mimetype=application/font-woff"
      }, {
        test: /\.ttf$/,
        loader: "file-loader"
      }, {
        test: /\.eot$/,
        loader: "file-loader"
      }, {
        test: /\.svg$/,
        loader: "file-loader"
      }
    ]
  }
};

const environments = {
  development: merge(common, {
    plugins: [
      new webpack.HotModuleReplacementPlugin(),
      new HtmlWebpackPlugin({
        title: 'Surf - Crawling in the cloud',
        template: 'static/index.ejs'
      })
    ],
    devServer: {
      contentBase: PATHS.build,

      // Enable history API fallback so HTML5 History API based
      // routing works. This is a good default that will come
      // in handy in more complicated setups.
      historyApiFallback: true,
      hot: true,
      inline: true,

      // Display only errors to reduce the amount of output.
      // stats: 'errors-only',

      // Parse host and port from env so this is easy to customize.
      // 0.0.0.0 is available to all network devices unlike default
      // localhost
      host: process.env.HOST,
      port: 3000
    }
  }),

  production: merge(common, {
    plugins: [
      new webpack.HotModuleReplacementPlugin(),
      new HtmlWebpackPlugin({
        title: 'Surf - Crawling in the cloud',
        template: 'static/index.ejs'
      }),
      new webpack.optimize.UglifyJsPlugin({minimize: true})
    ]
  })
};

const TARGET = process.env.npm_lifecycle_event;
let moduleToExport = undefined;

if (TARGET === 'dev' || !TARGET) {
  moduleToExport = environments.development;
} else if (TARGET === 'build') {
  moduleToExport = environments.production;
}

module.exports = environments[process.env.NODE_ENV] || moduleToExport;
