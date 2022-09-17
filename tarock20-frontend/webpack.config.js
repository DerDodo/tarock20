const path = require('path');
const CopyPlugin = require('copy-webpack-plugin');
module.exports = {
  entry: './src/index.tsx',
  devtool: 'source-map',
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: "ts-loader",
        exclude: /node_modules/,
      },
      {
        test: /\.css$/i,
        use: ["style-loader", "css-loader"],
      },
    ],
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js'],
  },
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist'),
    publicPath: '/'
  },
  plugins: [
    new CopyPlugin({
      patterns: [
        {from: 'public'}
      ]
    })
  ],
  devServer: {
    historyApiFallback: true,
    static: path.join(__dirname, 'dist'),
    port: 9002,
    proxy: {
      '/api': {
           target: 'http://localhost:9002',
           router: () => 'http://localhost:8080',
           logLevel: 'debug',
           ws: true,
      }
   },
  },
  mode: 'development',
};