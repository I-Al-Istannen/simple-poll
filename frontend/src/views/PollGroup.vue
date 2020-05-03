<template>
  <v-container class="wrapper">
    <v-row justify="center">
      <v-col>
        <v-card>
          <v-card-title>
            <v-toolbar color="primary" dark>View polls in group</v-toolbar>
          </v-card-title>
          <v-card-text>
            <v-list>
              <v-list-item-group>
                <v-list-item v-for="poll in pollsInGroup" :key="poll.id" two-line>
                  <v-list-item-content>
                    <v-list-item-title>{{ poll.humanName }}</v-list-item-title>
                    <v-list-item-subtitle>
                      <router-link
                        v-if="!poll.resultsRevealed"
                        class="mr-2"
                        :to="{ name: 'vote', params: { pollId: poll.id } }"
                      >
                        <span>Abstimmen</span>
                      </router-link>
                      <span v-else class="mr-2">Abstimmung beendet</span>
                      <router-link :to="{ name: 'view-results', params: { pollId: poll.id } }">
                        <span>Ergebnisse</span>
                      </router-link>
                    </v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" outlined @click="refresh">Refresh</v-btn>
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { Poll } from '../store/types'
import { vxm } from '../store'
import { Watch } from 'vue-property-decorator'

@Component
export default class Home extends Vue {
  private get groupId() {
    return this.$route.params.groupId
  }

  private get group() {
    return vxm.pollModule.pollGroupById(this.groupId)
  }

  private get pollsInGroup(): Poll[] {
    if (!this.group) {
      return []
    }
    return this.group.polls
  }

  @Watch('groupId')
  private refresh() {
    vxm.pollModule.fetchPollGroup(this.groupId)
  }

  mounted() {
    this.refresh()
  }
}
</script>
