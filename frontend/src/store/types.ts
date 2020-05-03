export class PollEntry {
  humanName: string
  id: string
  type: EntryType

  constructor(humanName: string, id: string, type: EntryType) {
    this.humanName = humanName
    this.id = id
    this.type = type
  }
}

export enum EntryType {
  BOOLEAN = 'BOOLEAN',
  TEXT = 'TEXT'
}

export class UserVote {
  user: string
  pollEntry: string
  value: any

  constructor(user: string, pollEntry: string, value: any) {
    this.user = user
    this.pollEntry = pollEntry
    this.value = value
  }
}

export class Poll {
  humanName: string
  id: string
  entries: PollEntry[]
  votes: Set<UserVote>

  constructor(humanName: string, id: string, entries: PollEntry[], votes: UserVote[]) {
    this.humanName = humanName
    this.id = id
    this.entries = entries.slice()
    this.votes = new Set(votes)
  }
}
