import {defineStore} from 'pinia'
import SockJs from 'sockjs-client/dist/sockjs.min.js'
import {Stomp} from "@stomp/stompjs"

export const stompClientStore = defineStore('stompClientStore', {
  state: () => ({
    stompClient: null,
    stompClientConnected: false,
    username: null,
    author: null,
    boardId: null,
    pokerUsername: null,
    pokerAuthor: null,
    pokerRoomId: null
  }),

  getters: {
    doubleCount(state) {
      return state.counter * 2
    },
    getStompClientStatus(state) {
      return state.stompClientConnected
    },
    getStompClient(state) {
      return state.stompClient
    },
    getRetroBoardId(state) {
      return state.boardId
    },
    getUsername(state) {
      return state.username
    },
    getAuthor(state) {
      return state.author
    },    
    getPokerRoomId(state) {
      return state.pokerRoomId
    },
    getPokerUsername(state) {
      return state.pokerUsername
    },
    getPokerAuthor(state) {
      return state.pokerAuthor
    },
  },

  actions: {
    connect() {
      let url = "https://www.retrospecto.cloud/ws";
      let socket = new SockJs(url);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, this.onConnected, this.onError, this.onClose);
    },
    onConnected() {
      this.stompClientConnected = true
    },
    onError() {
      this.stompClientConnected = false
      this.stompClient.connect({}, this.onConnected, this.onError, this.onClose);
    },
    onClose() {
      this.stompClientConnected = false
    },
    setUsernameAuthorBoardId(username, author, boardId) {
      this.username = username
      this.boardId = boardId
      this.author = author
    },
    setPokerUsernameAuthorRoomId(pokerUsername, pokerAuthor, pokerRoomId) {
      this.pokerUsername = pokerUsername
      this.pokerAuthor = pokerAuthor
      this.pokerRoomId = pokerRoomId
    }
  }
})
