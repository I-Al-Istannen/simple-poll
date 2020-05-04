<template>
  <v-container fluid>
    <v-card>
      <v-card-title>
        <v-toolbar color="primary" dark>
          View results
          <span v-if="poll" class="pl-1">for '{{ poll.humanName }}'</span>
        </v-toolbar>
      </v-card-title>
      <v-card-text v-if="poll">
        <v-row justify="end">
          <v-col cols="auto">
            <span>Link to this poll:</span>
            <router-link
              class="pl-1"
              :to="{ name: 'vote', params: { pollId: pollId } }"
              v-slot="{ href }"
            >
              <a :href="href">{{ origin }}{{ href }}</a>
            </router-link>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="2" v-for="(entry, index) in pollEntries" :key="index">
            <v-card>
              <v-card-title class="entry-header nicer-word-break">
                <span>{{ entry.humanName }}</span>
              </v-card-title>
              <v-card-text>
                <div v-if="entry.type === 'BOOLEAN'" class="text-center headline font-weight-black">
                  <span>{{ voteCountForEntry(entry.id) }}</span>
                </div>
                <div v-if="entry.type === 'TEXT'">
                  <v-list>
                    <v-subheader>
                      <span>{{ voteCountForEntry(entry.id) }} Vote(s)</span>
                    </v-subheader>
                    <v-list-item-group>
                      <v-list-item
                        v-for="(vote, index) in votesForEntry(entry.id)"
                        :key="index"
                        class="nicer-word-break"
                      >
                        <span>{{ vote.value }}</span>
                      </v-list-item>
                    </v-list-item-group>
                  </v-list>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-text v-else>Poll not (yet) known</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn v-if="canReveal && !isRevealed" color="warning" outlined @click="reveal">Reveal</v-btn>
        <v-btn v-if="canReveal && isRevealed" outlined color="success" @click="unreveal">
          <span>Hide results</span>
        </v-btn>
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
import { Watch } from 'vue-property-decorator'
import { PollEntry, Poll, UserVote } from '../store/types'

@Component
export default class Results extends Vue {
  private get pollId() {
    return this.$route.params.pollId
  }

  private get poll(): Poll | undefined {
    return vxm.pollModule.pollById(this.pollId)
  }

  private get pollEntries(): PollEntry[] {
    if (!this.poll) {
      return []
    }
    const entries = this.poll.entries.slice()
    entries.sort((a, b) => this.voteCountForEntry(b.id) - this.voteCountForEntry(a.id))
    return entries
  }

  private get canReveal() {
    if (!this.poll) {
      return false
    }
    return this.poll.creator
  }

  private get isRevealed() {
    if (!this.poll) {
      return false
    }
    return this.poll.resultsRevealed
  }

  private get origin() {
    return document.location.origin
  }

  private voteCountForEntry(id: string): number {
    return this.votesForEntry(id).length
  }

  private votesForEntry(id: string): UserVote[] {
    if (!this.poll) {
      return []
    }
    return Array.from(this.poll.votes).filter(it => it.pollEntry === id)
  }

  private reveal() {
    vxm.pollModule.reveal({ id: this.pollId, reveal: true })
  }

  private unreveal() {
    vxm.pollModule.reveal({ id: this.pollId, reveal: false })
  }

  private refresh() {
    vxm.pollModule.fetchPoll(this.pollId)
  }

  @Watch('pollId')
  private onIdChange() {
    this.refresh()
  }

  mounted() {
    this.refresh()
  }
}
</script>

<style scoped>
.entry-header {
  display: flex;
  justify-content: center;
}

.nicer-word-break {
  word-break: break-word;
}
</style>
