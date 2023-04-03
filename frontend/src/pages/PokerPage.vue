<template>
  <q-page v-if="spinnerVisible" class="q-pa-md flex flex-center">
    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 ">
      <div class="row justify-center" style="padding: 30px">
        <q-spinner :thickness="10" color="primary" size="5em" />
      </div>
    </div>
  </q-page>
  <q-page v-if="joinAndCreateButtonVisible && !spinnerVisible" class="q-pa-md flex flex-center">
    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" style="padding: 30px">
          <q-card>
            <q-card-section>
              <div class="text-h5">Create a new session</div>
            </q-card-section>
            <q-card-actions>
              <div class="row justify-center items-center">
                <div class="col-12">
                  <q-input v-model="author" :error="createUsernameValid" bg-color="white" error-message="Username required!"
                        label="Username" style="padding: 30px" type="text" @keydown.enter="createRoom"/>
                </div>
                <div class="col-12 text-center">
                  <q-btn color="primary" label="Create" @click="createRoom"/>
                </div>
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
                          :error-message="usernameErrorMessage" label="Username"
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
    <q-bar class="text-blue col-12 justify-center" dark data-html2canvas-ignore="true">
      <div class="text-weight-bold col-1 text-center" style="font-size:30px">
        {{ this.username }}
      </div>
      <div class="text-weight-bold col-10 text-center" style="font-size:30px">
        {{ this.room.id }}
      </div>
      <q-btn-dropdown class="col-1" v-if="true" color="primary" label="Menu">
        <q-list>
          <q-item v-close-popup clickable @click="shareTheBoard">
            <q-item-section>
              <q-item-label>Share the board</q-item-label>
            </q-item-section>
            <q-item-section avatar>
              <q-avatar color="primary" icon="ios_share" size="sm" text-color="white"/>
            </q-item-section>
          </q-item>
          <q-item v-close-popup clickable @click="exit">
            <q-item-section>
              <q-item-label>Exit</q-item-label>
            </q-item-section>
            <q-item-section avatar>
              <q-avatar color="red-13" icon="logout" size="sm" text-color="white"/>
            </q-item-section>
          </q-item>
        </q-list>
      </q-btn-dropdown>
      
    </q-bar>
    <div v-if="this.room.author == this.author" class="col-12">
      <div class="row text-center q-pa-md items-center">
        <div class="col-11">
          <q-input v-model="inputStory" label="Story name:" outlined/>
        </div>
        <div class="col-1">
          <q-btn color="primary" @click="createStory">Add</q-btn>
        </div>
      </div>
    </div>
    
    <div class="col-11">
      <q-tabs v-model="tab" active-color="primary" align="justify" 
      class="bg-grey-3 text-grey-7" dense indicator-color="primary">
        <template v-for="(story, index) in this.room.stories" :key="index">
          <div @click="selectStory(story)">
            <q-tab :disable=isStoryDisable(story) :label=story.story :name=story.story no-caps></q-tab>
          </div>
        </template>
      </q-tabs>
      <q-tabs>
        <div v-if="this.room.selectedStory == null">
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
              </q-item-section>
            </q-item>
          </template>
        </div>
      </q-tabs>
      <q-tab-panels v-model="tab" animated class="text-black">
        <template v-for="(story, index) in this.room.stories" :key="index">
          <q-tab-panel :name=story.story>
            <div v-if="!votingIsOpen" class="col-12 text-center text-blue" style="font-size:50px;"> {{ story.story }} </div>
            <div v-if="votingIsOpen" class="col-12 text-center text-blue" style="font-size:50px;"> {{ this.room.selectedStory.story }} </div>
            <div class="row justify-center items-center">
              <div class="col-xl-8 col-lg-8 col-md-8 col-sm-12 col-xs-12">
                <div class="row">
                  <div v-for="(option, index) in voteOptions.slice(0, 4)" v-bind:key="index" 
                  class="col-xs-12 col-sm-12 col-md-6 col-lg-3 col-xl-3 text-center q-pa-sm">
                    <q-btn :disable=!showVoteOptions :style=getScreenSizeForButton() :color="getVoteColor(option)" :label="option" @click="vote(option)" />
                  </div>
                </div>
                <div class="row">
                  <div v-for="(option, index) in voteOptions.slice(4, 8)" v-bind:key="index" 
                  class="col-xs-12 col-sm-12 col-md-6 col-lg-3 col-xl-3 text-center q-pa-sm">
                    <q-btn :disable=!showVoteOptions :style=getScreenSizeForButton() :color="getVoteColor(option)" :label="option" @click="vote(option)" />
                  </div>
                </div>
              </div>
              <div class="col-xl-2 col-lg-2 col-md-2 col-sm-12 col-xs-12" 
                v-if="this.room.selectedStory != null && this.author === this.room.author">
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
              <div class="col-xl-2 col-lg-2 col-md-2 col-sm-12 col-xs-12">
                <div class="row text-center">
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
import {copyToClipboard, Notify} from 'quasar'
import { Screen } from 'quasar'

