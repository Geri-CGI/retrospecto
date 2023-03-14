<template>
  <q-page class="q-pa-md">
    <q-dialog v-model="alert">
      <q-card>
        <q-card-section>
          <div class="text-h6">Edit the message:</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input v-model="alertMessage.cardMessage" type="textarea">
          </q-input>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" flat label="Ok" @click="sendEditMessage"/>
          <q-btn v-close-popup color="negative" flat label="Close"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <div class="column justify-center items-center content-center">
      <div class="row justify-center items-center content-center">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 ">
          <div v-if="spinnerVisible"
               class="row justify-center" style="padding: 30px">
            <q-spinner
              :thickness="10"
              color="primary"
              size="5em"
            />
          </div>
          <div v-if="noWebsocketConnectionVisible" class="row justify-center" style="padding: 30px">
            <div class="text-h2">No websocket connection reload page!</div>
          </div>
        </div>
      </div>
      <div class="row">
        <div v-if="joinAndCreateButtonVisible" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6"
             style="padding: 30px">
          <q-card style="max-width: 300px">
            <q-card-section>
              <div class="text-h5">Create a new session</div>
            </q-card-section>
            <q-card-actions>
              <div class="row justify-center">
                <q-input v-model="author" :error="createUsernameValid" bg-color="white"
                         error-message="Username required!"
                         label="Username" style="padding: 30px" type="text" @keydown.enter="createBoard"/>
                <q-btn color="primary" label="Create" @click="createBoard"/>
              </div>
            </q-card-actions>
          </q-card>
        </div>
        <div v-if="joinAndCreateButtonVisible" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6"
             style="padding: 30px">
          <q-card style="max-width: 300px">
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
                  <q-input v-model="boardId" :error="boardIdValid" :error-message="boardIdErrorMessage" bg-color="white"
                           label="Board ID"
                           type="text"/>
                </div>
                <div class="col-12 text-center" style="padding: 10px">
                  <q-btn color="secondary" label="Join" @click="joinBoard"/>
                </div>
              </div>
            </q-card-actions>
          </q-card>
        </div>
      </div>
    </div>
    <div class="row justify-center">
      <div v-if="messageInputVisible" class="row">
        <q-bar class="text-blue col-12" dark>
          <div class="col text-left text-weight-bold">
            Host: {{ retroBoard.author }}
            <q-list>
              <template v-for="(user, index) in retroBoard.users" :key="index">
                <q-avatar color="primary" size="lg" text-color="white">
                  {{ getFirstLetter(user) }}
                  <q-tooltip>
                    {{ user }}
                  </q-tooltip>
                </q-avatar>
              </template>
            </q-list>
          </div>
          <div class="col text-left text-weight-bold">
            Board ID: {{ boardId }}
          </div>
          <q-btn-dropdown v-if="userIsAuthor()" color="primary" label="Menu">
            <q-list>
              <q-item v-close-popup clickable @click="orderByLikes">
                <q-item-section>
                  <q-item-label>Order by likes</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="secondary" icon="thumb_up" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-close-popup clickable @click="orderByDislikes">
                <q-item-section>
                  <q-item-label>Order by dislikes</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="red-13" icon="thumb_down" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-close-popup clickable @click="lockTheBoard">
                <q-item-section>
                  <q-item-label>Lock the board</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="warning" icon="lock" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-close-popup clickable @click="exportTheBoard">
                <q-item-section>
                  <q-item-label>Export the board</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="primary" icon="download" size="sm" text-color="white"/>
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
          <q-btn v-if="!userIsAuthor()" color="red-13" icon="logout" size="sm" @click="exit"/>
        </q-bar>
        <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputExpectColumn" bg-color="primary" bottom-slots color="white"
                       label="What I expected:" label-color="white"
                       outlined rounded type="text" @keydown.enter="sendExpectMessage">
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.expectColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card v-touch-hold="card.show" @mouseleave="card.show = false" @mouseover="card.show = true">
                      <q-card-section>
                        <div class="row">
                          {{ card.cardMessage }}
                        </div>
                        <q-badge color="transparent" floating>
                          <div class="row">
                            <div class="col-4 q-pl-xs">
                              <q-badge color="secondary">{{ card.likes }}</q-badge>
                            </div>
                            <div class="col-4 q-pl-xs q-pr-md">
                              <q-badge color="red-13">{{ card.dislikes }}</q-badge>
                            </div>
                            <div class="col-4 q-pa-lg-md">
                              <q-badge color="orange">{{ card.username }}</q-badge>
                            </div>
                          </div>
                        </q-badge>
                        <q-slide-transition>
                          <div v-show="card.show" class="column">
                            <div class="col self-end">
                              <q-btn v-if="!likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="cancel" round size="sm"
                                     @click="removeLike(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="secondary"
                                     icon="thumb_up" round size="sm"
                                     @click="like(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="thumb_down" round size="sm"
                                     @click="dislike(card, index)"/>
                              <q-btn v-if="author || username === card.username" color="warning" icon="edit" round
                                     size="sm"
                                     @click="enableAlert(card, index)"/>
                              <q-btn v-if="author || username === card.username" color="negative" icon="delete" round
                                     size="sm"
                                     @click="deleteCardExpectColumn(index)"/>
                            </div>
                          </div>
                        </q-slide-transition>
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputWentWellColumn" bg-color="secondary" bottom-slots color="white" label="Went well:"
                       label-color="white"
                       outlined rounded type="text" @keydown.enter="sendWellMessage">
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.wentWellColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card v-touch-hold="card.show" @mouseleave="card.show = false" @mouseover="card.show = true">
                      <q-card-section>
                        <div class="row">
                          {{ card.cardMessage }}
                        </div>
                        <q-badge color="transparent" floating>
                          <div class="row">
                            <div class="col-4 q-pl-xs">
                              <q-badge color="secondary">{{ card.likes }}</q-badge>
                            </div>
                            <div class="col-4 q-pl-xs q-pr-md">
                              <q-badge color="red-13">{{ card.dislikes }}</q-badge>
                            </div>
                            <div class="col-4 q-pa-lg-md">
                              <q-badge color="orange">{{ card.username }}</q-badge>
                            </div>
                          </div>
                        </q-badge>
                        <q-slide-transition>
                          <div v-show="card.show" class="column">
                            <div class="col self-end">
                              <q-btn v-if="!likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="cancel" round size="sm"
                                     @click="removeLike(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="secondary"
                                     icon="thumb_up" round size="sm"
                                     @click="like(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="thumb_down" round size="sm"
                                     @click="dislike(card, index)"/>
                              <q-btn v-if="author || username === card.username" color="warning" icon="edit" round
                                     size="sm"
                                     @click="enableAlert(card, index)"/>
                              <q-btn v-if="author || username === card.username" color="negative" icon="delete" round
                                     size="sm"
                                     @click="deleteCardWentWellColumn(index)"/>
                            </div>
                          </div>
                        </q-slide-transition>
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputDidNotGoWellColumn" bg-color="negative" bottom-slots color="white"
                       label="Went wrong:" label-color="white"
                       outlined rounded type="text" @keydown.enter="sendNotWellMessage">
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.didNotGoWellColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card v-touch-hold="card.show" @mouseleave="card.show = false" @mouseover="card.show = true">
                      <q-card-section>
                        <div class="row">
                          {{ card.cardMessage }}
                        </div>
                        <q-badge color="transparent" floating>
                          <div class="row">
                            <div class="col-4 q-pl-xs">
                              <q-badge color="secondary">{{ card.likes }}</q-badge>
                            </div>
                            <div class="col-4 q-pl-xs q-pr-md">
                              <q-badge color="red-13">{{ card.dislikes }}</q-badge>
                            </div>
                            <div class="col-4 q-pa-lg-md">
                              <q-badge color="orange">{{ card.username }}</q-badge>
                            </div>
                          </div>
                        </q-badge>
                        <q-slide-transition>
                          <div v-show="card.show" class="column">
                            <div class="col self-end">
                              <q-btn v-if="!likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="cancel" round size="sm"
                                     @click="removeLike(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="secondary"
                                     icon="thumb_up" round size="sm"
                                     @click="like(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="thumb_down" round size="sm"
                                     @click="dislike(card, index)"/>
                              <q-btn v-if="author || username === card.username" color="warning" icon="edit" round
                                     size="sm"
                                     @click="enableAlert(card, index)"/>
                              <q-btn v-if="author || username === card.username" color="negative" icon="delete" round
                                     size="sm"
                                     @click="deleteCardNotWellColumn(index)"/>
                            </div>
                          </div>
                        </q-slide-transition>
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputWantToTryColumn" bg-color="info" bottom-slots color="white"
                       label="What I want to try:" label-color="white"
                       outlined rounded type="text" @keydown.enter="sendTryMessage">
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.wantToTryColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card v-touch-hold="card.show" @mouseleave="card.show = false" @mouseover="card.show = true">
                      <q-card-section>
                        <div class="row">
                          {{ card.cardMessage }}
                        </div>
                        <q-badge color="transparent" floating>
                          <div class="row">
                            <div class="col-4 q-pl-xs">
                              <q-badge color="secondary">{{ card.likes }}</q-badge>
                            </div>
                            <div class="col-4 q-pl-xs q-pr-md">
                              <q-badge color="red-13">{{ card.dislikes }}</q-badge>
                            </div>
                            <div class="col-4 q-pa-lg-md">
                              <q-badge color="orange">{{ card.username }}</q-badge>
                            </div>
                          </div>
                        </q-badge>
                        <q-slide-transition>
                          <div v-show="card.show" class="column">
                            <div class="col self-end">
                              <q-btn v-if="!likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="cancel" round size="sm"
                                     @click="removeLike(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="secondary"
                                     icon="thumb_up" round size="sm"
                                     @click="like(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="thumb_down" round size="sm"
                                     @click="dislike(card, index)"/>
                              <q-btn v-if="author || username === card.username" color="warning" icon="edit" round
                                     size="sm"
                                     @click="enableAlert(card, index)"/>
                              <q-btn v-if="author || username === card.username" color="negative" icon="delete" round
                                     size="sm"
                                     @click="deleteCardTryColumn(index)"/>
                            </div>
                          </div>
                        </q-slide-transition>
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref, watch} from 'vue'
import axios from 'axios'
import {stompClientStore} from 'stores/stomp'
import {storeToRefs} from "pinia";

