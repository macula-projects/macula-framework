const assert = require('assert')
const glob = require('glob')
const reshelper = require('./reshelper')

function parseKey (key) {
  let method = 'get'
  let path = key
  let openapi = false

  if (key.indexOf(' ') > -1) {
    const splited = key.split(' ')
    method = splited[0].toLowerCase()
    path = splited[1]
    if (splited.length > 2) {
      if (splited[2] === 'openapi') {
        openapi = true
      }
    }
  }

  return { method, path, openapi }
}

function createMockHandler (method, path, openapi, value) {
  return function mockHandler (...args) {
    if (typeof value === 'function') {
      reshelper.toResponse(openapi, value(...args), ...args)
    } else {
      reshelper.toResponse(openapi, value, ...args)
    }
  }
}

function init (server) {
  glob.sync('./src/modules/*/mock/index.js').forEach(f => {
    const config = require('../.' + f)
    Object.keys(config).forEach(key => {
      const keyParsed = parseKey(key)
      assert(!!server[keyParsed.method], `method of ${key} is not valid`)
      assert(
        typeof config[key] === 'function' || typeof config[key] === 'object',
        `mock value of ${key} should be function or object, but got ${typeof config[key]}`
      )

      server[keyParsed.method](
        keyParsed.path,
        createMockHandler(keyParsed.method, keyParsed.path, keyParsed.openapi, config[key])
      )
    })
  })
}

module.exports = {
  init
}
