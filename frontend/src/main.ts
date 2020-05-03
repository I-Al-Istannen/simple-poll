import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import { store, vxm } from './store'
import axios from 'axios'

Vue.config.productionTip = false

axios.defaults.baseURL = store.state.baseUrl

const vue = new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
})

axios.interceptors.request.use(function(config) {
  config.auth = {
    username: vxm.userModule.name!,
    password: vxm.userModule.password!
  }
  return config
})

vue.$mount('#app')
