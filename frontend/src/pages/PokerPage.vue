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

  <div v-if="!joinAndCreateButtonVisible">
    <div class="row">
      <div class="col-6">
          <h4>
            Room ID: {{ this.room.id }}
          </h4>
        </div>
        <div class="col-6">
          <h4>
            Username: {{ this.username }}
          </h4>
        </div>
    </div>
    <div class="q-pa-md flex flex-center">
      <q-input v-model="inputStory" bg-color="white" label="Story" type="text" @keydown.enter="createStory"/>
      <q-btn color="primary" label="Create" @click="createStory"/>
    </div>

      <div class="container">
        <div class="q-pa-md">
          <div v-for="story in room.stories" v-bind:key="story">
            <ul style="display: flex; justify-content:space-around; list-style-type:none;">
              <li>
                <h5 @click="selectStory(story)">{{ story["story"] }}</h5>
                <h5>Result: SOON</h5>
                <q-btn v-if="!this.showVoteOptions" color="primary" label="History" size="12px" @click="openHistory(story)"></q-btn>
                <div v-if="story['open']">
                  <div v-for="voteResult in story['voteResults']" v-bind:key="voteResult"> 
                    <div v-for="votes in voteResult" v-bind:key="votes"> 
                      <h5 v-for="vote in votes" v-bind:key="vote">
                        {{ voteResult["id"] }} {{ vote["username"] }} {{ vote["value"] }}
                      </h5>  
                    </div> 
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div class="q-pa-md flex flex-center">
          <div class="row">
            <h4 v-if="this.room.selectedStory != null">
            Current story: {{ this.room.selectedStory["story"] }}
            </h4>
          </div>
          <div v-if="this.room.selectedStory != null && this.author === this.room.author">
              <q-btn v-if="!this.votingIsOpen" color="primary" label="Start voting" size="22px" @click="startVoting()"/>
              <q-btn v-if="this.votingIsOpen" color="primary" label="Finish voting" size="22px" @click="finishVoting()"/>
          </div>
        </div>
      </div>

      <div class="container" v-if="this.showVoteOptions">
        <div class="q-pa-md flex flex-center">
            <q-btn v-for="prime in primeNumbersList" v-bind:key="prime" color="primary" :label="prime" size="28px" @click="vote(prime)"/>
        </div>
      </div>
  </div>
</template>

<script>
import {defineComponent, ref} from 'vue'
import axios from 'axios'
import {stompClientStore} from 'stores/stomp'

const store = stompClientStore()

export default defineComponent({
  name: 'PokerPage',
  methods: {
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
        let storyMessage = {story: this.inputStory};
        store.getStompClient.send("/app/poker/" + this.room.id + "/story/add", {}, JSON.stringify(storyMessage));
        this.inputStory = null;
      }
    },
    selectStory(story) {
      if (this.author === this.room.author && !this.votingIsOpen) {
        store.getStompClient.send("/app/poker/" + this.room.id + "/story/" + story["id"] + "/selected", {}/*, JSON.stringify({"storyId": story["id"]})*/);
      }
    },
    createRoom() {
      if (!this.author) {
        this.createUsernameValid = true
      } else {
        axios.post(this.backendUrls.createRoom(), { author: this.author })
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
              this.subscribe();
              this.joinAndCreateButtonVisible = false;
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
          this.room.selectedStory = this.room.stories[i];
        }
      }
    },
    openVotingMessageReceived(payload) {
      let votingIsOpenString = this.parseWSResponseBody(payload.body);
      let votingIsOpen = JSON.parse(votingIsOpenString);
      this.votingIsOpen = votingIsOpen["open"];
      this.showVoteOptions = this.votingIsOpen;
      for (let i = 0; i < this.room.stories.length; i++) {
        this.room.stories[i].open = false;        
      }
    },
    votesMessageReceived(payload) {
      let pokerRoomString = this.parseWSResponseBody(payload.body);
      let room = JSON.parse(pokerRoomString);
      this.room = room;
      for (let i = 0; i < this.room.stories.length; i++) {
        if (this.room.stories[i]["id"] === room["selectedStoryId"]) {
          this.room.selectedStory = this.room.stories[i];
        }
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
    openHistory(story) {
      if (story["open"]) {
        story["open"] = false;
      } else {
        story["open"] = true;
      }
    },
    parseWSResponseBody(body) {
      body = body.substring(body.indexOf("body") + 4);
      body = body.substring(body.indexOf("{"));
      body = body.substring(0, body.indexOf("statusCode") - 2);
      return body;
    }
  },
  data() {
    return {
      author: null,
      inputStory: null,
      joinAndCreateButtonVisible: true,
      selectedValue: 1,
      room: {
        id: null,
        author: null,
        stories: [],
        selectedStory: null,
      },
      username: null,
      createUsernameValid: false,
      joinUsernameValid: false,
      roomIdValid: false,
      roomIdErrorMessage: 'Room ID required!',
      primeNumbersList: [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97],
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
    }
  }
})
</script>
