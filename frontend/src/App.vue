<template>
  <v-app>
    <v-content>
      <nav-bar></nav-bar>
      <router-view />
    </v-content>
  </v-app>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import NavigationBar from './components/NavigationBar.vue'

@Component({
  components: {
    'nav-bar': NavigationBar
  }
})
export default class App extends Vue {}
</script>
