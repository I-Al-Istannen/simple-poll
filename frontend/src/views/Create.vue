<template>
  <v-container class="wrapper">
    <v-row justify="center">
      <create-poll></create-poll>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import CreatePoll from '../components/CreatePoll.vue'

@Component({
  components: {
    'create-poll': CreatePoll
  }
})
export default class Home extends Vue {}
</script>
