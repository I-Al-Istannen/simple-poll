<template>
  <v-container fluid>
    <v-card>
      <v-card-title>
        <v-toolbar color="primary" dark>Create a poll</v-toolbar>
      </v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="formValid">
          <v-text-field v-model="pollName" :rules="[notEmpty]" label="Name..."></v-text-field>
          <v-radio-group hide-details v-model="preset" :rules="[notEmpty, customSelected]">
            <v-radio
              v-for="fieldset in defaultFieldset"
              :key="fieldset.name"
              :label="fieldset.name"
              :value="fieldset.value"
            ></v-radio>
          </v-radio-group>
          <v-row v-if="customOptionSelected" class="ml-3" dense>
            <v-col cols="3" v-for="(value, index) in extendedCustomOptions" :key="index">
              <v-text-field
                class="ma-0"
                :rules="index === extendedCustomOptions.length - 1 && index != 0 ? [] : [notEmpty]"
                label="Text..."
                @input="setOption(index, $event)"
                :value="getOption(index)"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-checkbox
            v-model="freeformInput"
            label="Allow user to enter arbitrary text and don't constrain them to 'yes' or 'no'"
            class="pa-0"
            hide-details
          ></v-checkbox>
          <v-checkbox
            class="mt-1"
            v-model="allowMultiple"
            label="Allow multiple answers (freeform input is always multiple)"
            :disabled="freeformInput"
            hide-details
          ></v-checkbox>
          <v-checkbox
            class="mt-1"
            v-model="publicResults"
            label="Allow users to view results before the poll ended"
            :disabled="freeformInput"
            hide-details
          ></v-checkbox>
          <v-row>
            <v-col cols="3">
              <v-select
                v-model="selectedGroups"
                multiple
                clearable
                chips
                deletable-chips
                class="mt-3"
                :items="myPollGroups"
                item-text="name"
                item-value="id"
                label="Add to poll group"
                return-object
              ></v-select>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="success" outlined :disabled="!formValid" @click="create">Create form</v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { EntryType, PollGroup } from '@/store/types'
import { vxm } from '../store'

@Component
export default class Create extends Vue {
  private defaultFieldset: { name: string; value: string[] }[] = [
    {
      name: 'Yes / No',
      value: ['Yes', 'No']
    },
    {
      name: 'A / B',
      value: ['A', 'B']
    },
    {
      name: 'A / B / C /D',
      value: ['A', 'B', 'C', 'D']
    },
    {
      name: 'Custom options',
      value: ['Custom']
    }
  ]

  private selectedGroups: PollGroup[] = []

  private formValid = false
  private allowMultiple = false
  private publicResults = false

  private pollName = ''
  private customOptions: string[] = []
  private preset: string[] = this.defaultFieldset[0].value
  private freeformInput = false

  private get extendedCustomOptions(): string[] {
    return [...this.customOptions, '']
  }

  private getOption(index: number): string {
    return this.customOptions[index]
  }

  private setOption(index: number, value: string) {
    Vue.set(this.customOptions, index, value)
    if (value === '') {
      this.customOptions.splice(index, 1)
    }
    ;(this.$refs.form as any).validate()
  }

  private get customOptionSelected() {
    return this.preset[0] === 'Custom'
  }

  private notEmpty(input: string): boolean | string {
    return !input ? 'This field must not be empty' : true
  }

  private customSelected(): boolean | string {
    if (!this.customOptionSelected) {
      return true
    }
    return this.customOptions.length > 0 ? true : 'Please add at least one field'
  }

  private get myPollGroups() {
    return vxm.pollModule.allMyPollGroups
  }

  private async create() {
    if (!(this.$refs.form as any).validate()) {
      return
    }
    let entries: { name: string; type: EntryType }[]
    const type = this.freeformInput ? EntryType.TEXT : EntryType.BOOLEAN

    if (this.customOptionSelected) {
      entries = this.customOptions.map(it => ({ name: it, type: type }))
    } else {
      entries = this.preset.map(it => ({ name: it, type: type }))
    }

    const poll = await vxm.pollModule.createPoll({
      name: this.pollName,
      allowMultiple: this.allowMultiple,
      publicResults: this.publicResults,
      entries: entries
    })

    for (let i = 0; i < this.selectedGroups.length; i++) {
      const id = this.selectedGroups[i].id
      await vxm.pollModule.addPollToGroup({ groupId: id, pollId: poll.id })
    }

    this.$router.push({ name: 'view-results', params: { pollId: poll.id } })
  }

  mounted() {
    vxm.pollModule.fetchMyPollGroups()
  }
}
</script>

<style scoped></style>
