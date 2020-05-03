import Vue from 'vue'
import Vuex from 'vuex'
import { extractVuexModule, createProxy } from 'vuex-class-component'
import { UserStore } from './modules/UserStore'

interface RootState {
  baseUrl: string
  userModule: UserStore
}

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    baseUrl: process.env.VUE_APP_BASE_URL
  } as RootState,
  modules: {
    ...extractVuexModule(UserStore)
  }
})

export const vxm = {
  userModule: createProxy(store, UserStore)
}
