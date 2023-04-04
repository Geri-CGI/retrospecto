<template>
  <q-page v-if="spinnerVisible" class="row justify-center items-center content-center">
    <div>
      <q-spinner :thickness="10" color="primary" size="5em"/>
    </div>
  </q-page>
  <q-page v-if="joinAndCreateButtonVisible" class="q-pa-md flex flex-center">
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
              <q-input v-model="username" :error="joinUsernameValid" :error-message="usernameErrorMessage"
                       bg-color="white" label="Username"
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
    <div class="col-12 q-pa-md">
      <q-card>
        <div class="row full-width text-weight-bold text-center text-primary items-center q-pa-xs">
          <div class="col-2 text-left" style="font-size:20px">
            Current user: {{ this.username }}
          </div>
          <div class="col-8" style="font-size:20px">
            Room ID: {{ this.room.id }}
          </div>
          <div class="col-2">
            <div class="row">
              <div class="col-10 text-right">
                <q-btn color="primary" icon="ios_share" round size="sm" @click="shareTheBoard"/>
              </div>
              <div class="col-2">
                <q-btn color="red-13" icon="logout" round size="sm" @click="exit"/>
              </div>
            </div>
          </div>
          <div v-if="this.room.author == this.author" class="col-12 full-width">
            <div class="row q-pa-md items-center">
              <div class="col-12">
                <q-input v-model="inputStory" label="Add story:" outlined @keydown.enter="createStory"/>
              </div>
            </div>
          </div>
        </div>
      </q-card>
    </div>

    <div class="col-12 q-pa-md">
      <q-card>
        <q-tabs v-model="tab" active-color="primary" align="justify"
                class="bg-grey-3 text-grey-7" dense indicator-color="primary">
          <template v-for="(story, index) in this.room.stories" :key="index">
            <div @click="selectStory(story)">
              <q-tab :disable=isStoryDisable(story) :label=story.storyName :name=story.storyName no-caps></q-tab>
            </div>
          </template>
        </q-tabs>
        <q-tab-panels v-model="tab" animated class="text-black">
          <template v-for="(story, index) in this.room.stories" :key="index">
            <q-tab-panel :name=story.storyName>
              <div class="row justify-center items-start q-pa-md">
                <div class="col-xl-4 col-lg-4 col-md-4 col-sm-12 col-xs-12 ">
                  <q-card class="self-stretch">
                    <q-card-section>
                      <div v-if="!votingIsOpen" class="text-h6">{{ story.storyName }}</div>
                      <div v-if="votingIsOpen" class="text-h6">{{ this.room.selectedStory.storyName }}</div>
                      <div v-if="!votingIsOpen" class="text-subtitle2">{{ calculateVotesAvg() }}</div>
                    </q-card-section>
                    <q-separator/>
                    <q-card-section>
                      <template v-for="(user, index) in room.users" :key="index">
                        <q-item v-ripple>
                          <q-item-section>
                            <q-item-label class="text-blue text-weight-bold text-h5">{{ user.username }}</q-item-label>
                            <q-item-label v-if="!votingIsOpen || !showVoteOptions" caption> {{
                                getVoteValue(user)
                              }}
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                      </template>
                    </q-card-section>
                    <q-separator/>
                    <q-card-actions v-if="this.room.selectedStory != null && this.author === this.room.author" align="center"
                                    vertical>
                      <q-btn :disable="this.votingIsOpen" color="secondary" style="width: 30%" @click="startVoting">
                        Start voting
                      </q-btn>
                      <q-btn :disable="!this.votingIsOpen" color="negative" style="width: 30%" @click="finishVoting">
                        Close voting
                      </q-btn>
                      <q-btn :disable="this.votingIsOpen || index == this.room.stories.length - 1" color="primary"
                             style="width: 30%"
                             @click="nextStory(index, true)">Next story
                      </q-btn>
                    </q-card-actions>
                  </q-card>
                </div>
                <div class="col-xl-8 col-lg-8 col-md-8 col-sm-12 col-xs-12 q-pl-md">
                  <q-card>
                    <div class="row">
                      <div v-for="(option, index) in voteOptions.slice(0, 4)" v-bind:key="index"
                           class="col-xs-12 col-sm-12 col-md-6 col-lg-3 col-xl-3 text-center q-pa-sm">
                        <q-btn :color="getVoteColor(option)" :disable=!showVoteOptions :label="option"
                               :style=getScreenSizeForButton() @click="vote(option)"/>
                      </div>
                    </div>
                    <div class="row">
                      <div v-for="(option, index) in voteOptions.slice(4, 8)" v-bind:key="index"
                           class="col-xs-12 col-sm-12 col-md-6 col-lg-3 col-xl-3 text-center q-pa-sm">
                        <q-btn :color="getVoteColor(option)" :disable=!showVoteOptions :label="option"
                               :style=getScreenSizeForButton() @click="vote(option)"/>
                      </div>
                    </div>
                  </q-card>
                </div>
              </div>
            </q-tab-panel>
          </template>
        </q-tab-panels>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import axios from 'axios'
import {stompClientStore} from 'stores/stomp'
import {copyToClipboard, Notify, Screen} from 'quasar'

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
      roomId: null,
      roomIdValid: false,
      roomIdErrorMessage: 'Room ID required!',
      votingIsOpen: false,
      currentVoting: null,
      showVoteOptions: false,
      chosenOption: null,
      subscriptions: [],
      spinnerVisible: true,
      usernameErrorMessage: 'Username already in use!',
      frontendUrl: 'https://www.retrospecto.cloud',
      backendUrls: {
        pathParts: {
          poker: '/poker',
          room: '/room',
        },
        baseUrl: 'https://www.retrospecto.cloud',
        baseUrlPoker: function () {
          return this.baseUrl + this.pathParts.poker
        },
        createRoom: function () {
          return this.baseUrlPoker() + this.pathParts.room + "/"
        },
        getRoom: function () {
          return this.baseUrlPoker() + this.pathParts.room + "/"
        },
      },
      voteOptions: [1, 2, 3, 5, 7, 11, 13, 17],
      tab: 'test',
    }
  },
  created() {
    window.addEventListener("resize", this.getScreenSizeForButton);
    setTimeout(this.createPage, 2000);
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
          let story = this.room.stories[index + 1];
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
                    this.tab = this.room.selectedStory.storyName;
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

        if (this.room.stories.length == 1) {
          storyObject.disabled = false;
          this.selectStory(this.room.stories[0]);
        }

      },
      storySelectedByAuthorMessageReceived(payload) {
        let storyString = this.parseWSResponseBody(payload.body);
        let selectedStory = JSON.parse(storyString);
        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i]["id"] === selectedStory["storyId"]) {
            this.room.stories[i].disabled = selectedStory["disabled"];
            this.room.selectedStory = this.room.stories[i];
            this.tab = this.room.stories[i].storyName;

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

          return 'Average: ' + total / this.room.currentVoteResult.votes.length;
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
        this.spinnerVisible = false;
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
