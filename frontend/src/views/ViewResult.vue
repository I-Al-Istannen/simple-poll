<template>
  <v-container class="wrapper">
    <v-row justify="center">
      <view-result></view-result>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import ResultsComponent from '../components/ResultsComponent.vue'

@Component({
  components: {
    'view-result': ResultsComponent
  }
})
export default class ViewResult extends Vue {}
</script>

<style scoped></style>
