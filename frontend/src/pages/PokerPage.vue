<template>
  <q-page v-if="localFlags.spinnerVisible" class="row justify-center items-center content-center">
    <div>
      <q-spinner :thickness="10" color="primary" size="5em"/>
    </div>
  </q-page>
  <q-page v-if="localFlags.showCreateAndJoinRoomComponents" class="q-pa-md flex flex-center">
    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" style="padding: 30px">
      <q-card>
        <q-card-section>
          <div class="text-h5">Create a new session</div>
        </q-card-section>
        <q-card-actions>
          <div class="row justify-center items-center">
            <div class="col-12">
              <q-input v-model="localVariables.author" :error="localFlags.createUsernameInvalid" bg-color="white"
                       error-message="Username required!"
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
              <q-input v-model="localVariables.username" :error="localFlags.joinRoomUsernameInvalid"
                       :error-message="localFlags.joinRoomUsernameErrorMessage"
                       bg-color="white" label="Username"
                       type="text"/>
            </div>
            <div class="col-12" style="padding: 5px">
              <q-input v-model="localVariables.roomId" :error="localFlags.joinRoomIdInvalid"
                       :error-message="localFlags.joinRoomIdRequiredErrorMessage"
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
  <q-page v-else class="row q-pa-md content-start justify-center">
    <q-dialog v-model="localVariables.alert">
      <q-card>
        <q-card-section>
          <div class="text-h6">Edit story name:</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input v-model="localVariables.alertInput" type="text">
          </q-input>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" flat label="Ok" @click="sendEditMessage"/>
          <q-btn v-close-popup color="negative" flat label="Close"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <div class="col-xl-9 col-lg-9 col-md-9 col-md-10 col-sm-12 col-xs-12 q-pa-md">
      <q-card>
        <div class="row items-center">
          <div class="col-xl-2 col-lg-2 col-md-2 col-sm-12 col-xs-12 self-start">
            <template v-for="(user, index) in room.users" :key="index">
              <q-avatar :style="`left: ${index * 25}px`" class="overlapping" color="primary" size="xl"
                        text-color="white">
                {{ getFirstLetter(user.username) }}
                <q-tooltip>
                  {{ user.username }}
                </q-tooltip>
              </q-avatar>
            </template>
          </div>
          <div class="col-xl-8 col-lg-8 col-md-8 col-sm-12 col-xs-12 text-bold text-primary text-center"
               style="font-size:20px">
            Room ID: {{ room.id }}
          </div>
          <div class="col-xl-2 col-lg-2 col-md-2 col-sm-12 col-xs-12 text-right q-pt-md q-pr-md q-gutter-sm">
            <q-btn color="primary" icon="ios_share" round size="md" @click="shareTheBoard"/>
            <q-btn color="red-13" icon="logout" round size="md" @click="exit"/>
          </div>
        </div>
        <div class="row">
          <div class="col-12 full-width">
            <div class="row q-pa-md items-center">
              <div class="col-12">
                <q-input v-model="localVariables.inputStory" :disable="!isCurrentUserAuthor()" label="Add story:"
                         outlined
                         @keydown.enter="createStory"/>
              </div>
            </div>
          </div>
        </div>
      </q-card>
    </div>

    <div class="col-xl-9 col-lg-9 col-md-9 col-md-10 col-sm-12 col-xs-12 q-pa-md">
      <q-card>
        <q-tabs v-model="localVariables.tab" active-color="primary" align="justify"
                class="bg-grey-3 text-grey-7" dense indicator-color="primary">
          <template v-for="(story, index) in room.stories" :key="index">
            <div @click="selectStory(story)">
              <q-tab :disable=isStoryDisable(story) :label=story.storyName :name=story.storyName no-caps></q-tab>
            </div>
          </template>
        </q-tabs>
        <q-tab-panels v-model="localVariables.tab" animated class="text-black">
          <template v-for="(story, index) in room.stories" :key="index">
            <q-tab-panel :name=story.storyName>
              <div class="row">
                <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-xs-12 q-pa-md">
                  <q-card>
                    <div class="row q-pa-md items-center">
                      <div class="col-xl-7 col-lg-7 col-md-7 col-sm-12 col-xs-12">
                        <div class="row items-center">
                          <div class="col-10">
                            <div class="text-h5 text-primary text-bold q-pa-md">Story name: {{ story.storyName }}</div>
                            <div class="text-subtitle1 text-primary text-bold q-pa-md">
                              {{ calculateVotesAvg() }}
                            </div>
                          </div>
                          <div class="col-2 q-gutter-sm text-center">
                            <q-btn color="warning" icon="edit" round
                                   size="md" @click="enableStoryEditAlert(story.id)"/>
                            <q-btn color="negative" icon="delete" round
                                   size="md" @click="deleteStory(story.id)"/>
                          </div>
                        </div>
                      </div>
                      <q-separator :class="isSmallOrMediumScreenFullWidth()" :vertical="isSmallOrMediumScreenVertical()"
                                   inset></q-separator>
                      <div :class="isSmallOrMediumScreen()">
                        <div class="row">
                          <div class="col-xl-4 col-lg-4 col-md-4 col-sm-4 col-xs-4">
                            <q-btn :disable="isButtonEnabled('start', index)" :size="getButtonSize()"
                                   color="secondary" icon="play_arrow" round @click="startVoting">
                              <q-tooltip>Start voting</q-tooltip>
                            </q-btn>
                          </div>
                          <div class="col-xl-4 col-lg-4 col-md-4 col-sm-4 col-xs-4">
                            <q-btn :disable="isButtonEnabled('stop', index)" :size="getButtonSize()"
                                   color="negative" icon="stop" round @click="finishVoting">
                              <q-tooltip>Stop voting</q-tooltip>
                            </q-btn>
                          </div>
                          <div class="col-xl-4 col-lg-4 col-md-4 col-sm-4 col-xs-4">
                            <q-btn :disable="isButtonEnabled('next', index)"
                                   :size="getButtonSize()" color="primary" icon="arrow_forward_ios" round
                                   @click="nextStory(index, true)">
                              <q-tooltip>Next story</q-tooltip>
                            </q-btn>
                          </div>
                        </div>
                      </div>
                    </div>
                    <q-separator inset/>
                    <div class="row">
                      <div v-if="!isSmallScreenAndVotingOpen()" :class="isRowViewOrColumn('users')">
                        <div class="row">
                          <template v-for="(user, index) in room.users" :key="index">
                            <div :class="isRowViewOrColumnUsers()" style="height: 45px">
                              <q-card>
                                <div class="row items-center q-pa-xs">
                                  <div :class="getUserNameStyle(user.username)" class="col-10 text-center items-center">
                                    {{ user.username }}
                                  </div>
                                  <div
                                    class="col-2 rounded-borders text-center text-white bg-primary text-bold text-h6">
                                    {{ getVoteValue(user) }}
                                  </div>
                                </div>
                              </q-card>
                            </div>
                          </template>
                        </div>
                      </div>
                      <div v-if="localVariables.isRowView" class="col-12">
                        <q-separator inset/>
                      </div>
                      <div :class="isRowViewOrColumn('numbers')">
                        <div :id="'storyButtons'+story.storyName" class="row">
                          <template v-for="(option, index) in localVariables.voteOptions"
                                    v-bind:key="index">
                            <div
                              class="col-xl-3 col-lg-3 col-md-3 col-sm-6 col-xs-6 text-center items-center q-pa-sm">
                              <q-btn :color="getVoteColor(option)" :disable=!localFlags.showVoteOptions
                                     :label="option"
                                     :style="getScreenSizeForButton()"
                                     class="full-height full-width" @click="vote(option)"/>
                            </div>
                          </template>
                        </div>
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
    <q-page-sticky v-if="!localFlags.showCreateAndJoinRoomComponents" :offset="[18, 18]" position="bottom-right">
      <q-fab color="primary" direction="up" icon="space_dashboard">
        <q-fab-action v-if="!localVariables.isRowViewMenu" color="primary" icon="table_rows"
                      @click="changeRowViewTrue()"/>
        <q-fab-action v-if="localVariables.isRowViewMenu" color="primary" icon="view_column"
                      @click="changeRowViewFalse()"/>
      </q-fab>
    </q-page-sticky>
  </q-page>
