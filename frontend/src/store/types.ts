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
  creator: string | undefined
  entries: PollEntry[]
  votes: Set<UserVote>
  resultsRevealed: boolean
  allowMultiple: boolean
  publicResults: boolean

  constructor(
    humanName: string,
    id: string,
    creator: string | undefined,
    entries: PollEntry[],
    votes: UserVote[],
    resultsRevealed: boolean,
    allowMultiple: boolean,
    publicResults: boolean
  ) {
    this.humanName = humanName
    this.id = id
    this.creator = creator
    this.entries = entries.slice()
    this.votes = new Set(votes)
    this.resultsRevealed = resultsRevealed
    this.allowMultiple = allowMultiple
    this.publicResults = publicResults
  }
}

export class PollGroup {
  name: string
  id: string
  creator: string | undefined
  polls: Poll[]

  constructor(name: string, id: string, creator: string | undefined, polls: Poll[]) {
    this.name = name
    this.id = id
    this.creator = creator
    this.polls = polls
  }
}
