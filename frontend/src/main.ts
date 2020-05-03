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

// from: https://stackoverflow.com/a/2117523
function uuidv4(): string {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = (Math.random() * 16) | 0,
      v = c == 'x' ? r : (r & 0x3) | 0x8
    return v.toString(16)
  })
}

vxm.userModule.name = 'Max Musterfrau'
vxm.userModule.password = uuidv4()

axios.interceptors.request.use(function(config) {
  config.auth = {
    username: vxm.userModule.name!,
    password: vxm.userModule.password!
  }
  return config
})

vue.$mount('#app')
