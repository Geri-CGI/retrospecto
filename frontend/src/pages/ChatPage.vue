<template>
  <div class="column q-pa-xl full-height justify-center absolute-center full-width"
       style="padding-top: 60px; padding-bottom: 60px">
    <q-scroll-area class="col" ref="chatScroll">
      <q-list>
        <template v-for="(chatItem, index) in messages" :key="index">
          <q-chat-message v-if="chatItem.type === 'JOIN' || chatItem.type === 'LEAVE'"
                          :label="chatItem.content + ' - ' + chatItem.localDateTime"
          />
          <q-chat-message v-if="chatItem.type === 'CHAT'"
                          :name="chatItem.sender === username ? 'me' : chatItem.sender"
                          :text="[chatItem.content]"
                          :sent="username === chatItem.sender"
                          :stamp="chatItem.localDateTime"
          />
        </template>
      </q-list>
    </q-scroll-area>
    <div class="row full-width text-center items-center">
      <div class="col-12">
        <q-input type="text" bottom-slots v-model="username" placeholder="Username" @keydown.enter="connect"
                 v-if="usernameInputVisible">
          <template v-slot:append>
            <q-btn color="primary" @click="connect">Start Chatting</q-btn>
          </template>
        </q-input>
        <q-input type="text" bottom-slots v-model="messageInput" label="Text" @keydown.enter="sendMessage"
                 v-if="chatInputVisible">
          <template v-slot:append>
            <q-icon v-if="messageInput !== ''" name="close" @click="messageInput = ''" class="cursor-pointer"/>
            <q-btn color="primary" round flat icon="send" @click="sendMessage"/>
          </template>
        </q-input>
      </div>
    </div>
  </div>
</template>

<script>
import {defineComponent} from 'vue'
import SockJs from 'sockjs-client/dist/sockjs.min.js'
import {Stomp} from "@stomp/stompjs"
import axios from 'axios'

let stompClient = null;

export default defineComponent({
  name: 'ChatPage',
  data() {
    return {
      usernameInputVisible: true,
      chatInputVisible: false,
      messageInput: '',
      username: '',
      messages: []
    }
  },
  created() {
    this.getMessages()
  },
  methods: {
    connect(event) {
      if (this.username) {
        this.usernameInputVisible = false
        this.chatInputVisible = true
        let url = "https://www.retrospecto.cloud/ws"
        let socket = new SockJs(url);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, this.onConnected, this.onError, this.onClose);
      }
      event.preventDefault();
    },
    onConnected() {
      this.usernameInputVisible = false
      this.chatInputVisible = true
      // Subscribe to the Public Topic
      stompClient.subscribe('/topic/public', this.onMessageReceived);

      // Tell your username to the server
      stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: this.username, type: 'JOIN'})
      )
    },
    sendMessage(event) {
      let messageContent = this.messageInput;
      if (messageContent && stompClient) {
        let chatMessage = {
          sender: this.username,
          content: this.messageInput,
          type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        this.messageInput = '';
      }
      event.preventDefault();
    },
    onMessageReceived(payload) {
      let message = JSON.parse(payload.body);
      if (message.type === 'JOIN') {
        message.content = message.sender + ' joined!';
      } else if (message.type === 'LEAVE') {
        message.content = message.sender + ' left!';
      }
      this.messages.push(message)
      this.scrollToBottom()
    },
    onError() {

    },
    onClose() {
      this.usernameInputVisible = true
      this.chatInputVisible = false
    },
    scrollToBottom() {
      const scrollArea = this.$refs.chatScroll;
      const scrollTarget = scrollArea.getScrollTarget();
      const duration = 300; // ms - use 0 to instant scroll
      scrollArea.setScrollPosition("vertical", scrollTarget.scrollHeight, duration);
    },
    getMessages() {
      axios.get(`https://www.retrospecto.cloud/chat/messages`)
        .then(response => {
          // JSON responses are automatically parsed.
          this.messages = response.data
          this.scrollToBottom()
        })
    }
  }
})
</script>
