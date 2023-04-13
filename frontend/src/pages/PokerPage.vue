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
          <q-input v-model="localVariables.alertInput" :rules="[ val => val && val.trim().length > 0 || 'Please type something']" type="text" @keydown.enter="sendEditMessage">
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
            <q-btn v-if="false" color="secondary" icon="settings" round size="md" @click="openSettings"/>
            <q-btn color="primary" icon="ios_share" round size="md" @click="shareTheBoard"/>
            <q-btn color="red-13" icon="logout" round size="md" @click="exit"/>
          </div>
        </div>
        <div v-if="isCurrentUserAuthor()" class="row">
          <div class="col-12 full-width">
            <div class="row q-pa-md items-center">
              <div class="col-12">
                <q-input v-model="localVariables.inputStory" 
                         label="Add story:"
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
              <q-tab :disable="getStoryDisabled(story)" :label=story.storyName :name=story.storyName no-caps></q-tab>
            </div>
          </template>
        </q-tabs>
        <q-tab-panels v-model="localVariables.tab" animated class="text-black">
          <template v-for="(story, index) in room.stories" :key="index">
            <q-tab-panel :name=story.storyName>
              <div v-if="this.localFlags.votingIsOpen" class="row justify-center">
                  <div class="text-h5 text-primary text-bold q-pa-md">Voting has started !!!</div>
              </div>
              <div class="row">
                <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-xs-12 q-pa-md">
                  <q-card>
                    <div class="row q-pa-md items-center">
                      <div class="col-xl-7 col-lg-7 col-md-7 col-sm-12 col-xs-12">
                        <div class="row items-center">
                          <div class="col-10">
                            <div class="text-h5 text-primary text-bold q-pa-md">Story name: {{ getStoryName(story.storyName) }}</div>
                            <div class="text-subtitle1 text-primary text-bold q-pa-md">
                              {{ calculateVotesAvg() }}
                            </div>
                          </div>
                          <div v-if="isCurrentUserAuthor()" class="col-2 q-gutter-sm text-center">
                            <q-btn :disabled="localFlags.votingIsOpen" color="warning" icon="edit" round
                                   size="md" @click="enableStoryEditAlert(story.id)">
                                <q-tooltip>Edit story</q-tooltip>
                            </q-btn>
                            <q-btn :disabled="localFlags.votingIsOpen" color="negative" icon="delete" round
                                   size="md" @click="deleteStory(story.id)">
                                <q-tooltip>Remove story</q-tooltip>
                            </q-btn>
                          </div>
                          <div v-if="!isCurrentUserAuthor()" class="col-2 q-gutter-sm text-center">
                            <q-btn :disabled="localFlags.votingIsOpen || localVariables.selectedStory.id === room.selectedStory.id" color="primary" icon="screen_share" round
                                   size="md" @click="jumpToCurrentStory">
                                <q-tooltip>Jump to current story</q-tooltip>
                            </q-btn>
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
        alertStoryId: null,
        selectedStory: null,
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

        baseUrlPoker: function() { return this.baseUrl + this.pathParts.poker; },
        createRoom: function() { return this.baseUrlPoker() + this.pathParts.room; },
        getRoom: function() { return this.baseUrlPoker() + this.pathParts.room + "/"; },

        placeHolders: {
          roomId: "{roomId}",
          storyId: "{storyId}",
        },
        
        prefixAppPokerRoom: "/app/poker/room",
        roomWithIdPlaceholder: function() { return this.prefixAppPokerRoom + "/" + this.placeHolders.roomId; },
        story: function() { return this.roomWithIdPlaceholder() + "/story"; },
        storyWithIdPlaceholder() { return this.story() + "/" + this.placeHolders.storyId },
        smUserAdd: function() { return this.roomWithIdPlaceholder() + "/user/add"; },
        smUserRemove: function() { return this.roomWithIdPlaceholder() + "/user/remove"; },
        smStoryAdd: function() { return this.story() + "/add"; },
        smStoryEdit: function() { return this.storyWithIdPlaceholder() + "/edit"; },
        smStoryDelete: function() { return this.storyWithIdPlaceholder() + "/remove"; },
        smStorySelect: function() { return this.storyWithIdPlaceholder() + "/select"; },
        smVote: function() { return this.storyWithIdPlaceholder() + "/vote"; },
        smVoteOpen: function() { return this.storyWithIdPlaceholder() + "/vote/open"; },
        smVoteClose: function() { return this.storyWithIdPlaceholder() + "/vote/close"; },          
        
        prefixWithRoomId: function() { return '/topic/poker/room/' + this.placeHolders.roomId; },
        prefixWithRoomIdAndStory: function() { return this.prefixWithRoomId() + "/story"; },
        prefixWithRoomIdAndUser: function() { return this.prefixWithRoomId() + "/user"; },
        prefixWithRoomIdAndVote: function() { return this.prefixWithRoomId() + "/vote"; },
        stUserAdd: function() { return this.prefixWithRoomIdAndUser() + "/joined"; },
        stUserRemove: function() { return this.prefixWithRoomIdAndUser() + "/remove"; },
        stStoryAdd: function() { return this.prefixWithRoomIdAndStory() + "/add"; },
        stStoryEdit: function() { return this.prefixWithRoomIdAndStory() + "/edit"; },
        stStoryDelete: function() { return this.prefixWithRoomIdAndStory() + "/remove"; },
        stStorySelect: function() { return this.prefixWithRoomIdAndStory() + "/select"; },
        stVote: function() { return this.prefixWithRoomIdAndVote(); },
        stVoteOpenClose: function() { return this.prefixWithRoomIdAndVote() + "/open-close"; },          
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
          alert: false,
          alertInput: null,
          alertStoryId: null,
          selectedStory: null,
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

        store.getStompClient.send(
          this.replaceRoomIdAndStoryId(this.backendUrls.smVote(), this.room.id, this.room.selectedStory["id"]),
          {}, 
          JSON.stringify(vote)
        );

        this.localFlags.showVoteOptions = false;
      },
      createStory() {
        if (this.localVariables.inputStory) {
          store.getStompClient.send(
            this.replaceRoomId(this.backendUrls.smStoryAdd(), this.room.id), 
            {}, 
            this.localVariables.inputStory
          );
          this.localVariables.inputStory = null;
        }
      },
      selectStory(story) {
        if (!story.disabled) {
          this.localVariables.selectedStory = story;
        }

        if (this.isCurrentUserAuthor() && !this.localFlags.votingIsOpen && !story.disabled) {
          store.getStompClient.send(
            this.replaceRoomIdAndStoryId(this.backendUrls.smStorySelect(), this.room.id, story["id"]), 
            {}
          );
        }
      },
      nextStory(index) {
        if (this.isCurrentUserAuthor() && !this.localFlags.votingIsOpen) {
          let story = this.room.stories[index + 1];
          store.getStompClient.send(
            this.replaceRoomIdAndStoryId(this.backendUrls.smStorySelect(), this.room.id, story["id"]),
            {}
          );
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
                this.subscribe();
                this.room.author.sessionId = store.getPokerSessionId;
                this.localFlags.showCreateAndJoinRoomComponents = false;
                this.localVariables.username = this.localVariables.author;

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
                this.subscribe();
                this.localFlags.showCreateAndJoinRoomComponents = false;

                for (let i = 0; i < this.room.stories.length; i++) {
                  if (this.room.stories[i].id === this.room.selectedStoryId) {
                    this.room.selectedStory = this.room.stories[i];
                    this.localVariables.selectedStory = this.room.selectedStory;
                    this.localVariables.tab = this.room.selectedStory.storyName;
                  }
                }

                localStorage.setItem('poker-username', this.localVariables.username);
                this.room.currentVoteResult = this.getLastVoteResult(this.room.selectedStory);

                let user = {username: this.localVariables.username, sessionId: store.getPokerSessionId};

                store.getStompClient.send(
                  this.replaceRoomId(this.backendUrls.smUserAdd(), this.room.id),
                  {},
                  JSON.stringify(user)
                );

                store.generatePokerSessionId();

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
            });
        }
      },
      exit() {
        this.localVariables.subscriptions.forEach(function (subscription) {
          subscription.unsubscribe()
        })

        store.getStompClient.send(
          this.replaceRoomId(this.backendUrls.smUserRemove(), this.room.id),
          {},
          JSON.stringify({username: this.localVariables.username, sessionId: store.getPokerSessionId})
        );

        store.setPokerRoomIdNull();

        this.setStateToDefault();
        setTimeout(() => {
          this.localFlags.spinnerVisible = false;
        }, 1000);
      },
      subscribe() {
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(this.replaceRoomId(this.backendUrls.stUserAdd(), this.room.id), this.userJoinedMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(this.replaceRoomId(this.backendUrls.stUserRemove(), this.room.id), this.userRemovedMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(this.replaceRoomId(this.backendUrls.stStoryAdd(), this.room.id), this.storyAddMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(this.replaceRoomId(this.backendUrls.stStoryEdit(), this.room.id), this.storyEditMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(this.replaceRoomId(this.backendUrls.stStoryDelete(), this.room.id), this.storyDeleteMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(this.replaceRoomId(this.backendUrls.stStorySelect(), this.room.id), this.storySelectedByAuthorMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(this.replaceRoomId(this.backendUrls.stVoteOpenClose(), this.room.id), this.openVotingMessageReceived));
        this.localVariables.subscriptions.push(store.getStompClient.subscribe(this.replaceRoomId(this.backendUrls.stVote(), this.room.id), this.votesMessageReceived));
        store.setPokerUsernameAuthorRoomId(this.localVariables.username, this.room.author.username, this.room.id);
      },
      storyAddMessageReceived(payload) {
        let payloadBody = JSON.parse(payload.body);
        let story = payloadBody.body;
        this.room.stories.push(story);

        if (this.room.stories.length === 1) {
          story.disabled = false;

          store.getStompClient.send(
            this.replaceRoomIdAndStoryId(this.backendUrls.smStorySelect(), this.room.id, this.room.stories[0]["id"]),
            {}
          );
        }

      },
      storySelectedByAuthorMessageReceived(payload) {
        let payloadBody = JSON.parse(payload.body);
        let selectedStory = payloadBody.body;

        let story = this.room.stories.find(story => story.id === selectedStory.storyId);
        story.disabled = selectedStory.disabled;
        this.room.selectedStory = story;
        if (this.isCurrentUserAuthor() || !this.localVariables.selectedStory) {
          this.localVariables.selectedStory = story;
          this.localVariables.tab = story.storyName;
        }
        this.room.currentVoteResult = this.getLastVoteResult(story);

        this.room.stories = this.room.stories.map(s1 => [ story ].find(s2 => s2.id === s1.id) || s1);
      },
      openVotingMessageReceived(payload) {
        let payloadBody = JSON.parse(payload.body);
        let message = payloadBody.body;

        this.localFlags.votingIsOpen = message.open;
        this.localFlags.showVoteOptions = this.localFlags.votingIsOpen;
        this.room.currentVoteResult = message.voteResult;
        this.room.selectedStoryId = message.storyId;

        if (!this.localFlags.votingIsOpen) {
          this.localVariables.chosenOption = null;
        }

        for (let i = 0; i < this.room.stories.length; i++) {
          let story = this.room.stories[i];
          if (story.id === this.room.selectedStory.id) {
            if (story.voteResults.length === 0) {
              story.voteResults.push(this.room.currentVoteResult);
            } else {
              let ids = story.voteResults.map(vr => {
                return vr.id;
              });
              if (!ids.includes(this.room.currentVoteResult.id)) {
                story.voteResults.push(this.room.currentVoteResult);
              }
            }
          }
        }

        let selectedStory = this.room.stories.find(story => story.id === this.room.selectedStoryId);
        this.room.selectedStory = selectedStory;

        if (this.localFlags.votingIsOpen) {
          this.localVariables.tab = selectedStory.storyName;
        } else {
          this.localVariables.tab = this.localVariables.selectedStory.storyName;
        }
      },
      votesMessageReceived(payload) {
        let payloadBody = JSON.parse(payload.body);
        let vote = payloadBody.body;

        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i].id === this.room.selectedStory.id) {
            let lastVoteResult = this.getLastVoteResult(this.room.stories[i]);
            lastVoteResult.votes.push(vote);
            this.room.stories[i].voteResults = this.room.stories[i].voteResults.map(s1 => [ lastVoteResult ].find(s2 => s2.id === s1.id) || s1);
            this.room.currentVoteResult = lastVoteResult;
          }
        }

        if (this.isCurrentUserAuthor() && this.room.currentVoteResult.votes.length === this.room.users.length) {
          this.finishVoting();
        }
      },
      userJoinedMessageReceived(payload) {
        let payloadBody = JSON.parse(payload.body);
        let userObj = payloadBody.body;
        this.room.users.push({username: userObj.username});
      },
      userRemovedMessageReceived(payload) {
        let payloadBody = JSON.parse(payload.body);
        let userObj = payloadBody.body;

        this.room.users = this.room.users.filter(user => user.username !== userObj.username);
      },
      startVoting() {
        store.getStompClient.send(
          this.replaceRoomIdAndStoryId(this.backendUrls.smVoteOpen(), this.room.id, this.room.selectedStory.id),
          {}
        );
      },
      finishVoting() {
        store.getStompClient.send(
          this.replaceRoomIdAndStoryId(this.backendUrls.smVoteClose(), this.room.id, this.room.selectedStory.id),
          {}
        );
      },
      calculateVotesAvg() {
        if (this.localVariables.selectedStory && this.localVariables.selectedStory.voteResults && !this.localFlags.votingIsOpen) {
          let voteResult = this.getLastVoteResult(this.localVariables.selectedStory);
          if (voteResult && voteResult.votes && voteResult.votes.length > 0) {
            let total = voteResult.votes.reduce((acc, vote) => { return acc + vote.value; }, 0);
            return 'Average: ' + parseFloat(total / voteResult.votes.length).toFixed(2);
          }
        }
        return "";
      },
      getVoteValue(user) {
        let story;
        if (this.localFlags.votingIsOpen) { 
          if (!this.localVariables.chosenOption) {
            return "?";
          }

          story = this.room.selectedStory; 
        } else if (this.localVariables.selectedStory) { 
          story = this.localVariables.selectedStory; 
        }


        if (story) {
          let voteResult = this.getLastVoteResult(story);
          if (voteResult && voteResult.votes) {
            let votes = voteResult.votes;
            let vote = votes.find(v => v.user.username === user.username);
      
            if (vote && vote.value) {
              return vote.value;
            }
          }
        }

        return "?";
      },
      getLastVoteResult(story) {
        if (story && story.id !== null && story.voteResults && story.voteResults.length > 0) {
          let ids = story.voteResults.map(vr => {
            return vr.id;
          });

          let lastVoteResultId = Math.max(...ids);
          let lastVoteResult = story.voteResults.find(vr => vr.id === lastVoteResultId);

          return lastVoteResult;
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
        let selectedStory = this.localVariables.selectedStory;
        if (!this.localFlags.votingIsOpen && selectedStory && selectedStory.voteResults) {
          let voteResult = this.getLastVoteResult(selectedStory);
          if (voteResult && voteResult.votes) {
            let vote = voteResult.votes.find(vote => this.isCurrentUser(vote.user.username, vote.user.sessionId));

            if (vote && vote.value) {
              return vote.value === voteValue ? "red" : "green";
            }
          }
        }

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
      createPage() {
        if (store.getStompClientStatus && store.getPokerRoomId) {
          this.localVariables.roomId = store.getPokerRoomId;
          this.localVariables.username = store.getPokerUsername;
          this.localVariables.author = store.getPokerAuthor;
          axios.get(this.backendUrls.getRoom() + "/" + this.localVariables.roomId)
            .then(response => {
              if (response.data != null) {
                this.room = response.data;
                this.subscribe();
                this.localFlags.showCreateAndJoinRoomComponents = false;

                for (let i = 0; i < this.room.stories.length; i++) {
                  if (this.room.stories[i].id === this.room.selectedStoryId) {
                    this.room.selectedStory = this.room.stories[i];
                    this.localVariables.selectedStory = this.room.selectedStory;
                    this.localVariables.tab = this.room.selectedStory.storyName;
                  }
                }

                localStorage.setItem('poker-username', this.localVariables.username);
                this.room.currentVoteResult = this.getLastVoteResult(this.room.selectedStory);

                store.getStompClient.send(
                  this.replaceRoomId(this.backendUrls.smUserAdd(), this.room.id),
                  {},
                  JSON.stringify({username: this.localVariables.username, sessionId: store.getPokerSessionId})
                );

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
              return this.localFlags.votingIsOpen;
            }
            return true;
          case 'stop':
            if (this.isCurrentUserAuthor()) {
              return !this.localFlags.votingIsOpen;
            }
            return true;
          case 'next':
            if (this.isCurrentUserAuthor()) {
              return this.localFlags.votingIsOpen || index === this.room.stories.length - 1;
            }
            return true;
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
      isCurrentUserAuthor() {
        return this.room.author.username === this.localVariables.author && this.room.author.sessionId === store.getPokerSessionId;
      },
      isCurrentUser(username, sessionId) {
        return username === this.localVariables.username && sessionId === store.getPokerSessionId;
      },
      deleteStory(storyId) {
        if (this.isCurrentUserAuthor() && !this.localFlags.votingIsOpen) {
          store.getStompClient.send(
            this.replaceRoomIdAndStoryId(this.backendUrls.smStoryDelete(), this.room.id, storyId),
            {}
          );
        }
      },
      enableStoryEditAlert(storyId) {
        this.localVariables.alertInput = this.room.stories.find(s => s.id === storyId).storyName;
        this.localVariables.alert = true;
        this.localVariables.alertStoryId = storyId;
      },
      sendEditMessage() {
        this.localVariables.alertInput = this.localVariables.alertInput.trim();
        if (this.isCurrentUserAuthor() && this.localVariables.alertInput) {
          store.getStompClient.send(
            this.replaceRoomIdAndStoryId(this.backendUrls.smStoryEdit(), this.room.id, this.room.selectedStory["id"]),
            {},
            this.localVariables.alertInput
          );

          this.localVariables.alertInput = "";
        }
      },
      storyEditMessageReceived(payload) {
        let payloadBodyObject = JSON.parse(payload.body);
        
        let story = payloadBodyObject.body;
        for (let i = 0; i < this.room.stories.length; i++) {
          if (this.room.stories[i].id === this.room.selectedStory.id) {
            this.room.stories[i].storyName = story.storyname;
            this.room.selectedStory.storyName = story.storyName;
            this.localVariables.tab = story.storyName;
          }
        }
      },
      storyDeleteMessageReceived(payload) {
        let payloadBodyObject = JSON.parse(payload.body);
        let story = payloadBodyObject.body;

        this.room.stories = this.room.stories.filter(s => s.id !== story.id);

        if (this.room.stories.length > 0 && this.localVariables.selectedStory.id === story.id) {
          this.room.stories[0].disabled = false;
          this.room.selectedStory = this.room.stories[0];
          this.room.selectedStoryId = this.room.stories[0].id;
          this.room.currentVoteResult = this.getLastVoteResult(this.room.stories[0]);
          this.localVariables.selectedStory = this.room.stories[0];
          this.localVariables.tab = this.room.stories[0].storyName;
        }
      },
      replaceRoomId(url, roomId) {
        return url.replace(this.backendUrls.placeHolders.roomId, roomId);
      },      
      replaceRoomIdAndStoryId(url, roomId, storyId) {
        return this.replaceRoomId(url, roomId)
                   .replace(this.backendUrls.placeHolders.storyId, storyId);
      },
      getStoryName(storyName) {
        if (this.localFlags.votingIsOpen) {
          return this.room.selectedStory.storyName;
        }

        return storyName;
      },
      jumpToCurrentStory() {
        if (this.localVariables.selectedStory.id !== this.room.selectedStory.id) {
          this.localVariables.selectedStory = this.room.selectedStory;
          this.localVariables.tab = this.room.selectedStory.storyName;
        }
      },
      openSettings() {
        console.log("Open settings... Not implemented yet!");
      },
      getStoryDisabled(story) {
        if (this.localFlags.votingIsOpen && !this.localVariables.chosenOption) return true;
        return story.disabled;
      }
    }
})
</script>

<style lang="sass" scoped>
.overlapping
  border: 2px solid white
  position: absolute
</style>
