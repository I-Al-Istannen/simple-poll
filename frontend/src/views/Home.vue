<template>
  <v-container class="wrapper">
    <v-row justify="center">
      <v-card>
        <v-card-title>
          <v-toolbar dark color="primary">Simple poll System</v-toolbar>
        </v-card-title>
        <v-card-text>
          <v-row>
            <v-col>Hey, you can do a single thing here.</v-col>
          </v-row>
          <v-row justify="center">
            <v-col cols="auto">
              <v-btn :to="{ name: 'create-poll' }" color="primary" outlined>Create a poll</v-btn>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'

@Component
export default class Home extends Vue {}
</script>
