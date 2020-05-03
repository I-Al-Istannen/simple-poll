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
        <v-row>
          <v-col cols="2" v-for="(entry, index) in pollEntries" :key="index">
            <v-card>
              <v-card-title class="entry-header">{{ entry.humanName }}</v-card-title>
              <v-card-text class="text-center headline font-weight-black">
                <span>{{ votesForEntry(entry.id) }}</span>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-text v-else>Poll not (yet) known</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn v-if="canReveal && !isRevealed" color="warning" outlined @click="reveal">Reveal</v-btn>
        <v-btn v-if="isRevealed" outlined color="success" @click="unreveal">Hide results</v-btn>
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
import { PollEntry, Poll } from '../store/types'

@Component
export default class Results extends Vue {
  get pollId() {
    return this.$route.params.pollId
  }

  get poll(): Poll | undefined {
    return vxm.pollModule.pollById(this.pollId)
  }

  get pollEntries(): PollEntry[] {
    if (!this.poll) {
      return []
    }
    const entries = this.poll.entries.slice()
    entries.sort((a, b) => this.votesForEntry(b.id) - this.votesForEntry(a.id))
    return entries
  }

  get canReveal() {
    if (!this.poll) {
      return false
    }
    return this.poll.creator
  }

  get isRevealed() {
    if (!this.poll) {
      return false
    }
    return this.poll.resultsRevealed
  }

  votesForEntry(id: string): number {
    if (!this.poll) {
      return 0
    }
    let count = 0
    this.poll.votes.forEach(it => {
      if (it.pollEntry == id) count++
    })
    return count
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
</style>