</template>

<script>
import {defineComponent, watch} from 'vue';
import axios from 'axios';
import {stompClientStore} from 'stores/stomp';
import {copyToClipboard, Notify, Screen} from 'quasar';
import {storeToRefs} from "pinia";

const store = stompClientStore()
const {stompClientConnected} = storeToRefs(store)

export default defineComponent({
  name: 'PokerPage',
  data() {
    return {
      localVariables: {
        author: null,
        username: null,
        inputStory: null,
        roomId: null,
        chosenOption: null,
        subscriptions: [],
        voteOptions: [1, 2, 3, 5, 7, 11, 13, 17],
        tab: null,
        isRowView: true,
        isRowViewMenu: true,
        alert: false,
        alertInput: null,
        alertStoryId: null
      },
      room: {
        id: null,
        author: {
          username: null,
          sessionId: null
        },
        stories: [
          {
            id: null,
            storyName: null,
            disabled: null,
            voteResults: [
              {
                id: null,
                locked: null,
                votes: [
                  {
                    user: {
                      username: null,
                      sessionId: null,
                    },
                    value: null,
                  }
                ]
              }
            ]
          }
        ],
        selectedStoryId: null,
        users: [
          {
            username: null,
          }
        ],
        selectedStory: {
          id: null,
          storyName: null,
          disabled: null,
          voteResults: [
            {
              id: null,
              locked: null,
              votes: [
                {
                  user: {
                    username: null,
                    sessionId: null,
                  },
                  value: null,
                }
              ]
            }
          ]
        }
      },
      localFlags: {
        createUsernameInvalid: false,
        joinRoomUsernameInvalid: false,
        joinRoomIdInvalid: false,
        joinRoomIdRequiredErrorMessage: 'Room ID required!',
        joinRoomUsernameErrorMessage: 'Username already in use!',
        spinnerVisible: true,
        showCreateAndJoinRoomComponents: true,
        votingIsOpen: false,
        showVoteOptions: false,
      },
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
          return this.baseUrlPoker() + this.pathParts.room
        },
        getRoom: function () {
          return this.baseUrlPoker() + this.pathParts.room + "/"
        },
      },
    }
  },
  created() {
    window.addEventListener("resize", this.getScreenSizeForButton);
    setTimeout(this.createPage, 2000);
    watch(stompClientConnected, () => {
      if (!stompClientConnected) {
        this.exit();
      }
    });
    window.addEventListener("beforeunload", this.exit);
  },
  methods:
    {
      setStateToDefault() {
        this.localFlags = {
          createUsernameInvalid: false,
          joinRoomUsernameInvalid: false,
          joinRoomIdInvalid: false,
          joinRoomIdRequiredErrorMessage: 'Room ID required!',
          joinRoomUsernameErrorMessage: 'Username already in use!',
          spinnerVisible: true,
          showCreateAndJoinRoomComponents: true,
          votingIsOpen: false,
          showVoteOptions: false,
        };

        this.localVariables = {
          author: localStorage.getItem('poker-author') !== null ? JSON.parse(localStorage.getItem('poker-author')).username : null,
          username: localStorage.getItem('poker-username'),
          inputStory: null,
          roomId: this.localVariables.roomId,
          chosenOption: null,
          subscriptions: [],
          voteOptions: [1, 2, 3, 5, 7, 11, 13, 17],
          tab: null,
        };

        this.room = null;
      },
      getFirstLetter(username) {
        return Array.from(username)[0];
      },
      vote(voteValue) {
        this.localVariables.chosenOption = voteValue;
        let vote = {
          user: {
            username: this.localVariables.username,
            sessionId: store.getPokerSessionId
          },
          value: voteValue,
        };

        store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + this.room.selectedStory["id"] + "/vote", {}, JSON.stringify(vote));
        this.localFlags.showVoteOptions = false;
      },
      createStory() {
        if (this.localVariables.inputStory) {
          store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/add", {}, this.localVariables.inputStory);
          this.localVariables.inputStory = null;
        }
      },
      selectStory(story) {
        if (this.isCurrentUserAuthor() && !this.localFlags.votingIsOpen && !story.disabled) {
          store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + story["id"] + "/selected", {});
        }
      },
      nextStory(index) {
        if (this.isCurrentUserAuthor() && !this.localFlags.votingIsOpen) {
          let story = this.room.stories[index + 1];
          store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + story["id"] + "/selected", {});
        }
      },
      createRoom() {
        if (!this.localVariables.author) {
          this.localFlags.createUsernameInvalid = true;
        } else {
          this.room = null;

          store.generatePokerSessionId();

          let config = {
            headers: {
              'Content-Type': 'application/json',
            }
          };

          let user = {
            username: this.localVariables.author,
            sessionId: store.getPokerSessionId
          }

          axios.post(this.backendUrls.createRoom(), JSON.stringify(user), config)
            .then(response => {
              if (response.data != null) {
                this.room = response.data;
                this.room.author.sessionId = store.getPokerSessionId;
                this.localFlags.showCreateAndJoinRoomComponents = false;
                this.localVariables.username = this.localVariables.author;
                this.subscribe();

                localStorage.setItem('poker-author', JSON.stringify({
                  username: this.localVariables.author,
                  sessionId: store.getPokerSessionId
                }));
                localStorage.setItem('poker-username', this.localVariables.author);
              }
            })
            .catch(error => {
              console.log("Create room error: " + JSON.stringify(error));
            });
        }
      },
      joinRoom() {
        this.localFlags.joinRoomIdInvalid = !this.localVariables.roomId;

        if (!this.localVariables.username) {
          this.localFlags.joinRoomUsernameInvalid = true;
          this.localFlags.joinRoomUsernameErrorMessage = "Username required!";
        } else {
          this.localFlags.joinRoomUsernameInvalid = false;
        }

        if (this.localVariables.roomId && this.localVariables.username) {
          this.localFlags.joinRoomIdInvalid = false;
          this.localFlags.joinRoomUsernameInvalid = false;

          axios.get(this.backendUrls.getRoom() + this.localVariables.roomId)
            .then(response => {
              if (response.data != null) {
                this.room = response.data;
                this.localFlags.showCreateAndJoinRoomComponents = false;
                this.subscribe();

                for (let i = 0; i < this.room.stories.length; i++) {
                  if (this.room.stories[i].id === this.room.selectedStoryId) {
                    this.room.selectedStory = this.room.stories[i];
                    this.localVariables.tab = this.room.selectedStory.storyName;
                  }
                }

                localStorage.setItem('poker-username', this.localVariables.username);
                this.room.currentVoteResult = this.getLastVoteResult(this.room.selectedStory);

                let user = {username: this.localVariables.username, sessionId: store.getPokerSessionId};
                store.getStompClient.send("/app/poker/room/" + this.room.id + "/user/add", {}, JSON.stringify(user));

                store.generatePokerSessionId();
              }
            })
            .catch(error => {
              this.localVariables.roomId = null;
              if (error.response.status === 409) {
                this.localFlags.joinRoomUsernameInvalid = true;
                this.localFlags.joinRoomUsernameErrorMessage = 'Username already in use!';
              } else {
                this.localFlags.joinRoomIdInvalid = true;
              }
            });
        }
      },
      exit() {
        this.localVariables.subscriptions.forEach(function (subscription) {
          subscription.unsubscribe()
        })
        store.getStompClient.send("/app/poker/room/" + this.room.id + "/user/remove", {}, this.localVariables.username);
        store.setPokerRoomIdNull();

        this.setStateToDefault();
        setTimeout(() => {
          this.localFlags.spinnerVisible = false;
        }, 1000);
      },
      subscribe() {
        let prefixWithRoomId = '/topic/poker/room/' + this.room.id;
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/story/add', this.storyAddMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/selected-story', this.storySelectedByAuthorMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/vote/open-close', this.openVotingMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/vote', this.votesMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/user/joined', this.userJoinedMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(prefixWithRoomId + '/user/removed', this.userRemovedMessageReceived));
        store.setPokerUsernameAuthorRoomId(this.localVariables.username, this.room.author.username, this.room.id);
      },
      storyAddMessageReceived(payload) {
        let storyString = this.parseWSResponseBody(payload.body);
        let storyObject = JSON.parse(storyString);

        this.room.stories.push(storyObject);

        if (this.room.stories.length === 1) {
          storyObject.disabled = false;
          store.getStompClient.send("/app/poker/room/" + this.room.id + "/story/" + this.room.stories[0]["id"] + "/selected", {});
        }

      },
      storySelectedByAuthorMessageReceived(payload) {
        let storyString = this.parseWSResponseBody(payload.body);
        let selectedStory = JSON.parse(storyString);
        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i]["id"] === selectedStory["storyId"]) {
            this.room.stories[i].disabled = selectedStory["disabled"];
            this.room.selectedStory = this.room.stories[i];
            this.localVariables.tab = this.room.stories[i].storyName;

            this.room.currentVoteResult = this.getLastVoteResult(this.room.selectedStory);
          }
        }
      },
      openVotingMessageReceived(payload) {
        let votingIsOpenString = this.parseWSResponseBody(payload.body);
        let votingIsOpen = JSON.parse(votingIsOpenString);
        this.localFlags.votingIsOpen = votingIsOpen["open"];

        if (!this.localFlags.votingIsOpen) {
          this.localVariables.chosenOption = null;
        }

        this.localFlags.showVoteOptions = this.localFlags.votingIsOpen;

        let voteResult = votingIsOpen["voteResult"];

        this.room.currentVoteResult = voteResult;

        this.room.selectedStoryId = votingIsOpen["storyId"];

        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i].id === this.room.selectedStoryId) {
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

        this.room.currentVoteResult = JSON.parse(refreshedCurrentVoteResult);

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
        let user = this.parseWSResponseBody(payload.body);
        let userObj = JSON.parse(user);

        let newArr = [];
        for (let i = 0; i < this.room.users.length; i++) {
          newArr.push(this.room.users[i].username);
        }

        newArr.push(userObj["username"]);

        newArr.sort();

        this.room.users = [];

        newArr.forEach((name) => this.room.users.push({username: name}));
      },
      userRemovedMessageReceived(payload) {
        let user = this.parseWSResponseBody(payload.body);
        let userObj = JSON.parse(user);

        let arr = [];

        for (let i = 0; i < this.room.users.length; i++) {
          if (this.room.users[i].username !== userObj["username"]) {
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
              if (votes[i].user.username === user.username) {
                return votes[i].value;
              }
            }
          }
        }
        return "?";
      },
      getLastVoteResult(story) {
        if (story && story.id !== null) {
          let currStory = story;
          if (currStory !== null && currStory.voteResults) {
            if (currStory.voteResults.length > 0) {
              let lastVoteResult;
              let id = -1;
              let index;
              for (let j = 0; j < currStory.voteResults.length; j++) {
                if (currStory.voteResults[j].id > id) {
                  id = currStory.voteResults[j].id;
                  index = j;
                }
              }

              if (index !== -1) {
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
        return this.localVariables.chosenOption === voteValue || !this.localFlags.votingIsOpen ? "red" : "green";
      },
      getUserNameStyle(username) {
        if (username.length <= 4) {
          return 'text-blue text-weight-bold text-h5'
        }
        if (username.length > 4 && username.length <= 7) {
          return 'text-blue text-weight-bold text-h5'
        }
        if (username.length > 7) {
          return 'text-blue text-weight-bold text-h6'
        }
      },
      isStoryDisable(story) {
        return story.disabled;
      },
      createPage() {
        if (store.getStompClientStatus && store.getPokerRoomId) {
          this.localVariables.roomId = store.getPokerRoomId;
          this.localVariables.username = store.getPokerUsername;
          this.localVariables.author = store.getPokerAuthor;
          axios.get(this.backendUrls.getRoom() + this.localVariables.roomId)
            .then(response => {
              if (response.data != null) {
                this.room = response.data;
                this.localFlags.showCreateAndJoinRoomComponents = false;
                this.subscribe();

                for (let i = 0; i < this.room.stories.length; i++) {
                  if (this.room.stories[i].id === this.room.selectedStoryId) {
                    this.room.selectedStory = this.room.stories[i];
                    this.localVariables.tab = this.room.selectedStory.storyName;
                  }
                }

                localStorage.setItem('poker-username', this.localVariables.username);
                this.room.currentVoteResult = this.getLastVoteResult(this.room.selectedStory);

                let user = {username: this.localVariables.username, sessionId: store.getPokerSessionId};
                store.getStompClient.send("/app/poker/room/" + this.room.id + "/user/add", {}, JSON.stringify(user));

                this.localFlags.spinnerVisible = false;
              }
            })
            .catch(error => {
              this.localVariables.roomId = null;
              if (error.response.status === 409) {
                this.localFlags.joinRoomUsernameInvalid = true;
                this.localFlags.joinRoomUsernameErrorMessage = 'Username already in use!';
              } else {
                this.localFlags.joinRoomIdInvalid = true;
              }
              this.localFlags.spinnerVisible = false;
            })
        } else {
          if (this.$route.params.roomId != null) {
            this.localVariables.roomId = this.$route.params.roomId;
          }
          if (localStorage.getItem("poker-username")) {
            this.localVariables.username = localStorage.getItem("poker-username");
          }
          if (localStorage.getItem("poker-author")) {
            this.localVariables.author = JSON.parse(localStorage.getItem("poker-author")).username;
          }
          this.localFlags.spinnerVisible = false;
        }
      },
      getScreenSizeForButton() {
        if (Screen.xs) {
          return "font-size: 20px;";
        }
        if (Screen.sm) {
          return "font-size: 30px;";
        }
        if (Screen.md) {
          return "font-size: 50px;";
        }
        if (Screen.lg) {
          return "font-size: 60px;";
        }
        if (Screen.xl) {
          return "font-size: 60px;";
        }
      },
      getButtonSize() {
        if (Screen.xs) {
          return "md";
        }
        if (Screen.sm) {
          return "lg";
        }
        if (Screen.md) {
          return "lg";
        }
        if (Screen.lg) {
          return "xl";
        }
        if (Screen.xl) {
          return "xl";
        }
      },
      isSmallScreenAndVotingOpen() {
        return Screen.xs && this.localFlags.votingIsOpen;
      },
      isSmallOrMediumScreen() {
        if (Screen.xs || Screen.sm) {
          return 'col-12 q-pa-md text-center'
        } else {
          return 'col-4 text-right'
        }
      },
      isSmallOrMediumScreenVertical() {
        return !(Screen.xs || Screen.sm);
      },
      isSmallOrMediumScreenFullWidth() {
        if (Screen.xs || Screen.sm) {
          return 'full-width'
        } else {
          return ''
        }
      },
      isRowViewOrColumn(card) {
        if (this.localVariables.isRowView) {
          return 'col-12 q-pa-md'
        } else {
          switch (card) {
            case 'users':
              return 'col-xl-6 col-lg-6 col-md-6 col-sm-6 col-xs-12 q-pa-md order-last'
            case 'numbers':
              return 'col-xl-6 col-lg-6 col-md-6 col-sm-6 col-xs-12 q-pa-md'
          }
        }
      },
      isRowViewOrColumnUsers() {
        if (this.localVariables.isRowView) {
          return 'col-xl-3 col-lg-3 col-md-3 col-sm-6 col-xs-12 items-center q-pl-sm q-pr-sm'
        } else {
          return 'col-xl-6 col-lg-6 col-md-6 col-sm-12 col-xs-12 items-center q-pl-sm q-pr-sm'
        }
      },
      changeRowViewTrue() {
        this.localVariables.isRowView = true;
        this.localVariables.isRowViewMenu = true
      },
      changeRowViewFalse() {
        this.localVariables.isRowView = false;
        this.localVariables.isRowViewMenu = false
      },
      isButtonEnabled(button, index) {
        switch (button) {
          case 'start':
            if (this.isCurrentUserAuthor()) {
              return this.localFlags.votingIsOpen
            } else {
              return true
            }
          case 'stop':
            if (this.isCurrentUserAuthor()) {
              return !this.localFlags.votingIsOpen
            } else {
              return true
            }
          case 'next':
            if (this.isCurrentUserAuthor()) {
              return this.localFlags.votingIsOpen || index === this.room.stories.length - 1
            } else {
              return true
            }
        }
      },
      getHeightOfStoryCard(storyName) {
        let element = document.getElementById('storyCard' + storyName)
        if (element) {
          return element.getBoundingClientRect().height
        } else {
          return '310'
        }
      },
      setMinimumHeight(storyName) {
        return 'min-height: ' + this.getHeightOfStoryCard(storyName) + 'px'
      },
      parseWSResponseBody(body) {
        body = body.substring(body.indexOf("body") + 4);
        body = body.substring(body.indexOf("{"));
        body = body.substring(0, body.indexOf("statusCode") - 2);
        return body;
      },
      isCurrentUserAuthor() {
        return this.room.author.username === this.localVariables.author && this.room.author.sessionId === store.getPokerSessionId;
      },
      deleteStory(storyId) {
        //TODO delete story based on storyId
      },
      enableStoryEditAlert(storyId) {
        this.localVariables.alert = true
        this.localVariables.alertStoryId = storyId
      },
      sendEditMessage() {
        //TODO send alertInput to alertStoryId
        console.log(this.localVariables.alertInput)
      }
    }
})
</script>

<style lang="sass" scoped>
.overlapping
  border: 2px solid white
  position: absolute
</style>
