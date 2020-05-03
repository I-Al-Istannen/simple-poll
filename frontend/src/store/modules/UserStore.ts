import { createModule, mutation } from 'vuex-class-component'
import { mdiLogin } from '@mdi/js'
import { vxm } from '..'

const VxModule = createModule({
  namespaced: 'userModule',
  strict: false
})

// from: https://stackoverflow.com/a/2117523
function uuidv4(): string {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = (Math.random() * 16) | 0,
      v = c == 'x' ? r : (r & 0x3) | 0x8
    return v.toString(16)
  })
}

export function login() {
  if (!vxm.userModule.isLoggedIn) {
    vxm.userModule.logIn({ name: 'Max Musterfrau', password: uuidv4() })
  }
}

export class UserStore extends VxModule {
  name = ''
  password = ''

  @mutation
  logIn(payload: { name: string; password: string }) {
    this.name = payload.name
    this.password = payload.password
  }

  get isLoggedIn(): boolean {
    return this.name !== '' && this.password !== ''
  }
}
