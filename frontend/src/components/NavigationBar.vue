<template>
  <nav>
    <v-toolbar dark color="primary darken-1">
      <v-app-bar-nav-icon class="hidden-md-and-up" @click="drawerShown = !drawerShown"></v-app-bar-nav-icon>
      <v-toolbar-title>{{ title }}</v-toolbar-title>

      <v-spacer></v-spacer>

      <!-- Navigation items -->
      <v-btn
        class="hidden-sm-and-down"
        v-for="item in validRoutes"
        :key="item.routeName"
        text
        :to="{ name: item.routeName }"
      >
        {{ item.label }}
        <v-icon right dark :size="iconFontSize">{{ item.icon }}</v-icon>
      </v-btn>
    </v-toolbar>

    <!-- Navigation drawer -->
    <v-navigation-drawer class="hidden-md-and-up" v-model="drawerShown" app temporary>
      <v-toolbar dark color="primary darken-1">
        <v-list>
          <v-list-item>
            <v-list-item-title class="title">Navigation</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-toolbar>

      <v-divider></v-divider>

      <v-list dense class="pt-0">
        <v-list-item
          v-for="item in validRoutes"
          :key="item.routeName"
          :to="{ name: item.routeName }"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.label }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
  </nav>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { VuetifyIcon } from 'vuetify/types/services/icons'
import router from '../router'

class NavigationItem {
  readonly routeName: string
  readonly icon: VuetifyIcon
  readonly label: string

  constructor(routeName: string, icon: VuetifyIcon, label: string) {
    this.routeName = routeName
    this.icon = icon
    this.label = label
  }
}

@Component
export default class NavigationBar extends Vue {
  private iconFontSize = 22
  private title = 'SimplePoll'

  private drawerShown = false

  get validRoutes() {
    return router.routes
      .filter(route => route.meta.navigable)
      .map(route => new NavigationItem(route.name!, route.meta.icon, route.meta.label))
  }
}
</script>

<style>
.v-tooltip__content {
  opacity: 1 !important;
}
</style>