const store = stompClientStore()

export default defineComponent({
  name: 'PokerPage',
  data() {
    return {
      author: null,
      inputStory: null,
      joinAndCreateButtonVisible: true,
      room: null,
      username: null,
      createUsernameValid: false,
      joinUsernameValid: false,
      roomIdValid: false,
      roomIdErrorMessage: 'Room ID required!',
      votingIsOpen: false,
      currentVoting: null,
      showVoteOptions: false,
      chosenOption: null,
      subscriptions: [],
      spinnerVisible: false,
      usernameErrorMessage: 'Username already in use!',
      frontendUrl: 'https://retrospecto.cloud',
      backendUrls: {
        pathParts: {
          poker: '/poker',
          room: '/room',
        },
        baseUrl: 'https://www.retrospecto.cloud',
        baseUrlPoker: function() { return this.baseUrl + this.pathParts.poker},
        createRoom: function() { return this.baseUrlPoker() + this.pathParts.room + "/"},
        getRoom: function() { return this.baseUrlPoker() + this.pathParts.room + "/"},
      },
      voteOptions: [1, 2, 3, 5, 7, 11, 13, 17],
      tab: 'test',
    }
  },
  created() {
    window.addEventListener("resize", this.getScreenSizeForButton);
    this.spinnerVisible = true;
    setTimeout(this.createPage, 2000);
    this.spinnerVisible = false;
  },
  methods:
    {
      getFirstLetter(username) {
        return Array.from(username)[0];
      },
      vote(voteValue) {
        this.chosenOption = voteValue;
        let vote = {
          username: this.username,
          value: voteValue,
        };

        store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + this.room.selectedStory["id"] + "/vote", {}, JSON.stringify(vote));
        this.showVoteOptions = false;
      },
      createStory() {
        if (this.inputStory) {
          store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/add", {}, this.inputStory);
          this.inputStory = null;
        }
      },
      selectStory(story) {
        if (this.author === this.room.author && !this.votingIsOpen && !story.disabled) {
          store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + story["id"] + "/selected", {});
        }
      },
      nextStory(index) {
        if (this.author === this.room.author && !this.votingIsOpen) {
          let story = this.room.stories[index+1];
          store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + story["id"] + "/selected", {});
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
                  localStorage.setItem('poker-author', this.author);
                }
              });
        }
      },
      joinRoom() {
        if (!this.roomId && !this.username) {
          this.joinUsernameValid = true;
          this.roomIdValid = true;
          this.roomIdErrorMessage = "Room ID required!";
          this.usernameErrorMessage = "Username required!";
        }
        if (!this.username && this.roomId) {
          this.joinUsernameValid = true;
          this.roomIdValid = false;
          this.usernameErrorMessage = "Username required!";
        }
        if (this.username && !this.roomId) {
          this.roomIdValid = true;
          this.roomIdErrorMessage = "Room ID required!";
          this.joinUsernameValid = false;
        }
        if (this.roomId && this.username) {
          this.joinUsernameValid = false;
          this.roomIdValid = false;
        
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

                localStorage.removeItem("poker-author");
                store.getStompClient.send("/app/poker/room/" + this.room.id + "/user/add", {}, this.username);
              }
            })
            .catch(error => {
              this.roomId = null;
              if (error.response.status == 409) {
                this.joinUsernameValid = true;
                this.usernameErrorMessage = 'Username already in use!';
              } else {
                this.roomIdValid = true;
              }
            })
        }
      },
      exit() {
        this.subscriptions.forEach(function (subscription) {
          subscription.unsubscribe()
        })
        store.getStompClient.send("/app/poker/room/" + this.room.id + "/user/remove", {}, this.username);
        this.room = null;
        this.joinAndCreateButtonVisible = true;
        this.inputStory = null;
        this.joinAndCreateButtonVisible = true;
        this.room = null;
        this.createUsernameValid = false;
        this.joinUsernameValid = false;
        this.roomIdValid = false;
        this.votingIsOpen = false;
        this.currentVoting = null;
        this.showVoteOptions = false;
        this.chosenOption = null;
        this.subscriptions = [];
        this.roomId = null;
      },
      subscribe() {
        localStorage.setItem('poker-username', this.username)
        let prefixWithRoomId = '/topic/poker/room/' + this.room.id;
        this.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/story/add', this.storyAddMessageReceived));
        this.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/selected-story', this.storySelectedByAuthorMessageReceived));
        this.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/vote/open-close', this.openVotingMessageReceived));
        this.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/vote', this.votesMessageReceived));
        this.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/user/joined', this.userJoinedMessageReceived));
        this.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/user/removed', this.userRemovedMessageReceived));
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
            console.log("Story stringify: " + JSON.stringify(this.room.selectedStory));
            console.log("Story obj: " + this.room.selectedStory);
            console.log("Story obj story: " + this.room.selectedStory.story);
            this.tab = this.room.stories[i].story;

            this.room.currentVoteResult = this.getLastVoteResult(this.room.selectedStory);
          }
        }
      },
      openVotingMessageReceived(payload) {
        let votingIsOpenString = this.parseWSResponseBody(payload.body);
        let votingIsOpen = JSON.parse(votingIsOpenString);
        this.votingIsOpen = votingIsOpen["open"];

        if (!this.votingIsOpen) {
          this.chosenOption = null;
        }

        this.showVoteOptions = this.votingIsOpen;

        let voteResult = votingIsOpen["voteResult"];

        this.room.currentVoteResult = voteResult;

        this.room.selectedStoryId = votingIsOpen["storyId"];

        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i].id == this.room.selectedStoryId) {
            this.room.selectedStory = this.room.stories[i];
          }
        }

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
          let newArr = [];
          for (let i = 0; i < this.room.users.length; i++) {
            newArr.push(this.room.users[i].username);
          }

          newArr.push(usernameObj["username"]);
          
          newArr.sort();

          this.room.users = [];

          newArr.forEach((name) => this.room.users.push({username: name}));
        }
      },
      userRemovedMessageReceived(payload) {
        let username = this.parseWSResponseBody(payload.body);
        let usernameObj = JSON.parse(username);
        
        let arr = [];

        for (let i = 0; i < this.room.users.length; i++) {
          if (this.room.users[i].username !== usernameObj["username"]) {
            arr.push(this.room.users[i]);
          }
        }

        this.room.users = arr;
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
      shareTheBoard() {
        copyToClipboard(this.frontendUrl + '/#/poker/room/' + this.room.id).then(() => {
          Notify.create({
            message: 'URL copied to clipboard!',
            color: 'blue',
            position: "center"
          })
        })
          .catch(() => {
            Notify.create({
              message: 'URL copy failed!',
              color: 'red',
              position: "center"
            })
          })
      },
      getVoteColor(voteValue) {
        return this.chosenOption == voteValue || !this.votingIsOpen ? "green" : "red";
      },
      isStoryDisable(story) {
        return story.disabled;
      },
      createPage() {
        this.spinnerVisible = false;
        if (this.$route.params.roomId != null) {
          this.roomId = this.$route.params.roomId;
        }
        if (localStorage.getItem("poker-username")) {
          this.username = localStorage.getItem("poker-username");
        }
        if (localStorage.getItem("poker-author")) {
          this.author = localStorage.getItem("poker-author");
          localStorage.removeItem("poker-username");
        }
      },
      getScreenSizeForButton() {
        if (Screen.xs) {
          return "width: 70%; height: 70%; font-size: 40px;";
        }
        if (Screen.sm) {
          return "width: 70%; height: 70%; font-size: 40px;";
        }
        if (Screen.md) {
          return "width: 70%; height: 70%; font-size: 40px;";
        }
        if (Screen.lg) {
          return "width: 70%; height: 70%; font-size: 70px;";
        }
        if (Screen.xl) {
          return "width: 70%; height: 70%; font-size: 70px;";
        }
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
