<template>
  <v-container class="wrapper">
    <v-row justify="center">
      <list-poll-groups></list-poll-groups>
      <add-poll-group></add-poll-group>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import ListPollGroups from '../components/ListPollGroups.vue'
import AddPollGroup from '../components/AddPollGroup.vue'

@Component({
  components: {
    'list-poll-groups': ListPollGroups,
    'add-poll-group': AddPollGroup
  }
})
export default class PollGroups extends Vue {}
</script>

<style scoped></style>
