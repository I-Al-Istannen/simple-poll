import { createModule, action } from 'vuex-class-component'
import { Poll, PollEntry, EntryType, UserVote } from '../types'
import axios from 'axios'

const VxModule = createModule({
  namespaced: 'pollModule',
  strict: false
})

export class PollStore extends VxModule {
  polls: Poll[] = []

  @action
  async createPoll(payload: {
    name: string
    entries: { name: string; type: EntryType }[]
  }): Promise<Poll> {
    const response = await axios.put('poll', {
      name: payload.name,
      pollEntries: payload.entries
    })
    const data = response.data
    return new Poll(
      data.humanName,
      data.id,
      data.entries.map((it: any) => new PollEntry(it.humanName, it.id, it.type)),
      data.votes.map((it: any) => new UserVote(it.user, it.pollEntry, it.value))
    )
  }
}
