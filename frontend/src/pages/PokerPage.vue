<template>
  <q-page v-if="joinAndCreateButtonVisible" class="q-pa-md flex flex-center">
  <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" style="padding: 30px">
        <q-card>
          <q-card-section>
            <div class="text-h5">Create a new session</div>
          </q-card-section>
          <q-card-actions>
            <div class="row justify-center">
              <q-input v-model="author" :error="createUsernameValid" bg-color="white" error-message="Username required!"
                       label="Username" style="padding: 30px" type="text" @keydown.enter="createRoom"/>
              <q-btn color="primary" label="Create" @click="createRoom"/>
            </div>
          </q-card-actions>
        </q-card>
    </div>
    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 justify-center"
          style="padding: 30px">
      <q-card>
        <q-card-section>
          <div class="text-h5">Join a session</div>
        </q-card-section>
        <q-card-actions class="justify-center">
          <div class="justify-center">
            <div class="col-12" style="padding: 5px">
              <q-input v-model="username" :error="joinUsernameValid" bg-color="white"
                        error-message="Username required!" label="Username"
                        type="text"/>
            </div>
            <div class="col-12" style="padding: 5px">
              <q-input v-model="roomId" :error="roomIdValid" :error-message="roomIdErrorMessage"
              bg-color="white" label="Room ID" type="text"/>
            </div>
            <div class="col-12 text-center" style="padding: 10px">
              <q-btn color="secondary" label="Join" @click="joinRoom"/>
            </div>
          </div>
        </q-card-actions>
      </q-card>
    </div>
  </q-page>

  <q-page v-else class="row q-pa-md content-start">
    <div class="col-12 text-center">
      <h5> {{ this.room.id }}</h5>
    </div>
    <div v-if="this.room.author == this.author" class="col-12">
      <div class="row text-center q-pa-md">
        <div class="col-11">
          <q-input v-model="inputStory" label="Story name:" outlined/>
        </div>
        <div class="col-1">
          <q-btn color="primary" @click="createStory">Add</q-btn>
        </div>
      </div>
    </div>
    
    <div class="col-12">
      <q-tabs v-model="tab" active-color="primary" align="justify" 
      class="bg-grey-3 text-grey-7" dense indicator-color="primary">
        <template v-for="(story, index) in this.room.stories" :key="index">
          <div @click="selectStory(story)">
            <q-tab :disable=story.disabled :label=story.story :name=story.story no-caps></q-tab>
          </div>
        </template>
      </q-tabs>

      <q-tab-panels v-model="tab" animated class="text-black">
        <template v-for="(story, index) in this.room.stories" :key="index">
          <q-tab-panel :name=story.story>
            <div class="row justify-center items-center">
              <div class="col-8">
                <div class="row">
                  <div v-for="(option, index) in voteOptionsFirst.slice(0, 4)" v-bind:key="index" 
                  class="col-3 text-center q-pa-sm">
                    <q-btn :disable=!showVoteOptions style="width: 200px; height: 200px; font-size: 70px;" :label="option" @click="vote(option)" />
                  </div>
                </div>
                <div class="row">
                  <div v-for="(option, index) in voteOptionsFirst.slice(4, 8)" v-bind:key="index" 
                  class="col-3 text-center q-pa-sm">
                    <q-btn :disable=!showVoteOptions style="width: 200px; height: 200px; font-size: 70px;" :label="option" @click="vote(option)" />
                  </div>
                </div>
              </div>
              <div v-if="this.room.selectedStory != null && this.author === this.room.author" class="row">
                <div class="col-12 text-center items-center">
                  <div class="col-12 q-pa-sm">
                    <q-btn v-if="!this.votingIsOpen" color="secondary" @click="startVoting">Start voting</q-btn>
                  </div>
                  <div class="col-12 q-pa-md">
                    <q-btn v-if="this.votingIsOpen" color="negative" @click="finishVoting">Close voting</q-btn>
                  </div>
                  <div class="col-12">
                    <q-btn v-if="!this.votingIsOpen && index < this.room.stories.length - 1" color="primary" @click="nextStory(index, true)">Next story</q-btn>
                  </div>
                </div>
              </div>
              <div class="col-2">
                <div class="row text-center papapa">
                  <div class="col-12">
                    <template v-for="(user, index) in room.users" :key="index">
                      <q-item v-ripple clickable>
                        <q-item-section side>
                          <q-avatar color="primary" size="lg" text-color="white">
                            {{ getFirstLetter(user.username) }}
                            <q-tooltip>
                              {{ user.username }}
                            </q-tooltip>
                          </q-avatar>
                        </q-item-section>
                        <q-item-section>
                          <q-item-label>{{ user.username }}</q-item-label>
                          <q-item-label v-if="votingIsOpen && !(typeof getVoteValue(user) == 'number')" caption>Voting...</q-item-label>
                          <q-item-label v-if="!votingIsOpen || !showVoteOptions" caption> {{ getVoteValue(user) }} </q-item-label>
                        </q-item-section>
                      </q-item>
                    </template>
                    <div v-if="!votingIsOpen" class="text-center q-pa-sm">
                      <h5>
                        Average: {{ calculateVotesAvg() }}
                      </h5>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </q-tab-panel>
        </template>
      </q-tab-panels>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import axios from 'axios'