const store = stompClientStore()
const {stompClientConnected} = storeToRefs(store)

export default defineComponent({
  name: 'BoardPage',
  data() {
    return {
      boardId: null,
      messageInput: null,
      messageInputVisible: false,
      joinAndCreateButtonVisible: false,
      username: null,
      author: null,
      retroBoard: {
        id: null,
        author: null,
        expectColumn: [],
        wentWellColumn: [],
        didNotGoWellColumn: [],
        wantToTryColumn: [],
        likedRecords: new Map(),
        users: []
      },
      inputWentWellColumn: null,
      inputExpectColumn: null,
      inputDidNotGoWellColumn: null,
      inputWantToTryColumn: null,
      createUsernameValid: false,
      joinUsernameValid: false,
      boardIdValid: false,
      retroBoardMessage: {
        uniqueId: null,
        username: null,
        cardMessage: null,
        columnType: null,
        index: null,
        show: false,
        likes: 0,
        dislikes: 0,
        likedOrDisliked: false
      },
      boardIdErrorMessage: 'Board ID required!',
      ratingModel: ref(3),
      menu: false,
      noWebsocketConnectionVisible: false,
      spinnerVisible: false,
      alert: false,
      alertMessage: null,
      subscriptions: []
    }
  },
  created() {
    this.spinnerVisible = true
    setTimeout(this.reload, 1000)
    watch(stompClientConnected, () => {
      if (!stompClientConnected) {
        this.exit()
      }
    })
    window.addEventListener("beforeunload", this.exit)
  },
  beforeUnmount() {
    store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/user.remove", {});
  },
  unmounted() {
    this.subscriptions.forEach(function (subscription, index) {
      subscription.unsubscribe()
    })
  },
  methods: {
    reload() {
      if (stompClientStore().getStompClientStatus) {
        if (stompClientStore().getRetroBoardId) {
          this.boardId = stompClientStore().getRetroBoardId
          this.username = stompClientStore().getUsername
          this.author = stompClientStore().getAuthor
          axios.get(`https://www.retrospecto.cloud/board/` + this.boardId)
            .then(response => {
              if (response.data != null) {
                this.spinnerVisible = false
                this.retroBoard = response.data
                this.retroBoard.likedRecords = new Map(Object.entries(this.retroBoard.likedRecords))
                this.messageInputVisible = true
                this.joinAndCreateButtonVisible = false
                this.subscribe()
              }
            })
        } else {
          this.spinnerVisible = false
          this.joinAndCreateButtonVisible = true
          this.messageInputVisible = false
        }
      } else {
        this.spinnerVisible = false
        this.noWebsocketConnectionVisible = true
        this.joinAndCreateButtonVisible = false
        this.messageInputVisible = false
      }
    },
    deleteCardExpectColumn(key) {
      let retroBoardMessage = this.retroBoard.expectColumn[key]
      retroBoardMessage.index = key
      store.getStompClient.send("/app/board/" + this.boardId + "/card.delete", {}, JSON.stringify(retroBoardMessage));
    },
    deleteCardWentWellColumn(key) {
      let retroBoardMessage = this.retroBoard.wentWellColumn[key]
      retroBoardMessage.index = key
      store.getStompClient.send("/app/board/" + this.boardId + "/card.delete", {}, JSON.stringify(retroBoardMessage));
    },
    deleteCardNotWellColumn(key) {
      let retroBoardMessage = this.retroBoard.didNotGoWellColumn[key]
      retroBoardMessage.index = key
      store.getStompClient.send("/app/board/" + this.boardId + "/card.delete", {}, JSON.stringify(retroBoardMessage));
    },
    deleteCardTryColumn(key) {
      let retroBoardMessage = this.retroBoard.wantToTryColumn[key]
      retroBoardMessage.index = key
      store.getStompClient.send("/app/board/" + this.boardId + "/card.delete", {}, JSON.stringify(retroBoardMessage));
    },
    sendExpectMessage() {
      this.retroBoardMessage = {
        username: this.username,
        cardMessage: this.inputExpectColumn,
        columnType: 'EXPECT'
      }
      if (this.inputExpectColumn != null) {
        store.getStompClient.send("/app/board/" + this.boardId + "/card.add", {}, JSON.stringify(this.retroBoardMessage));
        this.inputExpectColumn = null
      }
    },
    sendWellMessage() {
      this.retroBoardMessage = {
        username: this.username,
        cardMessage: this.inputWentWellColumn,
        columnType: 'WELL'
      }
      if (this.inputWentWellColumn != null) {
        store.getStompClient.send("/app/board/" + this.boardId + "/card.add", {}, JSON.stringify(this.retroBoardMessage));
        this.inputWentWellColumn = null
      }
    },
    sendNotWellMessage() {
      this.retroBoardMessage = {
        username: this.username,
        cardMessage: this.inputDidNotGoWellColumn,
        columnType: 'NOT_WELL'
      }
      if (this.inputDidNotGoWellColumn != null) {
        store.getStompClient.send("/app/board/" + this.boardId + "/card.add", {}, JSON.stringify(this.retroBoardMessage));
        this.inputDidNotGoWellColumn = null
      }
    },
    sendTryMessage() {
      this.retroBoardMessage = {
        username: this.username,
        cardMessage: this.inputWantToTryColumn,
        columnType: 'TRY'
      }
      if (this.inputWantToTryColumn != null) {
        store.getStompClient.send("/app/board/" + this.boardId + "/card.add", {}, JSON.stringify(this.retroBoardMessage));
        this.inputWantToTryColumn = null
      }
    },
    sendEditMessage() {
      let updatedMessage = this.alertMessage
      store.getStompClient.send("/app/board/" + this.boardId + "/card.edit", {}, JSON.stringify(updatedMessage));
    },
    like(message, index) {
      message.index = index
      message.likedOrDisliked = true
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/card.like", {}, JSON.stringify(message));
    },
    dislike(message, index) {
      message.index = index
      message.likedOrDisliked = true
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/card.dislike", {}, JSON.stringify(message));
    },
    removeLike(message, index) {
      message.index = index
      message.likedOrDisliked = false
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/card.removeLike", {}, JSON.stringify(message));
    },
    likeButtonsVisible(uniqueId, likedOrDisliked) {
      try {
        const map = this.retroBoard.likedRecords
        if (map.has(this.username)) {
          if (map.get(this.username).includes(uniqueId)) {
            return false
          } else {
            return !likedOrDisliked
          }
        }
      } catch (error) {
        return !likedOrDisliked
      }
    },
    onAddMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn.push(retroBoardMessage)
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn.push(retroBoardMessage)
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn.push(retroBoardMessage)
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn.push(retroBoardMessage)
      }
    },
    onDeleteMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn.splice(retroBoardMessage.index, 1)
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn.splice(retroBoardMessage.index, 1)
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn.splice(retroBoardMessage.index, 1)
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn.splice(retroBoardMessage.index, 1)
      }
    },
    onEditMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn[retroBoardMessage.index].cardMessage = retroBoardMessage.cardMessage
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn[retroBoardMessage.index].cardMessage = retroBoardMessage.cardMessage
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn[retroBoardMessage.index].cardMessage = retroBoardMessage.cardMessage
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn[retroBoardMessage.index].cardMessage = retroBoardMessage.cardMessage
      }
    },
    onLikeMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn[retroBoardMessage.index].likes = retroBoardMessage.likes
        this.retroBoard.expectColumn[retroBoardMessage.index].dislikes = retroBoardMessage.dislikes
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn[retroBoardMessage.index].likes = retroBoardMessage.likes
        this.retroBoard.wentWellColumn[retroBoardMessage.index].dislikes = retroBoardMessage.dislikes
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn[retroBoardMessage.index].likes = retroBoardMessage.likes
        this.retroBoard.didNotGoWellColumn[retroBoardMessage.index].dislikes = retroBoardMessage.dislikes
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn[retroBoardMessage.index].likes = retroBoardMessage.likes
        this.retroBoard.wantToTryColumn[retroBoardMessage.index].dislikes = retroBoardMessage.dislikes
      }
    },
    onReorderMessageReceived(payload) {
      this.retroBoard = JSON.parse(payload.body)
      this.retroBoard.likedRecords = new Map(Object.entries(this.retroBoard.likedRecords))
    },
    onUserMessageReceived(payload) {
      this.retroBoard.users = JSON.parse(payload.body)
    },
    enableAlert(message, index) {
      this.alert = true
      this.alertMessage = message
      this.alertMessage.index = index
    },
    orderByLikes() {
      store.getStompClient.send("/app/board/" + this.boardId + "/order.like", {}, JSON.stringify(null));
    },
    orderByDislikes() {
      store.getStompClient.send("/app/board/" + this.boardId + "/order.dislike", {}, JSON.stringify(null));
    },
    subscribe() {
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/add', this.onAddMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/delete', this.onDeleteMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/edit', this.onEditMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/like', this.onLikeMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/reorder', this.onReorderMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/user', this.onUserMessageReceived))
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/user.add", {});
      stompClientStore().setUsernameAuthorBoarDId(this.username, this.author, this.boardId)
    },
    exit() {
      this.subscriptions.forEach(function (subscription, index) {
        subscription.unsubscribe()
      })
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/user.remove", {});
      stompClientStore().setUsernameAuthorBoarDId(null, null, null)
      this.messageInputVisible = false
      this.joinAndCreateButtonVisible = true
      this.username = null
      this.boardId = null
      this.retroBoard = null
      this.author = null
    },
    getFirstLetter(username) {
      return Array.from(username)[0];
    },
    userIsAuthor() {
      return this.username === this.author
    },
    lockTheBoard() {
      //coming
    },
    exportTheBoard() {
      //coming
    },
    joinBoard() {
      if (!this.boardId && !this.username) {
        this.joinUsernameValid = true
        this.boardIdValid = true
        this.boardIdErrorMessage = "Board ID required!"
      }
      if (!this.username && this.boardId) {
        this.joinUsernameValid = true
        this.boardIdValid = false
      }
      if (this.username && !this.boardId) {
        this.boardIdValid = true
        this.boardIdErrorMessage = "Board ID required!"
        this.joinUsernameValid = false
      }
      if (this.boardId && this.username) {
        this.joinUsernameValid = false
        this.boardIdValid = false
        axios.get(`https://www.retrospecto.cloud/board/` + this.boardId)
          .then(response => {
            if (response.data != null) {
              this.retroBoard = response.data
              this.subscribe()
              this.messageInputVisible = true
              this.joinAndCreateButtonVisible = false
            }
          })
          .catch(error => {
            this.boardIdValid = true
            this.boardIdErrorMessage = 'Board ID does not exist!'
            this.boardId = null
            console.log(error)
          })
      }
    },
    createBoard() {
      if (!this.author) {
        this.createUsernameValid = true
      } else {
        axios.post(`https://www.retrospecto.cloud/board/create/` + this.author)
          .then(response => {
            // JSON responses are automatically parsed.
            if (response.data != null) {
              this.retroBoard = response.data
              this.boardId = response.data.id
              this.createUsernameValid = false
              this.username = this.author
              this.messageInputVisible = true
              this.joinAndCreateButtonVisible = false
              this.subscribe()
            }
          })
      }
    }
  }
})
</script>
