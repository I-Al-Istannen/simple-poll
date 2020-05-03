import Vue from 'vue'
import Vuex from 'vuex'
import { extractVuexModule, createProxy } from 'vuex-class-component'
import { UserStore } from './modules/UserStore'
import { PollStore } from './modules/PollStore'

interface RootState {
  baseUrl: string
  userModule: UserStore
  pollModule: PollStore
}

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    baseUrl: process.env.VUE_APP_BASE_URL
  } as RootState,
  modules: {
    ...extractVuexModule(UserStore),
    ...extractVuexModule(PollStore)
  }
})

export const vxm = {
  userModule: createProxy(store, UserStore),
  pollModule: createProxy(store, PollStore)
}
