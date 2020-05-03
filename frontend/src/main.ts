import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import { store, vxm } from './store'
import axios from 'axios'
import { login } from './store/modules/UserStore'

Vue.config.productionTip = false

axios.defaults.baseURL = store.state.baseUrl

const vue = new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
})

axios.interceptors.request.use(function(config) {
  login()

  config.auth = {
    username: vxm.userModule.name!,
    password: vxm.userModule.password!
  }
  return config
})

vue.$mount('#app')
