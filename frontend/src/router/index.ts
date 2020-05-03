import Vue from 'vue'
import VueRouter, { RouteConfig, RouterOptions } from 'vue-router'
import Home from '../views/Home.vue'
import Create from '../views/Create.vue'
import Results from '../views/ViewResult.vue'
import Vote from '../views/Vote.vue'
import PollGroups from '../views/PollGroups.vue'
import NotFound404 from '../views/NotFound404.vue'
import { mdiHome, mdiPoll, mdiStackOverflow } from '@mdi/js'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home',
    meta: {
      navigable: false,
      label: 'Home'
    }
  },
  {
    // Redirect / to /home
    path: '/home',
    name: 'home',
    component: Home,
    meta: {
      label: 'Home',
      navigable: true,
      icon: mdiHome
    }
  },
  {
    path: '/create',
    name: 'create-poll',
    component: Create,
    meta: {
      label: 'Create a poll',
      navigable: true,
      icon: mdiPoll
    }
  },
  {
    path: '/results/:pollId',
    name: 'view-results',
    component: Results,
    meta: {
      label: 'View poll results',
      navigable: false,
      icon: mdiStackOverflow
    }
  },
  {
    path: '/vote/:pollId',
    name: 'vote',
    component: Vote,
    meta: {
      label: 'Vote on a poll',
      navigable: false,
      icon: mdiStackOverflow
    }
  },
  {
    path: '/my-poll-groups',
    name: 'my-poll-groups',
    component: PollGroups,
    meta: {
      label: 'List your poll groups',
      navigable: false,
      icon: mdiStackOverflow
    }
  },
  {
    path: '*',
    name: '404',
    component: NotFound404,
    meta: {
      label: 'Not found',
      navigable: false
    }
  }
]

class VueRouterEx extends VueRouter {
  matcher: any

  public routes: RouteConfig[] = []

  constructor(options: RouterOptions) {
    super(options)
    const { addRoutes } = this.matcher
    const { routes } = options

    this.routes = routes!

    this.matcher.addRoutes = (newRoutes: RouteConfig[]) => {
      this.routes.push(...newRoutes)
      addRoutes(newRoutes)
    }
  }
}

Vue.use(VueRouterEx)

const router = new VueRouterEx({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.afterEach(to => {
  Vue.nextTick(() => {
    document.title = to.meta.label ? 'SimplePoll - ' + to.meta.label : 'SimplePoll'
  })
})

export default router
