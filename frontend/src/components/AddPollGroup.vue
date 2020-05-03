<template>
  <v-container fluid>
    <v-card outlined>
      <v-card-text>
        <v-row justify="center" align="center">
          <v-col cols="auto">
            <v-dialog width="500" v-model="dialogOpen">
              <template #activator="{ on }">
                <v-btn v-on="on" color="primary" outlined>
                  <span>Add a poll group</span>
                </v-btn>
              </template>
              <v-card>
                <v-card-title>
                  <v-toolbar color="primary" dark>Add a group</v-toolbar>
                </v-card-title>
                <v-card-text class="px-7">
                  <v-form v-model="formValid">
                    <v-text-field v-model="groupName" :rules="[notEmpty]" label="Group name..."></v-text-field>
                  </v-form>
                </v-card-text>
                <v-card-actions class="pb-5">
                  <v-spacer></v-spacer>
                  <v-btn color="success" :disabled="!formValid" outlined @click="addPollGroup">
                    <span>Create poll group</span>
                  </v-btn>
                  <v-btn color="warning" outlined @click="dialogOpen = false">Close</v-btn>
                  <v-spacer></v-spacer>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { vxm } from '../store'

@Component
export default class AddPollGroup extends Vue {
  private dialogOpen = false
  private formValid = false
  private groupName = ''

  private addPollGroup() {
    vxm.pollModule.createPollGroup(this.groupName).then(() => (this.dialogOpen = false))
  }

  private notEmpty(input: string): boolean | string {
    return !input ? 'This field must not be empty' : true
  }
}
</script>

<style scoped></style>
