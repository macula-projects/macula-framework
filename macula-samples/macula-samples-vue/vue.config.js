const fs = require('fs')
const glob = require('glob')

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
  baseUrl: '/',
  outputDir: 'dist',
  lintOnSave: true,
  chainWebpack: (config) => {
    // const env = process.env.VUE_APP_ENV
    // console.log(`当前环境: ${env}`)
  },
  productionSourceMap: true,
  css: {
    extract: true,
    sourceMap: false,
    loaderOptions: {}
  },
  parallel: require('os').cpus().length > 1,
  pluginOptions: {},
  devServer: {
    open: false,
    disableHostCheck: true,
    host: '0.0.0.0',
    port: 8000,
    https: false,
    hotOnly: false,
    proxy: null, // string | Object
    before: (app) => {
    }
  }
}
