import { createModule } from 'vuex-class-component'

const VxModule = createModule({
  namespaced: 'userModule',
  strict: false
})

export class UserStore extends VxModule {
  name: string | undefined
  password: string | undefined
}
