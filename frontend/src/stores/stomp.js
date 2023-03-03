import {defineStore} from 'pinia'
import SockJs from 'sockjs-client/dist/sockjs.min.js'
import {Stomp} from "@stomp/stompjs"

export const stompClientStore = defineStore('stompClientStore', {
  state: () => ({
    counter: 0,
    stompClient: null,
    stompClientConnected: false
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
    }
  },

  actions: {
    increment() {
      this.counter++
    },
    connect() {
      let url = "https://www.retrospecto.cloud/ws"
      let socket = new SockJs(url);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, this.onConnected, this.onError, this.onClose);
    },
    onConnected() {
      this.stompClientConnected = true
    },
    onError() {
      this.stompClientConnected = false
    },
    onClose() {
      this.stompClientConnected = false
    }
  }
})
