<template>
  <v-container fluid>
    <v-card>
      <v-card-title>
        <v-toolbar color="primary" dark>
          <span v-if="poll">Vote on '{{ poll.humanName }}'</span>
          <span v-if="!poll">Vote on a poll</span>
        </v-toolbar>
      </v-card-title>
      <v-card-text class="mx-4">
        <v-form v-model="formValid" v-if="poll">
          <v-row>
            <v-col v-for="entry in poll.entries" :key="entry.id">
              <v-text-field
                :rules="[notEmpty]"
                :label="entry.humanName"
                v-if="entry.type === 'TEXT'"
                v-model="freeEntryValues[entry.id]"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row v-if="hasBooleanOption && !allowMultiple">
            <v-radio-group :rules="[notEmptyArray]" v-model="booleanOptionsValues">
              <v-radio
                v-for="entry in booleanOptions"
                :key="entry.id"
                :label="entry.humanName"
                :value="entry.id"
              ></v-radio>
            </v-radio-group>
          </v-row>
          <v-row v-if="hasBooleanOption && allowMultiple">
            <v-col cols="3" v-for="entry in booleanOptions" :key="entry.id">
              <v-checkbox
                :label="entry.humanName"
                :value="entry.id"
                :rules="[notEmptyArray]"
                v-model="booleanOptionsValues"
                multiple
              ></v-checkbox>
            </v-col>
          </v-row>
        </v-form>
        <div v-if="!poll" class="headline text-center">
          <span>There is no poll with that ID or it hasn't loaded yet.</span>
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" :disabled="!formValid" outlined @click="submit">Submit vote</v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { Poll, PollEntry, EntryType } from '../store/types'
import { vxm } from '../store'
import { Watch } from 'vue-property-decorator'

@Component
export default class VoteOnPoll extends Vue {
  private formValid = false

  private booleanOptionsValues: string[] | string = []
  private freeEntryValues: { [key: string]: string } = {}

  get pollId() {
    return this.$route.params.pollId
  }

  private get poll(): Poll | undefined {
    return vxm.pollModule.pollById(this.pollId)
  }

  private get pollEntries(): PollEntry[] {
    if (!this.poll) {
      return []
    }
    return this.poll.entries
  }

  private get isRevealed() {
    if (!this.poll) {
      return false
    }
    return this.poll.resultsRevealed
  }

  private get hasBooleanOption() {
    return this.booleanOptions.length > 0
  }

  private get booleanOptions() {
    if (!this.poll) {
      return []
    }
    return this.poll.entries.filter(it => it.type === EntryType.BOOLEAN)
  }

  private get allowMultiple() {
    if (!this.poll) {
      return false
    }
    return this.poll.allowMultiple
  }

  private refresh() {
    vxm.pollModule.fetchPoll(this.pollId)
  }

  private notEmpty(input: string): boolean | string {
    return !input ? 'This field must not be empty' : true
  }

  private notEmptyArray(input: string[]): boolean | string {
    return input.length === 0 ? 'This field must not be empty' : true
  }

  @Watch('pollId')
  private onIdChange() {
    this.refresh()
  }

  mounted() {
    this.refresh()
  }

  private async submit() {
    const entries: { entryId: string; value: string }[] = []

    if (this.booleanOptionsValues instanceof Array) {
      this.booleanOptionsValues.forEach(it => entries.push({ entryId: it, value: 'true' }))
    } else {
      entries.push({ entryId: this.booleanOptionsValues, value: 'true' })
    }

    Object.entries(this.freeEntryValues).forEach(([key, value]) =>
      entries.push({ entryId: key, value: value })
    )

    const poll = await vxm.pollModule.vote({
      id: this.pollId,
      votes: entries
    })

    if (poll) {
      this.$router.push({ name: 'view-results', params: { pollId: poll.id } })
    }
  }
}
</script>

<style scoped></style>
