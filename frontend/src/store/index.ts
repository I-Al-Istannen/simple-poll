import Vue from 'vue'
import Vuex from 'vuex'
import { extractVuexModule, createProxy } from 'vuex-class-component'
import { UserStore } from './modules/UserStore'
import { PollStore } from './modules/PollStore'
import VuexPersistence from 'vuex-persist'

interface RootState {
  baseUrl: string
  userModule: UserStore
  pollModule: PollStore
}

Vue.use(Vuex)

const persistenceLocalStorage = new VuexPersistence<RootState>({
  storage: window.localStorage,
  modules: ['userModule']
})

export const store = new Vuex.Store({
  state: {
    baseUrl: process.env.VUE_APP_BASE_URL
  } as RootState,
  modules: {
    ...extractVuexModule(UserStore),
    ...extractVuexModule(PollStore)
  },
  plugins: [persistenceLocalStorage.plugin]
})

export const vxm = {
  userModule: createProxy(store, UserStore),
  pollModule: createProxy(store, PollStore)
}
