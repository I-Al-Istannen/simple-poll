import { createModule, action, mutation } from 'vuex-class-component'
import { Poll, PollEntry, EntryType, UserVote } from '../types'
import axios from 'axios'
import Vue from 'vue'

const VxModule = createModule({
  namespaced: 'pollModule',
  strict: false
})

function pollFromJson(data: any) {
  return new Poll(
    data.humanName,
    data.id,
    data.creator || undefined,
    data.entries.map((it: any) => new PollEntry(it.humanName, it.id, it.type)),
    data.votes.map((it: any) => new UserVote(it.user, it.pollEntry, it.value)),
    data.revealed,
    data.allowMultiple
  )
}

export class PollStore extends VxModule {
  polls: { [key: string]: Poll } = {}

  @action
  async createPoll(payload: {
    name: string
    allowMultiple: boolean
    entries: { name: string; type: EntryType }[]
  }): Promise<Poll> {
    const response = await axios.put('poll', {
      name: payload.name,
      allowMultiple: payload.allowMultiple,
      pollEntries: payload.entries
    })
    return pollFromJson(response.data)
  }

  @action
  async fetchPoll(id: string): Promise<Poll | undefined> {
    const response = await axios.get('poll', { params: { id: id } })

    if (response.status === 404) {
      return undefined
    }

    const poll = pollFromJson(response.data)

    this.addPoll(poll)

    return poll
  }

  @action
  async reveal(payload: { id: string; reveal: boolean }): Promise<Poll | undefined> {
    const response = await axios.patch('poll', {
      pollId: payload.id,
      isReveal: payload.reveal
    })

    if (response.status === 404) {
      return undefined
    }

    const poll = pollFromJson(response.data)

    this.addPoll(poll)

    return poll
  }

  @action
  async vote(payload: {
    id: string
    votes: { entryId: string; value: string }[]
  }): Promise<Poll | undefined> {
    const response = await axios.post('vote', {
      pollId: payload.id,
      votes: payload.votes
    })

    if (response.status === 404) {
      return undefined
    }

    const poll = pollFromJson(response.data)

    this.addPoll(poll)

    return poll
  }

  @mutation
  addPoll(poll: Poll) {
    Vue.set(this.polls, poll.id, poll)
  }

  get pollById(): (id: string) => Poll | undefined {
    return id => {
      return this.polls[id]
    }
  }
}
