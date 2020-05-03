class Poll {
  constructor(humanName, id, creator, entries, votes, revealResults) {
    this.humanName = humanName;
    this.id = id;
    this.creator = creator;
    this.entries = entries;
    this.votes = votes;
    this.revealResults = revealResults;
  }
}

class PollEntry {

  constructor(humanName, id, type) {
    this.humanName = humanName;
    this.id = id;
    this.type = type;
  }
}

class VoteRequest {

  constructor(pollId, pollEntryId, value) {
    this.pollId = pollId;
    this.pollEntryId = pollEntryId;
    this.value = value;
  }
}

class PollRequest {

  constructor(name, entries) {
    this.name = name;
    this.entries = entries;
  }
}

class RevealPollRequest {

  constructor(pollId) {
    this.pollId = pollId;
  }
}

BASE_URL = "http://localhost:8080"

/**
 * Creates a new poll
 * @param {PollRequest} createPollRequest the request
 * @returns {Promise<Response>} the created poll
 */
function createPoll(createPollRequest) {
  return fetch(
      BASE_URL + "/create",
      {
        credentials: "include",
        body: JSON.stringify(createPollRequest),
        method: "POST"
      }
  )
}

/**
 * Get a poll.
 * @param {string} id the id of the poll
 * @returns {Promise<Response>} the poll, if any
 */
function getPoll(id) {
  return fetch(
      BASE_URL + "/get?pollId=" + encodeURI(id),
      {credentials: "include"}
  )
}

/**
 /**
 * Votes on a poll.
 *
 * @param {VoteRequest} voteRequest the vote request
 * @returns {Promise<Response>} nothing
 */
function voteOnPoll(voteRequest) {
  return fetch(
      BASE_URL + "/vote",
      {
        credentials: "include",
        body: JSON.stringify(voteRequest),
        method: "POST"
      }
  )
}

/**
 * Reveals a poll.
 * @param {RevealPollRequest} revealPollRequest the reveal request
 * @returns {Promise<Response>} the new poll
 */
function revealPoll(revealPollRequest) {
  return fetch(
      BASE_URL + "/reveal",
      {
        credentials: "include",
        body: JSON.stringify(revealPollRequest),
        method: "POST"
      }
  )
}