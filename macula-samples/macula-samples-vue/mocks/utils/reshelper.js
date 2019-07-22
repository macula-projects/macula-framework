const { parse } = require('url')

function toResponse (openapi, content, req, res, u, b) {
  let result = null
  if (openapi) {
    if (content instanceof Array) {
      let url = u
      if (!url || Object.prototype.toString.call(url) !== '[object String]') {
        url = req.url // eslint-disable-line
      }

      // 提取URL参数
      const params = parse(url, true).query

      // 排序
      if (params.sort) {
        const s = params.sort.split(',')
        content = content.sort((prev, next) => {
          if (s[s.length - 1] === 'DESC') {
            return next[s[0]] > prev[s[0]] ? 1 : -1
          }
          return prev[s[0]] > next[s[0]] ? 1 : -1
        })
      }

      // 分页
      let size = 10
      if (params.rows) {
        size = parseInt(params.rows) || size
      }
      let page = 1
      if (params.page) {
        page = parseInt(params.page) || page
      }

      // 每页数不能大于100，页码不能小于1，不能大于总页数
      const total = content.length
      size = size <= 0 ? total : size
      size = size < 100 ? size : 100
      page = page >= 1 ? page : 1
      page = page <= Math.ceil(total / size) ? page : Math.ceil(total / size)
      let begin = page - 1 < 0 ? 0 : (page - 1) * size
      let end = page * size <= total ? page * size : total

      content = content.slice(begin, end)

      result = {
        success: true,
        content: content,
        size: size,
        number: page - 1,
        totalElements: total
      }
    } else {
      result = {
        success: true,
        returnObject: content
      }
    }
  } else {
    result = content
  }

  if (res && res.json) {
    res.json(result)
  } else {
    return result
  }
}

function toError () {
  return {
    success: false,
    errorCode: 'error',
    errorMessage: '出错了',
    exceptionCode: 'error',
    exceptionMessage: '出异常了'
  }
}

module.exports = {
  toResponse,
  toError
}
