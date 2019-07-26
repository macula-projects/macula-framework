const fs = require('fs')
const glob = require('glob')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const WebpackCdnPlugin = require('webpack-cdn-plugin')
const CompressionPlugin = require('compression-webpack-plugin')

// 实际的CDN地址
const cdnUrl = '//cdn.staticfile.org'

function getPages () {
  const pages = {}
  let entries
  try {
    entries = glob('src/modules/*/*.js', { sync: true })
  } catch (err) {
    entries = []
    throw err
  }
  entries.forEach((file) => {
    const fileSplit = file.split('/')
    const pageName = fileSplit[2]
    let pageHtml = fileSplit.slice(0, 3).join('/') + '/index.html'
    if (!fs.existsSync(pageHtml)) {
      pageHtml = 'public/index.html'
    }
    pages[pageName] = {
      entry: file,
      template: pageHtml,
      filename: `${pageName}.html`
    }
  })
  return pages
}

module.exports = {
  pages: getPages(),
  publicPath: process.env.NODE_ENV === 'production' ? cdnUrl + '/' + require('./package.json').name : '/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV !== 'production',
  productionSourceMap: process.env.NODE_ENV !== 'production',
  css: {
    extract: true,
    sourceMap: false,
    loaderOptions: {}
  },
  parallel: require('os').cpus().length > 1,
  pluginOptions: {},
  chainWebpack: (config) => {
    const env = process.env.NODE_ENV
    const profile = process.env.VUE_APP_PROFILE
    console.log(`当前环境: NODE_ENV=${env}，PROFILE=${profile}`)

    config.resolve.alias
      .set('@assets', '@/assets')
      .set('@components', '@/components')
  },
  configureWebpack: (config) => {
    // 生产环境开始gzip压缩
    if (process.env.NODE_ENV === 'production') {
      config.plugins.push(
        new CompressionPlugin({
          test: /\.js$|\.html$|.\css/, // 匹配文件名
          threshold: 10240, // 对超过10k的数据压缩
          deleteOriginalAssets: false // 不删除源文件
        })
      )
    }

    // 生产获取远程CDN，非生产获取本地node_modules
    config.plugins.push(
      new WebpackCdnPlugin({
        modules: [
          {
            var: 'Vue',
            name: 'vue',
            version: '2.6.10',
            path: 'vue.min.js'
          },
          {
            var: 'VueRouter',
            name: 'vue-router',
            version: '3.0.3',
            path: 'vue-router.min.js'
          }
        ],
        prodUrl: cdnUrl + '/:name/:version/:path'
      })
    )
  },
  devServer: {
    open: false,
    disableHostCheck: true,
    host: '0.0.0.0',
    port: 8000,
    https: false,
    hotOnly: false,
    // proxy: {
    //   '/api': {
    //     target: 'http://localhost:8082',
    //     changeOrigin: true
    //   }
    // },
    before: (app) => {
    }
  }
}
