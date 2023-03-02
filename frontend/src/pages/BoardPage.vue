<template>
  <q-page class="q-pa-md flex flex-center">
    <div class="row">
      <div v-if="joinAndCreateButtonVisible" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6"
           style="padding: 30px">
        <q-card>
          <q-card-section>
            <div class="text-h5">Create a new session</div>
          </q-card-section>
          <q-card-actions>
            <div class="row justify-center">
              <q-input v-model="author" :error="createUsernameValid" bg-color="white" error-message="Username required!"
                       label="Username" style="padding: 30px" type="text"/>
              <q-btn color="primary" label="Create" @click="createRandomBoardId"/>
            </div>
          </q-card-actions>
        </q-card>
      </div>
      <div v-if="joinAndCreateButtonVisible" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 justify-center"
           style="padding: 30px">
        <q-card>
          <q-card-section>
            <div class="text-h5">Join a session</div>
          </q-card-section>
          <q-card-actions class="justify-center">
            <div class="justify-center">
              <div class="col-12" style="padding: 5px">
                <q-input v-model="username" :error="joinUsernameValid" bg-color="white" error-message="Username required!" label="Username"
                         type="text"/>
              </div>
              <div class="col-12" style="padding: 5px">
                <q-input v-model="boardId" :error="boardIdValid" bg-color="white" error-message="Board Id required!" label="Board ID"
                         type="text"/>
              </div>
              <div class="col-12 text-center" style="padding: 10px">
                <q-btn color="secondary" label="Join" @click="connect"/>
              </div>
            </div>
          </q-card-actions>
        </q-card>
      </div>
      <div v-if="messageInputVisible" class="row">
        <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputExpectColumn" bg-color="primary" bottom-slots color="white" label="What I expected:" label-color="white"
                       outlined rounded type="text">
                <template v-slot:append>
                  <q-btn color="white" flat icon="send" @click="sendMessage(inputExpectColumn,'EXPECT')"/>
                </template>
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.expectColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card>
                      <q-card-section>
                        {{ card.cardMessage }}
                        <q-badge color="orange" floating>{{ card.username }}</q-badge>
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputWentWellColumn" bg-color="secondary" bottom-slots color="white" label="Went well:" label-color="white"
                       outlined rounded type="text">
                <template v-slot:append>
                  <q-btn color="white" flat icon="send" @click="sendMessage(inputWentWellColumn,'WELL')"/>
                </template>
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.wentWellColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card>
                      <q-card-section>
                        {{ card.cardMessage }}
                        <q-badge color="orange" floating>{{ card.username }}</q-badge>
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputDidNotGoWellColumn" bg-color="negative" bottom-slots color="white" label="Went wrong:" label-color="white"
                       outlined rounded type="text">
                <template v-slot:append>
                  <q-btn color="white" flat icon="send" @click="sendMessage(inputDidNotGoWellColumn,'NOT_WELL')"/>
                </template>
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.didNotGoWellColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card>
                      <q-card-section>
                        {{ card.cardMessage }}
                        <q-badge color="orange" floating>{{ card.username }}</q-badge>
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputWantToTryColumn" bg-color="info" bottom-slots color="white" label="What I want to try:" label-color="white"
                       outlined rounded type="text">
                <template v-slot:append>
                  <q-btn color="white" flat icon="send" @click="sendMessage(inputWantToTryColumn,'TRY')"/>
                </template>
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.wantToTryColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card class="my-card">
                      <q-card-section>
                        {{ card.cardMessage }}
                        <q-badge color="orange" floating>{{ card.username }}</q-badge>
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
import {defineComponent} from 'vue'
import SockJs from 'sockjs-client/dist/sockjs.min.js'
import {Stomp} from "@stomp/stompjs"
import axios from 'axios'

let stompClient = null;

export default defineComponent({
  name: 'BoardPage',
  data() {
    return {
      boardId: null,
      messageInput: null,
      messageInputVisible: false,
      joinAndCreateButtonVisible: true,
      username: null,
      author: null,
      retroBoard: {
        id: null,
        author: null,
        expectColumn: [],
        wentWellColumn: [],
        didNotGoWellColumn: [],
        wantToTryColumn: []
      },
      inputWentWellColumn: null,
      inputExpectColumn: null,
      inputDidNotGoWellColumn: null,
      inputWantToTryColumn: null,
      createUsernameValid: false,
      joinUsernameValid: false,
      boardIdValid: false
    }
  },
  created() {
  },
  methods: {
    connect() {
      if (this.username === null && this.boardId != null) {
        this.joinUsernameValid = true
        this.boardIdValid = false
      }
      if (this.boardId === null && this.username != null) {
        this.joinUsernameValid = false
        this.boardIdValid = true
      }
      if (this.boardId === null && this.username === null) {
        this.joinUsernameValid = true
        this.boardIdValid = true
      } else {
        this.joinUsernameValid = false
        this.boardIdValid = false
        this.messageInputVisible = true
        this.joinAndCreateButtonVisible = false
        let url = "https://www.retrospecto.cloud/ws"
        let socket = new SockJs(url);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, this.onConnected, this.onError, this.onClose);
      }
    },
    onConnected() {
      if (this.checkRetroBoardExists()) {
        this.getRetroBoard()
        stompClient.subscribe('/topic/board/' + this.boardId, this.onMessageReceived);
      }
    },
    sendMessage(input, columnType) {
      let retroBoardMessage = {
        username: this.username,
        cardMessage: input,
        columnType: columnType
      }
      if (retroBoardMessage && stompClient) {
        stompClient.send("/app/board/" + this.boardId + "/card.add", {}, JSON.stringify(retroBoardMessage));
        switch (columnType) {
          case 'EXPECT':
            this.inputExpectColumn = null
            break;
          case 'WELL':
            this.inputWentWellColumn = null
            break;
          case 'NOT_WELL':
            this.inputDidNotGoWellColumn = null
            break;
          case 'TRY':
            this.inputWantToTryColumn = null
            break;
        }
      }
    },
    onMessageReceived(payload) {
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
    createRandomBoardId() {
      if (this.author === null) {
        this.createUsernameValid = true
      } else {
        this.createUsernameValid = false
        this.username = this.author
        this.messageInputVisible = true
        this.joinAndCreateButtonVisible = false
        this.boardId = this.randomIntFromInterval()
        this.createRetroBoard()
        this.connect();
      }
    },
    randomIntFromInterval() { // min and max included
      return Math.floor(100000 + Math.random() * 900000)
    },
    onError() {
      this.messageInputVisible = false
      this.joinAndCreateButtonVisible = true
    },
    getRetroBoard() {
      axios.get(`https://www.retrospecto.cloud/board/` + this.boardId)
        .then(response => {
          // JSON responses are automatically parsed.
          this.retroBoard = response.data
        })
    },
    checkRetroBoardExists() {
      return axios.get(`https://www.retrospecto.cloud/board/` + this.boardId)
        .then(response => {
          return true
        })
        .catch(error => {
          return false
        })
    },
    createRetroBoard() {
      axios.post(`https://www.retrospecto.cloud/board/create/` + this.boardId + '/' + this.author)
        .then(response => {
          // JSON responses are automatically parsed.
          this.retroBoard = response.data
        })
    }
  }
})
</script>