import {stompClientStore} from 'stores/stomp'

const store = stompClientStore()

export default defineComponent({
  name: 'PokerPage',
  data() {
    return {
      author: null,
      inputStory: null,
      joinAndCreateButtonVisible: true,
      room: {
        id: 1,
        author: "",
        currentVoteResult: {
          id: 0,
          locked: false,
          votes: [
            {
              username: "",
              value: 10,
            }
          ]
        },
        stories: [
          {
            id: 1,
            story: "",
            voteResults: [
              {
                id: 1,
                votes: [
                  {
                    username: "",
                    value: 1
                  }
                ],
              }
            ],
            disabled: true,
          }
        ],
        selectedStory: {
          id: 1,
          story: "",
          voteResults: [
            {
              id: 1,
              votes: [
                {
                  username: "",
                  value: 1
                }
              ],
            }
          ],
          disabled: true,
        },
        users: [],
        selectedStoryId: 1,
      },
      username: null,
      createUsernameValid: false,
      joinUsernameValid: false,
      roomIdValid: false,
      roomIdErrorMessage: 'Room ID required!',
      votingIsOpen: false,
      currentVoting: null,
      showVoteOptions: false,
      backendUrls: {
        pathParts: {
          poker: '/poker',
          room: '/room',
        },
        //baseUrl: 'http://localhost:8098',
        baseUrl: 'https://www.retrospecto.cloud',
        baseUrlPoker: function() { return this.baseUrl + this.pathParts.poker},
        createRoom: function() { return this.baseUrlPoker() + this.pathParts.room + "/"},
        getRoom: function() { return this.baseUrlPoker() + this.pathParts.room + "/"},
      },
      voteOptionsFirst: [1, 2, 3, 5, 7, 11, 13, 17],
      tab: 'test',
    }
  },
  methods:
    {
      getFirstLetter(username) {
        return Array.from(username)[0];
      },
      vote(voteValue) {
        let vote = {
          username: this.username,
          value: voteValue,
        };

        store.getStompClient.send("/app/poker/" + this.room.id + "/story/" + this.room.selectedStory["id"] + "/vote", {}, JSON.stringify(vote));
        this.showVoteOptions = false;
      },
      createStory() {
        if (this.inputStory) {
          store.getStompClient.send("/app/poker/" + this.room.id + "/story/add", {}, this.inputStory);
          this.inputStory = null;
        }
      },
      selectStory(story) {
        if (this.author === this.room.author && !this.votingIsOpen && !story.disabled) {
          store.getStompClient.send("/app/poker/" + this.room.id + "/story/" + story["id"] + "/selected", {});
        }
      },
      nextStory(index) {
        if (this.author === this.room.author && !this.votingIsOpen) {
          let story = this.room.stories[index+1];
          store.getStompClient.send("/app/poker/" + this.room.id + "/story/" + story["id"] + "/selected", {});
        }
      },
      createRoom() {
        if (!this.author) {
          this.createUsernameValid = true
        } else {
          this.room = null;
          var config = {
              headers: {
                  'Content-Type': 'text/plain'
              }
          };
          axios.post(this.backendUrls.createRoom(), this.author, config)
            .then(response => {
                if (response.data != null) {
                  this.room = response.data;
                  this.joinAndCreateButtonVisible = false;
                  this.username = this.author;
                  this.subscribe();
                }
              });
        }
      },
      joinRoom() {
        if (!this.roomId && !this.username) {
          this.joinUsernameValid = true
          this.roomIdValid = true
          this.roomIdErrorMessage = "Room ID required!"
        }
        if (!this.username && this.roomId) {
          this.joinUsernameValid = true
          this.roomIdValid = false
        }
        if (this.username && !this.roomId) {
          this.roomIdValid = true
          this.roomIdErrorMessage = "Room ID required!"
          this.joinUsernameValid = false
        }
        if (this.roomId && this.username) {
          this.joinUsernameValid = false
          this.roomIdValid = false
        
          axios.get(this.backendUrls.getRoom() + this.roomId + '/username/' + this.username)
            .then(response => {
              if (response.data != null) {
                this.room = response.data;
                this.joinAndCreateButtonVisible = false;
                this.subscribe();

          
                for (let i = 0; i < this.room.stories.length; i++) {
                  if (this.room.stories[i].id == this.room.selectedStoryId) {
                    this.room.selectedStory = this.room.stories[i];
                  }
                }

                store.getStompClient.send("/app/poker/room/" + this.room.id + "/user/add", {}, this.username);
              }
            })
            .catch(error => {
              this.roomIdValid = true;
              this.roomIdErrorMessage = 'Room ID does not exist!';
              this.roomId = null;
            })
        }
      },
      subscribe() {
        store.getStompClient.subscribe('/topic/poker/' + this.room.id + '/story/add', this.storyAddMessageReceived);
        store.getStompClient.subscribe('/topic/poker/' + this.room.id + '/selectedStory', this.storySelectedByAuthorMessageReceived)
        store.getStompClient.subscribe('/topic/poker/' + this.room.id + '/vote/open-close', this.openVotingMessageReceived);
        store.getStompClient.subscribe('/topic/poker/' + this.room.id + '/vote', this.votesMessageReceived);
        store.getStompClient.subscribe('/topic/poker/room/' + this.room.id + '/user/joined', this.userJoinedMessageReceived);
      },
      storyAddMessageReceived(payload) {
        let storyString = this.parseWSResponseBody(payload.body);
        let storyObject = JSON.parse(storyString);
        this.room.stories.push(storyObject);
      },
      storySelectedByAuthorMessageReceived(payload) {
        let storyString = this.parseWSResponseBody(payload.body);
        let selectedStory = JSON.parse(storyString);
        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i]["id"] === selectedStory["storyId"]) {
            this.room.stories[i].disabled = selectedStory["disabled"];
            this.room.selectedStory = this.room.stories[i];
            this.tab = this.room.stories[i].story;

            this.room.currentVoteResult = this.getLastVoteResult(this.room.selectedStory);
          }
        }
      },
      openVotingMessageReceived(payload) {
        let votingIsOpenString = this.parseWSResponseBody(payload.body);
        let votingIsOpen = JSON.parse(votingIsOpenString);
        this.votingIsOpen = votingIsOpen["open"];
        this.showVoteOptions = this.votingIsOpen;

        let voteResult = votingIsOpen["voteResult"];

        this.room.currentVoteResult = voteResult;

        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i].id === this.room.selectedStory.id) {
            let voteResults = this.room.stories[i].voteResults;
            this.room.stories[i].voteResults = [];
            if (voteResults !== null && voteResults.length > 0) {
              for (let j = 0; j < voteResults.length; j++) {
                if (voteResults[j].id !== voteResult.id) {
                  this.room.stories[i].voteResults.push(voteResults[j]);
                } else {
                  this.room.stories[i].voteResults.push(voteResult);
                }
              }
            } else {
              this.room.stories[i].voteResults.push(voteResult);
            }
          }
        }

      },
      votesMessageReceived(payload) {
        let refreshedCurrentVoteResult = this.parseWSResponseBody(payload.body);

        let refreshedCurrentVoteResultObj = JSON.parse(refreshedCurrentVoteResult);
        this.room.currentVoteResult = refreshedCurrentVoteResultObj;

        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i].id === this.room.selectedStory.id) {
            let voteResults = this.room.stories[i].voteResults;
            this.room.stories[i].voteResults = [];
            for (let j = 0; j < voteResults.length; j++) {
              if (voteResults[j].id !== this.room.currentVoteResult.id) {
                this.room.stories[i].voteResults.push(voteResults[j]);
              } else {
                this.room.stories[i].voteResults.push(this.room.currentVoteResult);
              }
            }
          }
        }
      },
      userJoinedMessageReceived(payload) {
        let username = this.parseWSResponseBody(payload.body);
        let usernameObj = JSON.parse(username);
        if (usernameObj["username"] !== this.username) {
          this.room.users.push(usernameObj);
        }
      },
      startVoting() {
        store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + this.room.selectedStory["id"] + "/vote/open", {});
      },
      finishVoting() {
        store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + this.room.selectedStory["id"] + "/vote/close", {});
      },
      getRoom() {
        axios.get(this.backendUrls.getRoom() + this.roomId)
            .then(response => {
              if (response.data != null) {
                this.room = response.data;
              }
            });
      },
      calculateVotesAvg() {
        if (this.room.currentVoteResult && this.room.currentVoteResult.votes) {
          let total = 0;
          for (let i = 0; i < this.room.currentVoteResult.votes.length; i++) {
            total += this.room.currentVoteResult.votes[i].value;
          }

          return total / this.room.currentVoteResult.votes.length;
        }
        return "";
      },
      getVoteValue(user) {
        if (this.room.selectedStory) {
          let voteResult = this.getLastVoteResult(this.room.selectedStory);

          if (this.room.currentVoteResult && this.room.currentVoteResult.votes) {
            let votes = this.room.currentVoteResult.votes;
            for (let i = 0; i < votes.length; i++) {
              if (votes[i].username === user.username) {
                return votes[i].value;
              }
            }
          }
        }
        return "Not voted yet!";
      },
      getLastVoteResult(story) {
        if (story !== null && story.id !== null) {
          let currStory = story;
          if (currStory !== null && currStory.voteResults) {
            if (currStory.voteResults !== null && currStory.voteResults.length > 0) {
              let lastVoteResult;
              let id = -1;
              let index;
              for (let j = 0; j < currStory.voteResults.length; j++) {
                if (currStory.voteResults[j].id > id) {
                  id = currStory.voteResults[j].id;
                  index = j;
                }
              }

              if (index != -1) {
                lastVoteResult = currStory.voteResults[index];
              }
            
              return lastVoteResult;
            }
          }
        }
        return null;
      },
      parseWSResponseBody(body) {
        body = body.substring(body.indexOf("body") + 4);
        body = body.substring(body.indexOf("{"));
        body = body.substring(0, body.indexOf("statusCode") - 2);
        return body;
      }
    }
})
</script>
