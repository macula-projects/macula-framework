const IndexIndex = () =>
  import(/* webpackChunkName: "index-index" */ '@/modules/index/views/index.vue')
const IndexAbout = () =>
  import(/* webpackChunkName: "index-about" */ '@/modules/index/views/about.vue')

export default [
  {
    name: 'index-index',
    path: '',
    component: IndexIndex
  },
  {
    name: 'index-about',
    path: 'about',
    component: IndexAbout,
    meta: {
      requireAuth: true
    }
  }
]
