import { createModule, action, mutation } from 'vuex-class-component'
import { Poll, PollEntry, EntryType, UserVote, PollGroup } from '../types'
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

function pollGroupFromJson(data: any) {
  return new PollGroup(
    data.name,
    data.id,
    data.creator,
    data.polls.map((it: any) => pollFromJson(it))
  )
}

export class PollStore extends VxModule {
  polls: { [key: string]: Poll } = {}
  myPollGroups: { [key: string]: PollGroup } = {}

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

  @action
  async createPollGroup(name: string): Promise<PollGroup> {
    const response = await axios.put('group', {
      name: name
    })

    const group = pollGroupFromJson(response.data)

    this.addPollGroup(group)

    return group
  }

  @action
  async addPollToGroup(payload: { groupId: string; pollId: string }): Promise<PollGroup> {
    const response = await axios.patch('group', {
      groupId: payload.groupId,
      pollId: payload.pollId
    })

    const group = pollGroupFromJson(response.data)

    this.addPollGroup(group)

    return group
  }

  @action
  async fetchPollsInGroup(id: string): Promise<Poll[]> {
    const response = await axios.get('group/polls', { params: { id: id } })

    const polls: Poll[] = response.data.map((it: any) => pollFromJson(it))

    polls.forEach(it => this.addPoll(it))

    return polls
  }

  @action
  async fetchMyPollGroups(): Promise<PollGroup[]> {
    const response = await axios.get('group/for-user')

    const groups: PollGroup[] = response.data.map((it: any) => pollGroupFromJson(it))

    groups.forEach(it => this.addPollGroup(it))

    return groups
  }

  @action
  async fetchPollGroup(id: string): Promise<PollGroup | undefined> {
    const response = await axios.get('group', { params: { id: id } })

    if (response.status === 404) {
      return undefined
    }

    const group = pollGroupFromJson(response.data)

    this.addPollGroup(group)

    return group
  }

  @mutation
  addPollGroup(pollGroup: PollGroup) {
    Vue.set(this.myPollGroups, pollGroup.id, pollGroup)
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

  get allMyPollGroups(): PollGroup[] {
    return Object.values(this.myPollGroups).filter(it => it.creator !== undefined)
  }

  get pollGroupById(): (id: string) => PollGroup {
    return id => {
      return this.myPollGroups[id]
    }
  }
}
