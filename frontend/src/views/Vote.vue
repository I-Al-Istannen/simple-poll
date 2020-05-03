<template>
  <v-container class="wrapper">
    <v-row justify="center">
      <vote-on-poll></vote-on-poll>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import VoteOnPoll from '../components/VoteOnPoll.vue'

@Component({
  components: {
    'vote-on-poll': VoteOnPoll
  }
})
export default class Vote extends Vue {}
</script>

<style scoped></style>
