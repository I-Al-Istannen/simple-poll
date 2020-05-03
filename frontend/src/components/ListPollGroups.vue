<template>
  <v-container fluid>
    <v-card>
      <v-card-title>
        <v-toolbar color="primary" dark>All of your polls</v-toolbar>
      </v-card-title>
      <v-card-text>
        <v-dialog width="700" v-model="editOpen">
          <template #activator></template>
          <v-card v-if="editGroup">
            <v-card-title>
              <v-toolbar color="primary" dark>Edit '{{ editGroup.name }}'</v-toolbar>
            </v-card-title>
            <v-card-text></v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn outlined @click="editOpen = false">Close</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-treeview :items="treeItems">
          <template #label="{ item, leaf }">
            <v-btn v-if="!leaf" color="primary" outlined @click="edit(item)">
              <span>Modify '{{ item.name }}'</span>
            </v-btn>
            <v-btn
              v-if="leaf"
              :to="{ name: 'view-results', params: { pollId: item.id } }"
              color="primary"
              outlined
            >
              <span>Show results for '{{ item.name }}'</span>
            </v-btn>
          </template>
          <template #append="{ item, leaf }">
            <router-link
              v-if="!leaf"
              class="ml-2"
              :to="{ name: 'view-group', params: { groupId: item.id } }"
            >
              <span>Details</span>
            </router-link>
          </template>
        </v-treeview>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" outlined @click="refresh">Refresh</v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { vxm } from '../store'
import { PollGroup, Poll } from '../store/types'

class TreeItem {
  id: string
  name: string
  children: TreeItem[] | undefined

  constructor(id: string, name: string, children: TreeItem[] | undefined) {
    this.id = id
    this.name = name
    this.children = children
  }
}

@Component
export default class ListPollGroups extends Vue {
  private editOpen = false
  private editGroup: PollGroup | null = null

  private get myPollGroups(): PollGroup[] {
    return vxm.pollModule.allMyPollGroups
  }

  private get treeItems(): TreeItem[] {
    return this.myPollGroups.map(
      it =>
        new TreeItem(
          it.id,
          it.name,
          it.polls.map(poll => this.pollToItem(poll))
        )
    )
  }

  private pollToItem(poll: Poll): TreeItem {
    return new TreeItem(poll.id, poll.humanName, undefined)
  }

  private edit(item: TreeItem) {
    this.editGroup = this.myPollGroups.find(it => it.id === item.id) || null
    this.editOpen = true
  }

  refresh() {
    vxm.pollModule.fetchMyPollGroups()
  }

  mounted() {
    this.refresh()
  }
}
</script>

<style scoped></style>
